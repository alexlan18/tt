package com.wing.system.roleauths.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SRoleAuth {

    //主键
    private Integer id;
    //角色ID
    private String roleId;
    //权限ID
    private String authId;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRoleId() {
        return this.roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getAuthId() {
        return this.authId;
    }
    public void setAuthId(String authId) {
        this.authId = authId;
    }
}