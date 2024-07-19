package com.park.monitoring.dto;

public class LineChartData {

    private String year;
    private int count;

    public LineChartData(String year, int count) {
        this.year = year;
        this.count = count;
    }

    public String getYear() {
        return year;
    }

    public int getCount() {
        return count;
    }
}
