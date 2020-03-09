package cn.eight.purcharseforward.dao;

import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.util.Dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

/**
 * @author wu
 * @create 2020-02-29
 */
public class UserDao {
    private BasicDao dao=new BasicDao();
    public boolean querUserName(String username){
        boolean result=false;
        PreparedStatement pst =null;
        ResultSet rs =null;
        Connection con=Dbpool.getConnection();
        String sql="select count(*) from user where username=? and rule=1";
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, username);
            while (rs!=null&&rs.next()&&rs.getInt(1)==1) {
                result=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }
        return result;
    }


    public boolean regUser(User user){
        boolean result=false;
        PreparedStatement pst =null;
        Connection con = Dbpool.getConnection();
        String sql="insert user(username,password,rule,email,qq) value(?,?,1,?,?)";
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            dao.execUpdate(con,pst,user.getUsername(),user.getPassword(),user.getEmail(),user.getQq());
            con.commit();
            result=true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            dao.releaseResurse(null,pst,con);
        }
        return result;

    }


    public boolean queryUsers(User user){
        boolean result=false;
        PreparedStatement pst =null;
        ResultSet rs =null;
        Connection con=Dbpool.getConnection();
        String sql="select count(*) from user where username=? and password=? and rule=1";
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, user.getUsername(),user.getPassword());
            if (rs!=null&&rs.next()&&rs.getInt(1)==1) {
                result=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dao.releaseResurse(rs,pst,con);
        }
        return result;
    }

}
