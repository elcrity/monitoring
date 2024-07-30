package com.park.monitoring.model;

import java.time.LocalDateTime;
import java.util.Optional;

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
    Long diskTotal1;
    Long diskTotal2;
    Long diskTotal3;
    Long diskTotal4;

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
        private Long diskTotal1;
        private Long diskTotal2;
        private Long diskTotal3;
        private Long diskTotal4;

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

        public Builder diskTotal1(Long diskTotal1) {
            this.diskTotal1 = diskTotal1;
            return this;
        }

        public Builder diskTotal2(Long diskTotal2) {
            this.diskTotal2 = diskTotal2;
            return this;
        }

        public Builder diskTotal3(Long diskTotal3) {
            this.diskTotal3 = diskTotal3;
            return this;
        }

        public Builder diskTotal4(Long diskTotal4) {
            this.diskTotal4 = diskTotal4;
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

    public Long getDiskTotal1() {
        return diskTotal1;
    }

    public void setDiskTotal1(Long diskTotal1) {
        this.diskTotal1 = diskTotal1;
    }

    public Long getDiskTotal2() {
        return diskTotal2;
    }

    public void setDiskTotal2(Long diskTotal2) {
        this.diskTotal2 = diskTotal2;
    }

    public Long getDiskTotal3() {
        return diskTotal3;
    }

    public void setDiskTotal3(Long diskTotal3) {
        this.diskTotal3 = diskTotal3;
    }

    public Long getDiskTotal4() {
        return diskTotal4;
    }

    public void setDiskTotal4(Long diskTotal4) {
        this.diskTotal4 = diskTotal4;
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
