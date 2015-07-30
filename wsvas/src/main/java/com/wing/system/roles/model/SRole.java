package com.wing.system.roles.model;


import com.wing.framework.annotation.CodeType;
import com.wing.framework.common.constant.SysContant;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SRole {

    //主键
    private Integer id;
    //角色ID
    private String roleId;
    //角色名称
    private String roleName;
    //是否可用
    @CodeType(id = SysContant.CODE_ENABLED)
    private String enabled;
    //是否超级权限
    @CodeType(id = SysContant.CODE_IS_SYS)
    private String isSys;

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
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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