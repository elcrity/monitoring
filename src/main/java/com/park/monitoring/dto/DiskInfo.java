package com.park.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(hidden = true)
public class DiskInfo {
    @Schema(description = "서버의 디스크 총 용량(MB)", example = "[512312, 256773]")
    private List<Double> diskTotalData;
    @Schema(description = "서버의 디스크 총 사용량(%)", example = "[20.2, 15.3]")

    private List<Double> diskUsageData;
    @Schema(description = "서버의 디스크 이름", example = "[\"C/\", \"D/\"]")
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
