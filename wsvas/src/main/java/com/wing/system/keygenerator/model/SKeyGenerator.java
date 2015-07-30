package com.wing.system.keygenerator.model;

import com.wing.framework.converter.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
public class SKeyGenerator {

    //主键
    private Integer id;
    //前缀
    private String prefix;
    //自增数
    private Integer num;
    //后缀
    private String suffix;
    //长度
    private Integer length;
    //主键日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date keyDate;
    //类型
    private String type;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public Integer getNum() {
        return this.num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getSuffix() {
        return this.suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public Integer getLength() {
        return this.length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }
    public Date getKeyDate() {
        return this.keyDate;
    }
    public void setKeyDate(Date keyDate) {
        this.keyDate = keyDate;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}