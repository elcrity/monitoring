package com.park.monitoring.dto;

import java.util.List;

public class BarChartData {
    public List<String> labels;
    public List<Integer> data;
    public List<String> backgroundColor;
    public List<String> borderColor;

    public BarChartData(List<String> labels, List<Integer> data, List<String> backgroundColor, List<String> borderColor) {
        this.labels = labels;
        this.data = data;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }
}
