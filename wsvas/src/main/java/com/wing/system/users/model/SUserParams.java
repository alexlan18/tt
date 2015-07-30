package com.wing.system.users.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SUserParams {

    //主键
    private Integer id;
    //登录名称
    private String loginName;
    //密码
    private String password;
    //用户名称
    private String userName;
    //是否可用
    private String enabled;
    //部门
    private String department;
    //邮件
    private String email;
    //手机
    private String telphone;
    //归属地区
    private String areaCode;
    //是否超级用户
    private String isSys;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLoginName() {
        return this.loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEnabled() {
        return this.enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getDepartment() {
        return this.department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelphone() {
        return this.telphone;
    }
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
    public String getAreaCode() {
        return this.areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getIsSys() {
        return this.isSys;
    }
    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }
}