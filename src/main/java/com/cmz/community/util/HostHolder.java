package com.cmz.community.util;

import com.cmz.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用于持有用户信息，代替session对象
 */
@Component
public class HostHolder {
    //以线程为map的key 存取值（get、set）->ThreadLocalMap
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
