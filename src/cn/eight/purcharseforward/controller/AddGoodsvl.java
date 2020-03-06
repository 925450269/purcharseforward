package cn.eight.purcharseforward.controller;

import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.service.impl.GoodserviceImp;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author wu
 * @create 2020-03-01
 */
@WebServlet("/houtai/addgoodsvl")
public class AddGoodsvl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");    //设置编码
        //创建 FileItem 对象的工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //获取文件需要上传到的路径
        String path = getServletContext().getRealPath("/WEB-INF/upload");
        //指定临时文件目录
        factory.setRepository(new File(path));
        //设置内存缓冲区的大小
        factory.setSizeThreshold(1024 * 1024);
        //负责处理上传的文件数据，并将表单中每个输入项封装成一个 FileItem 对象中
        ServletFileUpload upload = new ServletFileUpload(factory);
        //ProgressListener显示上传进度
        ProgressListener progressListener = new ProgressListener() {
            public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("到现在为止,  " + pBytesRead + " 字节已上传，总大小为 " + pContentLength);
            }
        };
        upload.setProgressListener(progressListener);

        List<FileItem> list;
        try {
            //调用Upload.parseRequest方法解析request对象，得到一个保存了所有上传内容的List对象。
            list = (List<FileItem>) upload.parseRequest(request);
            //对list进行迭代，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件
            Good good = new Good();
            for (FileItem item : list) {
                String name = item.getFieldName();
                if (item.isFormField()) {//为普通表单字段
                    String value = new String(item.getString().getBytes("iso-8859-1"), "utf-8");
                    if (name.equals("pname")) {
                        good.setGoodname(value);
                    } else if (name.equals("ptype")) {
                        good.setGoodtype(value);
                    } else if (name.equals("pprice")) {
                        good.setPrice(Double.valueOf(value));
                    }


                } else {//为上传文件，则调用item.write方法写文件
                    String value = item.getName();
                    int start = value.lastIndexOf("\\");
                    String filename = value.substring(start + 1);
                    good.setPic(filename);
                    //把文件数据写入磁盘
                    item.write(new File(path, filename));

                }
            }
            GoodserviceImp goodservice = new GoodserviceImp();
            boolean result = goodservice.addgood(good);
            if (result) {
                request.getRequestDispatcher("addnewproduct.jsp").forward(request, response);
            } else {
                request.setAttribute("info", "添加失败");
                request.getRequestDispatcher("addnewproduct.jsp").forward(request, response);
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
