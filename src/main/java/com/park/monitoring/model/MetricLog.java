package com.park.monitoring.model;

import java.time.LocalDateTime;

public class MetricLog {
    Integer logId;
    Double cpuUsage;
    Double memoryUsage;
    Integer serverMetricFk;
    LocalDateTime createdDate;
    Double diskUsage1;
    Double diskUsage2;
    Double diskUsage3;
    Double diskUsage4;
    Double diskTotal1;
    Double diskTotal2;
    Double diskTotal3;
    Double diskTotal4;
    String diskName1;
    String diskName2;
    String diskName3;
    String diskName4;

    public MetricLog() {}

    public MetricLog(Builder builder) {
        this.logId = builder.logId;
        this.cpuUsage = builder.cpuUsage;
        this.memoryUsage = builder.memoryUsage;
        this.serverMetricFk = builder.serverMetricFk;
        this.createdDate = builder.createdDate;
        this.diskUsage1 = builder.diskUsage1;
        this.diskUsage2 = builder.diskUsage2;
        this.diskUsage3 = builder.diskUsage3;
        this.diskUsage4 = builder.diskUsage4;
        this.diskTotal1 = builder.diskTotal1;
        this.diskTotal2 = builder.diskTotal2;
        this.diskTotal3 = builder.diskTotal3;
        this.diskTotal4 = builder.diskTotal4;
        this.diskName1 = builder.diskName1;
        this.diskName2 = builder.diskName2;
        this.diskName3 = builder.diskName3;
        this.diskName4 = builder.diskName4;
    }

    public static class Builder {
        private Integer logId;
        private Double cpuUsage;
        private Double memoryUsage;
        private Integer serverMetricFk;
        private LocalDateTime createdDate;
        private Double diskUsage1;
        private Double diskUsage2;
        private Double diskUsage3;
        private Double diskUsage4;
        private Double diskTotal1;
        private Double diskTotal2;
        private Double diskTotal3;
        private Double diskTotal4;
        private String diskName1;
        private String diskName2;
        private String diskName3;
        private String diskName4;

        public Builder logId(Integer logId) {
            this.logId = logId;
            return this;
        }

        public Builder cpuUsage(Double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }

        public Builder memoryUsage(Double memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }

        public Builder serverMetricFk(Integer serverMetricFk) {
            this.serverMetricFk = serverMetricFk;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder diskUsage1(Double diskUsage1) {
            this.diskUsage1 = diskUsage1;
            return this;
        }

        public Builder diskUsage2(Double diskUsage2) {
            this.diskUsage2 = diskUsage2;
            return this;
        }

        public Builder diskUsage3(Double diskUsage3) {
            this.diskUsage3 = diskUsage3;
            return this;
        }

        public Builder diskUsage4(Double diskUsage4) {
            this.diskUsage4 = diskUsage4;
            return this;
        }

        public Builder diskTotal1(Double diskTotal1) {
            this.diskTotal1 = diskTotal1;
            return this;
        }

        public Builder diskTotal2(Double diskTotal2) {
            this.diskTotal2 = diskTotal2;
            return this;
        }

        public Builder diskTotal3(Double diskTotal3) {
            this.diskTotal3 = diskTotal3;
            return this;
        }

        public Builder diskTotal4(Double diskTotal4) {
            this.diskTotal4 = diskTotal4;
            return this;
        }

        public Builder diskName1(String diskName1) {
            this.diskName1 = diskName1;
            return this;
        }

        public Builder diskName2(String diskName2) {
            this.diskName2 = diskName2;
            return this;
        }

        public Builder diskName3(String diskName3) {
            this.diskName3 = diskName3;
            return this;
        }

        public Builder diskName4(String diskName4) {
            this.diskName4 = diskName4;
            return this;
        }

        public MetricLog build() {
            return new MetricLog(this);
        }
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Integer getServerId() {
        return serverMetricFk;
    }

    public void setServerId(Integer serverMetricFk) {
        this.serverMetricFk = serverMetricFk;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Double getDiskUsage1() {
        return diskUsage1;
    }

    public void setDiskUsage1(Double diskUsage1) {
        this.diskUsage1 = diskUsage1;
    }

    public Double getDiskUsage2() {
        return diskUsage2;
    }

    public void setDiskUsage2(Double diskUsage2) {
        this.diskUsage2 = diskUsage2;
    }

    public Double getDiskUsage3() {
        return diskUsage3;
    }

    public void setDiskUsage3(Double diskUsage3) {
        this.diskUsage3 = diskUsage3;
    }

    public Double getDiskUsage4() {
        return diskUsage4;
    }

    public void setDiskUsage4(Double diskUsage4) {
        this.diskUsage4 = diskUsage4;
    }

    public Double getDiskTotal1() {
        return diskTotal1;
    }

    public void setDiskTotal1(Double diskTotal1) {
        this.diskTotal1 = diskTotal1;
    }

    public Double getDiskTotal2() {
        return diskTotal2;
    }

    public void setDiskTotal2(Double diskTotal2) {
        this.diskTotal2 = diskTotal2;
    }

    public Double getDiskTotal3() {
        return diskTotal3;
    }

    public void setDiskTotal3(Double diskTotal3) {
        this.diskTotal3 = diskTotal3;
    }

    public Double getDiskTotal4() {
        return diskTotal4;
    }

    public void setDiskTotal4(Double diskTotal4) {
        this.diskTotal4 = diskTotal4;
    }

    public String getDiskName1() {
        return diskName1;
    }

    public void setDiskName1(String diskName1) {
        this.diskName1 = diskName1;
    }

    public String getDiskName2() {
        return diskName2;
    }

    public void setDiskName2(String diskName2) {
        this.diskName2 = diskName2;
    }

    public String getDiskName3() {
        return diskName3;
    }

    public void setDiskName3(String diskName3) {
        this.diskName3 = diskName3;
    }

    public String getDiskName4() {
        return diskName4;
    }

    public void setDiskName4(String diskName4) {
        this.diskName4 = diskName4;
    }

    @Override
    public String toString() {
        return "MetricLog{" +
                ", serverMetricFk=" + serverMetricFk +
                ", logId=" + logId +
                ", createdDate=" + createdDate +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage1=" + diskUsage1 +
                ", diskUsage2=" + diskUsage2 +
                ", diskUsage3=" + diskUsage3 +
                ", diskUsage4=" + diskUsage4 +
                ", diskTotal1=" + diskTotal1 +
                ", diskTotal2=" + diskTotal2 +
                ", diskTotal3=" + diskTotal3 +
                ", diskTotal4=" + diskTotal4 +
                '}';
    }
}
