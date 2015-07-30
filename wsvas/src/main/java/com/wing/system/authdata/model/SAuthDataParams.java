package com.wing.system.authdata.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SAuthDataParams {

    //主键
    private Integer id;
    //权限ID
    private String authId;
    //权限名称
    private String authName;
    //是否可用
    private String enabled;
    //是否超级权限
    private String isSys;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAuthId() {
        return this.authId;
    }
    public void setAuthId(String authId) {
        this.authId = authId;
    }
    public String getAuthName() {
        return this.authName;
    }
    public void setAuthName(String authName) {
        this.authName = authName;
    }
    public String getEnabled() {
        return this.enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getIsSys() {
        return this.isSys;
    }
    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }
}