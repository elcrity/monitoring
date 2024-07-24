package com.park.monitoring.model;

import java.time.LocalDateTime;

public class MetricLog {
    Long logId;
    Double cpuUsage;
    Double memoryUsage;
    Long serverMetricFk;
    LocalDateTime createdDate;
    public MetricLog() {}

    public MetricLog(Builder builder) {
        this.logId = builder.logId;
        this.cpuUsage = builder.cpuUsage;
        this.memoryUsage = builder.memoryUsage;
        this.serverMetricFk = builder.serverMetricFk;
        this.createdDate = builder.createdDate;
    }

    public static class Builder{
        private Long logId;
        private Double cpuUsage;
        private Double memoryUsage;
        private Long serverMetricFk;
        private LocalDateTime createdDate;

        public Builder logId(Long logId) {
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
        public Builder serverMetricFk(Long serverMetricFk) {
            this.serverMetricFk = serverMetricFk;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public MetricLog build() {
            return new MetricLog(this);
        }
    }
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
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

    public Long getServerMetricFk() {
        return serverMetricFk;
    }

    public void setServerMetricFk(Long serverMetricFk) {
        this.serverMetricFk = serverMetricFk;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "MetricLog{" +
                "logId=" + logId +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", serverMetricFk=" + serverMetricFk +
                ", createdDate=" + createdDate +
                '}';
    }
}
