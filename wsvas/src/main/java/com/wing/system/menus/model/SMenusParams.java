package com.wing.system.menus.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SMenusParams {

    //主键
    private Integer id;
    //菜单ID
    private String menuId;
    //菜单名称
    private String menuName;
    //父菜单ID
    private String parentMenuId;
    //菜单地址
    private String menuUrl;
    //图标地址
    private String iconUrl;
    //排序号
    private Integer sortNo;
    //是否可见
    private String isVisible;
    //备注
    private String remarks;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getMenuName() {
        return this.menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getParentMenuId() {
        return this.parentMenuId;
    }
    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }
    public String getMenuUrl() {
        return this.menuUrl;
    }
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
    public String getIconUrl() {
        return this.iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public Integer getSortNo() {
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
    public String getIsVisible() {
        return this.isVisible;
    }
    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}