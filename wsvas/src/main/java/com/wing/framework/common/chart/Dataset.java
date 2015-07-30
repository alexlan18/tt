package com.wing.framework.common.chart;

/**
 *
 * 数据集
 *
 * @author: panwb
 *
 * Date: 2014/7/22
 * Time: 14:58
 */
public class Dataset {

    //数据项名称
    private String seriesname;

    //是否显示值在图表
    private String showvalues;

    //数据集合
    private Value[] value;

    public String getSeriesname() {
        return seriesname;
    }

    public void setSeriesname(String seriesname) {
        this.seriesname = seriesname;
    }

    public String getShowvalues() {
        return showvalues;
    }

    public void setShowvalues(String showvalues) {
        this.showvalues = showvalues;
    }

    public Value[] getValue() {
        return value;
    }

    public void setValue(Value[] value) {
        this.value = value;
    }
}
