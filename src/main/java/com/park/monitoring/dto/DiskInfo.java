package com.park.monitoring.dto;


import java.util.List;

public class DiskInfo {
    private List<Double> diskTotalData;

    private List<Double> diskUsageData;
    private List<String> diskName;

    public DiskInfo(List<Double> diskTotalData, List<Double> diskUsageData, List<String> diskName) {
        this.diskTotalData = diskTotalData;
        this.diskUsageData = diskUsageData;
        this.diskName = diskName;
    }

    public List<Double> getDiskTotalData() {
        return diskTotalData;
    }

    public List<Double> getDiskUsageData() {
        return diskUsageData;
    }

    public List<String> getDiskName() {
        return diskName;
    }
}
