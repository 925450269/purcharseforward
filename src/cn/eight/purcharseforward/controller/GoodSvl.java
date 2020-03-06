package cn.eight.purcharseforward.controller;

import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.service.Goodservice;
import cn.eight.purcharseforward.service.impl.GoodserviceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(value = "/houtai/goodsvl", initParams = @WebInitParam(name = "pageSize", value = "20"))
public class GoodSvl extends HttpServlet {
    private Goodservice service = new GoodserviceImp();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("reqType");
        if (reqType.equals("QueryByCritria")) {
            int page = Integer.valueOf(request.getParameter("page"));
            queryByCritria(request, response);
    }
        if (reqType.equals("QueryAll")) {
            queryALL(request, response, -1);
        }
        if (reqType.equals("Modgoods")) {
            modGoods(request, response);
        }
        if (reqType.equals("Delete")) {
            deleteGood(request, response);
        }
    }

    private void deleteGood(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.valueOf(request.getParameter("pid"));
        int pageNow = Integer.valueOf(request.getParameter("pageNow"));
        boolean result = service.removeGood(id);
        if (result) {
            queryALL(request,response,pageNow);
        }else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>window.alert('删除失败');window.history.back()</script>");
            response.getWriter().flush();
        }

    }

    private void modGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.valueOf(request.getParameter("pid"));
        String goodname = request.getParameter("pname");
        String goodtype = request.getParameter("ptype");
        Double goodprice = Double.valueOf(request.getParameter("pprice"));
        int pageNow = Integer.valueOf(request.getParameter("pageNow"));
        Good good = new Good(id, goodname, goodtype, goodprice, null);
        boolean result = service.modifyGood(good);
        //修改完成之后要跳转之修改之前的页面；
        if (result) {
            queryALL(request,response,pageNow);
        }else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>window.alert('修改失败');window.history.back()</script>");
            response.getWriter().flush();
        }


    }

    private void queryALL(HttpServletRequest request, HttpServletResponse response, int update_pageNow) {
        int pageNow = 0;
        if (update_pageNow == -1) {
            pageNow = Integer.valueOf(request.getParameter("pageNow"));
            if (pageNow < 1) {
                pageNow = 1;
            }
        }else {
            pageNow=update_pageNow;
        }
        final int pageSize = Integer.valueOf(getServletConfig().getInitParameter("pageSize"));
        List<Good> goodAllList = service.findGoodAll(pageNow, pageSize);
        //得到总记录数
        int totalRecord = service.findTotalRecord();
        //得到总页数
        int totalPage = 0;
        if (totalRecord % pageSize == 0) {
            totalPage = totalRecord / pageSize;
        } else {
            totalPage = totalRecord / pageSize + 1;
        }
        request.setAttribute("goodAllList", goodAllList);
        request.setAttribute("totalRecord", totalRecord);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("pageNow", pageNow);
        try {
            request.getRequestDispatcher("productListUI.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void queryByCritria(HttpServletRequest request, HttpServletResponse response) {
        String Idstr = request.getParameter("pid").trim();
        int gid;
        if (Idstr.isEmpty()) {
            gid = -1;
        } else {
            gid = Integer.valueOf(Idstr);
        }
        String gname = request.getParameter("pname");
        String gtype = request.getParameter("ptype");
        Good good = new Good(gid, gname, gtype, null, null);
        List<Good> goodList = service.findGoodCritria(good);
        //System.out.println(goodList);
        request.setAttribute("goodList", goodList);
        try {
            request.getRequestDispatcher("productListBYcritria.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
