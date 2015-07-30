package com.wing.framework.common.chart;

/**
 *
 * 多图表（多条线、多个柱状）
 *
 * @author: panwb
 *
 * Date: 2014/7/22
 * Time: 16:07
 */
public class MutilChart {

    private Chart chart;

    private Categorys categories;

    private Dataset[] dataset;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Dataset[] getDataset() {
        return dataset;
    }

    public void setDataset(Dataset[] dataset) {
        this.dataset = dataset;
    }

    public Categorys getCategories() {
        return categories;
    }

    public void setCategories(Categorys categories) {
        this.categories = categories;
    }
}
