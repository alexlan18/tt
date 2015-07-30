package com.wing.framework.common;

/**
 * @author: panwb
 *
 * Date: 14-2-18
 * Time: 上午10:31
 */
public class Row {

    private String id;

    private Object[] cell;

    public Object[] getCell() {
        return cell;
    }

    public void setCell(Object[] cell) {
        this.cell = cell;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
