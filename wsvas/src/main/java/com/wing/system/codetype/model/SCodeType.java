package com.wing.system.codetype.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SCodeType {

    //主键
    private Integer id;
    //代码类型ID
    private String codeTypeId;
    //代码类型名称
    private String codeTypeName;
    //排序号
    private Integer sortNo;
    //备注
    private String remark;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCodeTypeId() {
        return this.codeTypeId;
    }
    public void setCodeTypeId(String codeTypeId) {
        this.codeTypeId = codeTypeId;
    }
    public String getCodeTypeName() {
        return this.codeTypeName;
    }
    public void setCodeTypeName(String codeTypeName) {
        this.codeTypeName = codeTypeName;
    }
    public Integer getSortNo() {
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}