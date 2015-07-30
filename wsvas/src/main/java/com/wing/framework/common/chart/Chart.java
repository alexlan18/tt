package com.wing.framework.common.chart;

/**
 *
 * 图表头信息（标题、X轴标题等等）
 *
 * @author: panwb
 *
 * Date: 2014/7/22
 * Time: 14:28
 */
public class Chart {

    //图表名称
    private String caption;

    //X轴名称
    private String yaxisname;

    //数值前缀
    private String numberprefix;

    //最大数值
    private String yaxismaxvalue;

    //是否显示边框
    private String showborder;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getYaxisname() {
        return yaxisname;
    }

    public void setYaxisname(String yaxisname) {
        this.yaxisname = yaxisname;
    }

    public String getNumberprefix() {
        return numberprefix;
    }

    public void setNumberprefix(String numberprefix) {
        this.numberprefix = numberprefix;
    }

    public String getYaxismaxvalue() {
        return yaxismaxvalue;
    }

    public void setYaxismaxvalue(String yaxismaxvalue) {
        this.yaxismaxvalue = yaxismaxvalue;
    }

    public String getShowborder() {
        return showborder;
    }

    public void setShowborder(String showborder) {
        this.showborder = showborder;
    }
}
