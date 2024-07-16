package com.park.monitoring.dto;

import java.util.List;

public class DoughnutChartData {
    public List<String> labels;
    public List<Integer> data;
    public List<String> backgroundColor;

    public DoughnutChartData(List<String> labels, List<Integer> data, List<String> backgroundColor) {
        this.labels = labels;
        this.data = data;
        this.backgroundColor = backgroundColor;
    }
}
