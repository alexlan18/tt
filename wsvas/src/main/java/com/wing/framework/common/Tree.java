package com.wing.framework.common;

import java.util.List;
import java.util.Map;

/**
 *
 * 封装easyui树形对象
 *
 * @author: panwb
 *
 * Date: 13-3-25
 * Time: 上午10:27
 */
public class Tree {

    private String id;

    private String text;

    private String state;

    private String checked;

    private Map<Object, Object> attributes;

    private List<Tree> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Object, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }
}
