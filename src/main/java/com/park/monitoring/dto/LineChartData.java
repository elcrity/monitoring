package com.park.monitoring.dto;

public class LineChartData {

    private int year;
    private int count;

    public LineChartData(int year, int count) {
        this.year = year;
        this.count = count;
    }

    public int getYear() {
        return year;
    }

    public int getCount() {
        return count;
    }
}
