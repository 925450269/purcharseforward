package cn.eight.purcharseforward.controller;

import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.service.UserService;
import cn.eight.purcharseforward.service.impl.UserServiceImp;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


@WebServlet("/qiantai/UserSvl")
public class UserSvl extends HttpServlet {
    UserService service = new UserServiceImp();
    private static String codeChars = "1234567890abcdefghijklmnopqrstuvwxyz";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("reqType");
        if (reqType.equals("reg")) {
            registerUser(request, response);
        } else if (reqType.equals("checkName")) {
            checkUsename(request, response);
        } else if (reqType.equals("validate")) {
            validateCode(request, response);
        } else if (reqType.equals("Login")) {
            loginUser(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String valiCode = request.getParameter("validateCode");
        HttpSession session = request.getSession();

        String validation_code = (String) session.getAttribute("validation_code");
        User user = new User(username, password, null, null, null);
        session.setAttribute("user",user);
        boolean result = service.LoginUesrs(user);
        if (result) {
            if (valiCode == null || !valiCode.equalsIgnoreCase(validation_code)) {
                //response.getWriter().print("<script>window.history.back()</script>");
                request.setAttribute("info", "验证码错误,请重新登录");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            response.sendRedirect("goodsvl?reqType=Main");
            request.setAttribute("username", username);
        } else {
            request.setAttribute("info", "登录失败,请重新登录");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

    }

    private static Color getRandomColor(int minColor, int maxColor) {
        Random random = new Random();
        if (minColor > 255)
            minColor = 255;
        if (maxColor > 255)
            maxColor = 255;
        int red = minColor + random.nextInt(maxColor - minColor);
        int green = minColor + random.nextInt(maxColor - minColor);
        int blue = minColor + random.nextInt(maxColor - minColor);
        return new Color(red, green, blue);
    }

    private void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得验证码集合的长度
        int charsLength = codeChars.length();
        //下面3条是关闭客户端浏览器的缓冲区
        response.setHeader("ragma", "No-cache");
        response.setHeader("Cach-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //设置图形验证码的长宽
        int width = 90, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();//获得输出文字的graphics对象
        Random random = new Random();
        g.setColor(getRandomColor(180, 250));//背景颜色
        g.fillRect(0, 0, width, height);
        //设置初始字体
        g.setFont(new Font("Times New Roman", Font.ITALIC, height));
        g.setColor(getRandomColor(120, 180));//字体颜色
        StringBuilder validationCode = new StringBuilder();
        //验证码的随机字体
        String[] fontNames = {"Times New Roman", "Book antiqua", "Arial"};
        //随机生成3-5个验证码
        for (int i = 0; i < 4; i++) {
            //随机设置当前验证码的字符的字体
            g.setFont(new Font(fontNames[random.nextInt(3)], Font.ITALIC, height));
            //随机获得当前验证码的字符
            char codeChar = codeChars.charAt(random.nextInt(charsLength));
            validationCode.append(codeChar);
            //随机设置当前验证码字符的颜色
            g.setColor(getRandomColor(10, 100));
            //在图形上输出验证码字符，x y随机生成
            g.drawString(String.valueOf(codeChar), 16 * i + random.nextInt(7), height - random.nextInt(6));
        }
        //获得session对象
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(5 * 60);
        //将验证码保存在session对象中，key为validation_code
        session.setAttribute("validation_code", validationCode.toString());
        g.dispose();
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "JPEG", os);//以JPEG格式向客户端发送图形验证码


    }

    private void checkUsename(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        boolean result = service.Verifyname(username);
        response.getWriter().print(result);
        response.getWriter().flush();

    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");
        String qqcode = request.getParameter("qqcode");
        if (username != "" && password != "" && confirm_password != "") {
            User user = new User(username, password, null, email, qqcode);
            boolean result = service.regUser(user);
            if (result) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("login.jsp");
            }else {
                request.setAttribute("info", "注册失败");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("info", "注册失败");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }


    }
}
