package cn.eight.purcharseforward.controller;

import cn.eight.purcharseforward.pojo.CarBean;
import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.service.Goodservice;
import cn.eight.purcharseforward.service.impl.GoodserviceImp;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


@WebServlet("/qiantai/goodsvl")
public class GoodSvl extends HttpServlet {
    Goodservice service = new GoodserviceImp();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("reqType");
        if (reqType.equals("Main")) {
            mainpage(request, response);
        } else if (reqType.equals("downImg")) {
            downImg(request, response);
        }else if (reqType.equals("addGoodCar")) {
            addGoodCar(request, response);
        }else if (reqType.equals("delGood")) {
            delGoodCar(request, response);
        }else if (reqType.equals("ClearGood")) {
            clearGood(request, response);
        }else if (reqType.equals("Gocar")) {
            openCar(request, response);
        }else if (reqType.equals("paymon")) {
            Paymon(request, response);
        }
    }

    private void Paymon(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        Double gettalmoney = carBean.gettalmoney();
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>window.alert('总计"+gettalmoney+"元')</script>");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void openCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        List<Good> carList = service.findCars(carBean);
        request.setAttribute("carList",carList);
        //得到总额;
        double totalmoney=0;
        for (int i = 0;  i< carList.size(); i++) {
            totalmoney+=carList.get(i).getPrice()*carList.get(i).getCount();
        }
        request.setAttribute("totalmoney",totalmoney);
        request.getRequestDispatcher("flow.jsp").forward(request,response);



    }

    private void modGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] gooids = request.getParameterValues("gooids");
        String[] counts = request.getParameterValues("counts");
        Integer[] gooids_int=new Integer[gooids.length];
        Integer[] counts_int=new Integer[counts.length];

        for (int i = 0; i < gooids.length; i++) {
            gooids_int[i]= Integer.valueOf(gooids[i]);
            counts_int[i]= Integer.valueOf(counts[i]);
        }
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        carBean.modGood(gooids_int,counts_int);
        genericCarData(request,response,carBean,session);
    }

    private void clearGood(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        carBean.clearGood();
        genericCarData(request,response,carBean,session);


    }

    private void delGoodCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer goodid = Integer.valueOf(request.getParameter("goodid"));
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        carBean.removeGood(goodid);
        genericCarData(request,response,carBean,session);
    }

    private void addGoodCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer goodid = Integer.valueOf(request.getParameter("gid"));
        HttpSession session = request.getSession();
        CarBean carBean = (CarBean) session.getAttribute("carBean");
        if (carBean==null) {
            carBean = new CarBean();
        }
        carBean.addGood(goodid);
        //要把car放到session中去
        genericCarData(request,response,carBean,session);
    }
    //把CarBean对象中的表示购物车的map对象转换为能够在页面展现的List<Good>集合对象
    private void genericCarData(HttpServletRequest request, HttpServletResponse response, CarBean carBean, HttpSession session) throws ServletException, IOException {
        session.setAttribute("carBean",carBean);
        List<Good> carList = service.findCars(carBean);
        request.setAttribute("carList",carList);
        //得到总额;
        double totalmoney=0;
        for (int i = 0;  i< carList.size(); i++) {
            totalmoney+=carList.get(i).getPrice()*carList.get(i).getCount();
        }
        request.setAttribute("totalmoney",totalmoney);
        request.getRequestDispatcher("flow.jsp").forward(request,response);

    }


    private void downImg(HttpServletRequest request, HttpServletResponse response) {
        String filename = request.getParameter("filename");
        String path = request.getServletContext().getRealPath("/WEB-INF/upload/"+filename);
        ServletOutputStream os = null;
        FileInputStream fis = null;
        try {
            int len = 0;
            byte[] b = new byte[8*1024];
            fis = new FileInputStream(path);
            os = response.getOutputStream();
            while ((len=fis.read(b))!= -1) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void mainpage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodType = request.getParameter("goodType");
        List<String> allGoodTypes = service.findGoodType();
        List<Good> GoodsList = null;
        if (allGoodTypes.size() > 0) {
            int count=0;
            double talmoney=0;
            if (goodType==null||goodType.isEmpty()) {
                goodType = allGoodTypes.get(0);
                GoodsList = service.findAllGoods(goodType);
            }
            //取得该商品的所有商品；
            GoodsList = service.findAllGoods(goodType);
            HttpSession session = request.getSession();
            CarBean carBean = (CarBean) session.getAttribute("carBean");
            if (carBean != null) {
                count = carBean.gettalCount();
                talmoney=carBean.gettalmoney();
            }
            request.setAttribute("count", count);
            request.setAttribute("talmoney", talmoney);
            request.setAttribute("allGoodTypes", allGoodTypes);
            request.setAttribute("GoodsList", GoodsList);
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

}
