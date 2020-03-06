package cn.eight.purcharseforward.dao;

import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.util.Dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wu
 * @create 2020-02-29
 */
public class UserDao {
    private BasicDao dao=new BasicDao();
    public boolean queruUser(User user){
        boolean result=false;
        PreparedStatement pst =null;
        ResultSet rs =null;
        Connection con=Dbpool.getConnection();
        String sql="select count(*) from user where username=? and password=? and rule=0";
        try {
            pst = con.prepareStatement(sql);
            rs = dao.execQuery(con, pst, user.getUsername(), user.getPassword());
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
