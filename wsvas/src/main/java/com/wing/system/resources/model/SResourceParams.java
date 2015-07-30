package com.wing.system.resources.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SResourceParams {

    //主键
    private Integer id;
    //资源ID
    private String resourceId;
    //资源名称
    private String resourceName;
    //资源类型
    private String resourceType;
    //资源优先权
    private Integer resourcePriority;
    //资源链接
    private String resourceUrl;
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
    public String getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public String getResourceName() {
        return this.resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getResourceType() {
        return this.resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    public Integer getResourcePriority() {
        return this.resourcePriority;
    }
    public void setResourcePriority(Integer resourcePriority) {
        this.resourcePriority = resourcePriority;
    }
    public String getResourceUrl() {
        return this.resourceUrl;
    }
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
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