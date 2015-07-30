package com.wing.system.users.model;

/**
 *
 * 密码修改实体对象
 *
 * @author: panwb
 *
 * Date: 2014/6/23
 * Time: 15:39
 */
public class ModifyPassword {

    private String password;

    private String password1;

    private String password2;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
