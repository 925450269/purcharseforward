package cn.eight.purcharseforward.service.impl;


import cn.eight.purcharseforward.dao.UserDao;
import cn.eight.purcharseforward.pojo.User;
import cn.eight.purcharseforward.service.UserService;

/**
 * @author wu
 * @create 2020-02-28
 */
public class UserServiceImp implements UserService {
    private UserDao userDao=new UserDao();

    @Override
    public boolean checkUser(User user) {
        return userDao.queruUser(user);
    }
}
