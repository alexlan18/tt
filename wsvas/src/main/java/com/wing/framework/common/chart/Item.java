package com.wing.framework.common.chart;

/**
 *
 * 图表的每项数据
 *
 * @author: panwb
 *
 * Date: 2014/7/22
 * Time: 14:28
 */
public class Item {

    //显示项名称
    private String label;

    //显示项值
    private String value;

    //鼠标飘过，显示内容
    private String tooltext;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTooltext() {
        return tooltext;
    }

    public void setTooltext(String tooltext) {
        this.tooltext = tooltext;
    }
}
