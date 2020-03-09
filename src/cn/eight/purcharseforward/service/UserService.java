package cn.eight.purcharseforward.service;


import cn.eight.purcharseforward.pojo.User;

/**
 * @author wu
 * @create 2020-02-28
 */
public interface UserService {
    boolean regUser(User user);
    boolean Verifyname(String username);
    boolean LoginUesrs(User user);

}
