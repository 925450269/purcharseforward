package cn.eight.purcharseforward.service.impl;


import cn.eight.purcharseforward.dao.UserDao;
import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.service.UserService;
import cn.eight.purcharseforward.util.Tools;

/**
 * @author wu
 * @create 2020-02-28
 */
public class UserServiceImp implements UserService {
    private UserDao userDao=new UserDao();

    @Override
    public boolean regUser(User user) {
        user.setPassword(Tools.md5(user.getPassword()));
        return userDao.regUser(user);
    }

    @Override
    public boolean Verifyname(String username) {
        return userDao.querUserName(username );
    }

    @Override
    public boolean LoginUesrs(User user) {
        user.setPassword(Tools.md5(user.getPassword()));
        return userDao.queryUsers(user);
    }
}
