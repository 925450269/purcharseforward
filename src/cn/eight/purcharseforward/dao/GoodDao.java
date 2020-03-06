package cn.eight.purcharseforward.dao;

import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.util.Dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 * @create 2020-03-01
 */
public class GoodDao {
    private BasicDao dao = new BasicDao();
    public boolean insertGood(Good good) {
        boolean result = false;
        PreparedStatement pst = null;
        String sql = "insert into good(goodname,goodtype,price,pic) values(?,?,?,?)";
        Connection con = Dbpool.getConnection();
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            dao.execUpdate(con, pst, good.getGoodname(), good.getGoodtype(), good.getPrice(), good.getPic());
            result = true;
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            dao.releaseResurse(null, pst, con);
        }
        return result;
    }



    public boolean UpdateGood(Good good) {
        boolean result = false;
        PreparedStatement pst = null;
        String sql = "update good set goodname=?,goodtype=?,price=? where id=?";
        Connection con = Dbpool.getConnection();
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            dao.execUpdate(con, pst, good.getGoodname(), good.getGoodtype(), good.getPrice(),good.getId());
            con.commit();
            result = true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            dao.releaseResurse(null, pst, con);
        }
        return result;
    }



    public boolean deleteGood(int id) {
        boolean result = false;
        PreparedStatement pst = null;
        String sql = "delete from good where id=?";
        Connection con = Dbpool.getConnection();
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            dao.execUpdate(con,pst,id);
            con.commit();
            result = true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            dao.releaseResurse(null, pst, con);
        }
        return result;
    }
    public List<Good> QueryGoodCritria(Good good){
        PreparedStatement pst =null;
        ResultSet rs =null;
                String sql="select id,goodname,goodtype,price from good";
        String Critria="";
        if (good.getId()!=-1){
            Critria="id="+good.getId();
        }
        if (!good.getGoodname().trim().isEmpty()){
            if (Critria.isEmpty()){
            Critria+="goodname like'%"+good.getGoodname()+"%'";
            }else {
                Critria+=" and goodname like'%"+good.getGoodname()+"%'";
            }
        }
        if (!good.getGoodtype().trim().isEmpty()){
            if (Critria.isEmpty()){
                Critria+="goodtype like'%"+good.getGoodtype()+"%'";
            }else {
                Critria+=" and goodtype like'%"+good.getGoodtype()+"%'";
            }
        }
        if (!Critria.isEmpty()){
            sql+=" where "+Critria;
        }
        //System.out.println(sql);
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, null);
            List<Good> goodList=new ArrayList<>();
            while (rs!=null&&rs.next()) {
                Good goodBean=new Good();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                goodList.add(goodBean);
            }
            return goodList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }

        return null;
    }
    /*public List<Good> QueryGoodCritria(Good good,int pageNow, int pageSize){
        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="select id,goodname,goodtype,price from good";
        String Critria="";
        if (good.getId()!=-1){
            Critria="id="+good.getId();
        }
        if (!good.getGoodname().trim().isEmpty()){
            if (Critria.isEmpty()){
                Critria+="goodname like'%"+good.getGoodname()+"%'";
            }else {
                Critria+=" and goodname like'%"+good.getGoodname()+"%'";
            }
        }
        if (!good.getGoodtype().trim().isEmpty()){
            if (Critria.isEmpty()){
                Critria+="goodtype like'%"+good.getGoodtype()+"%'";
            }else {
                Critria+=" and goodtype like'%"+good.getGoodtype()+"%'";
            }
        }
        if (!Critria.isEmpty()){
            sql+=" where "+Critria+" LIMIT ?,?";
        }
        //System.out.println(sql);
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, (pageNow-1)*pageSize,pageSize);
            List<Good> goodList=new ArrayList<>();
            while (rs!=null&&rs.next()) {
                Good goodBean=new Good();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                goodList.add(goodBean);
            }
            return goodList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }

        return null;
    }*/
    public List<Good> QueryGoodAll(int pageNow,int pageSize){
        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="SELECT * FROM good ORDER BY id LIMIT ?,?;";
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, (pageNow-1)*pageSize,pageSize);
            List<Good> goodList=new ArrayList<>();
            while (rs!=null&&rs.next()) {
                Good goodBean=new Good();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                goodList.add(goodBean);
            }
            return goodList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }
        return null;
    }

    public int QueryTotalRecord(){
        int result=0;
        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="SELECT count(*) FROM good ;";
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, null);
            if (rs!=null&&rs.next()){
            result=rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }
        return result;
    }


}
