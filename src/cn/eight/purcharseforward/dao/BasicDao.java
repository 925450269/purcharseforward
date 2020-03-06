package cn.eight.purcharseforward.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BasicDao {
    public ResultSet execQuery(Connection con, PreparedStatement pst,Object...param){
        //针对占位符传参；
        ResultSet rs =null;
        try {
            if (param!=null){
                for (int i = 0; i <param.length; i++) {
                    pst.setObject(i+1,param[i]);
                }
            }
            rs = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //修改功能；
    public void execUpdate(Connection con, PreparedStatement pst,Object...param) throws SQLException {

        if (param!=null) {
            for (int i = 0; i < param.length; i++) {
                pst.setObject(i + 1, param[i]);
            }
        }
            pst.executeUpdate();


    }
    //释放资源方法
    public void  releaseResurse(ResultSet rs,PreparedStatement pst,Connection con) {
        try {
            if (rs!=null) {
                rs.close();
            }
            if (pst!=null) {
                pst.close();
            }
            if (con!=null) {
                con.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

   /* public static void main(String[] args) {
        Connection con = Dbpool.getConnection();
        System.out.println(con);
        String sql="insert user(username,password,rule,email,qq) value(?,?,?,?,?)";
        BasicDao basicDao=new BasicDao();
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            basicDao.execUpdate(con,pst,"admin","1234567",0,null,null);
            basicDao.releaseResurse(null,pst,con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
