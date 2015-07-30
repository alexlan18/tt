package com.wing.system.codeinfo.model;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SCodeInfo {

    //主键
    private Integer id;
    //代码类型ID
    private String codeTypeId;
    //代码值
    private String codeValue;
    //代码名称
    private String codeName;
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
    public String getCodeValue() {
        return this.codeValue;
    }
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
    public String getCodeName() {
        return this.codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
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