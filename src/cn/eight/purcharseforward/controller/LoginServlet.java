package cn.eight.purcharseforward.controller;


import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.service.impl.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author wu
 * @create 2020-02-28
 */
@WebServlet("/houtai/loginservlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String savename = request.getParameter("savename");
        //System.out.println(username+password);
        User user=new User(username,password,null,null,null);
        UserServiceImp service = new UserServiceImp();
        boolean result = service.checkUser(user);
        if (result) {
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            Cookie cookiename = new Cookie("username",username);
            cookiename.setPath(request.getContextPath());
            cookiename.setMaxAge(60*60*24);
            response.addCookie(cookiename);
            if (savename!=null) {
                Cookie cookiepass = new Cookie("password",password);
                cookiename.setPath(request.getContextPath());
                cookiename.setMaxAge(60*60*24);
                response.addCookie(cookiepass);
            }

            response.sendRedirect("main.html");
        }else {
            request.setAttribute("info","登录失败");
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
