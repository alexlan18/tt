package com.wing.system.authresources.model;

/**
 *
 * 授权资源视图
 *
 * @author: panwb
 *
 * Date: 14-3-24
 * Time: 上午11:20
 */
public class AuthResourceView {

    //主键
    private Integer id;
    //资源ID
    private String resourceId;
    //资源名称
    private String resourceName;
    //资源类型
    private String resourceType;
    //资源链接
    private String resourceUrl;
    //是否已授权
    private Boolean authFlag;

    public Boolean getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(Boolean authFlag) {
        this.authFlag = authFlag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
