package com.park.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LogRequest {

    @Schema(description = "Log Input Details")
    private LogInput logInput;

    @Schema(description = "Disk Information")
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
