package com.test.retrofit2.body_for_bean;

import java.io.Serializable;

/**
 * Created by zhouwei on 2018/7/31.
 */

public class User implements Serializable{

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
