package com.wing.framework.common.chart;

/**
 *
 * 单个图表（单条线、单个柱状）
 *
 * @author: panwb
 *
 * Date: 2014/7/22
 * Time: 16:05
 */
public class SingleChart {

    private Chart chart;

    private Item[] data;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Item[] getData() {
        return data;
    }

    public void setData(Item[] data) {
        this.data = data;
    }
}
