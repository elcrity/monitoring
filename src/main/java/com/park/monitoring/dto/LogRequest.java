package com.park.monitoring.dto;


public class LogRequest {

    private LogInput logInput;

    private DiskInfo diskInfo;

    // Getters and Setters

    public LogInput getLogInput() {
        return logInput;
    }

    public void setLogInput(LogInput logInput) {
        this.logInput = logInput;
    }

    public DiskInfo getDiskInfo() {
        return diskInfo;
    }

    public void setDiskInfo(DiskInfo diskInfo) {
        this.diskInfo = diskInfo;
    }
}
