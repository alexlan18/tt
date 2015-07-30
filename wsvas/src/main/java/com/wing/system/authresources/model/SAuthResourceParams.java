package com.wing.system.authresources.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SAuthResourceParams {

    //主键
    private Integer id;
    //权限ID
    private String authId;
    //资源ID
    private String resourceId;

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
    public String getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}