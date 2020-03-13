package cn.eight.purcharseforward.dao;

import cn.eight.purcharseforward.pojo.CarBean;
import cn.eight.purcharseforward.pojo.Good;
import cn.eight.purcharseforward.util.Dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @create 2020-03-01
 */
public class GoodDao {
    private BasicDao dao = new BasicDao();

    public List<String> QueryGoodType(){
        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="SELECT DISTINCT goodtype FROM good order by goodtype";
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, null);
            List<String> goodTypeList=new ArrayList<>();
            while (rs!=null&&rs.next()) {
                goodTypeList.add(rs.getString(1));
            }
            return goodTypeList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }
        return null;
    }



    public List<Good> QueryGoodsByType(String goodType){
        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="SELECT * from good where goodtype=? order by id LIMIT ?,?";
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, goodType,0,20);
            List<Good> goodList=new ArrayList<>();
            while (rs!=null&&rs.next()) {
                Good goodBean = new Good();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                goodBean.setPic(rs.getString(5));
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


    public List<Good> QueryCarBean(CarBean carBean){
        List<Good> goodList = new ArrayList<>();
        Map<Integer, Integer> car = carBean.getCar();
        StringBuilder ids=new StringBuilder();
        for (Map.Entry<Integer,Integer> entry:car.entrySet()){
            Integer id = entry.getKey();
            ids.append(id.toString()).append(",");
        }
        String idStr=ids.toString();
        if (!idStr.isEmpty()) {
            idStr=idStr.substring(0,idStr.length()-1);
        }else {
            return goodList;
        }


        PreparedStatement pst =null;
        ResultSet rs =null;
        String sql="SELECT * from good where  id in ("+idStr+")";
        Connection con = Dbpool.getConnection();
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, null);

            while (rs!=null&&rs.next()) {
                Good goodBean = new Good();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setPrice(rs.getDouble(4));
                //得到商品的数量;
                goodBean.setCount(car.get(rs.getInt(1)));
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

}
