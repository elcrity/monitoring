package com.park.monitoring.model;

import java.time.LocalDateTime;

public class DiskLog {
    private Long diskLogId;
    private Double diskUsage;
    private LocalDateTime createdDate;
    private Long diskDiskLogFk;

    public DiskLog() {}

    public DiskLog(Builder builder){
        this.diskLogId = builder.diskLogId;
        this.diskUsage = builder.diskUsage;
        this.createdDate = builder.createdDate;
        this.diskDiskLogFk = builder.diskDiskLogFk;
    }

    public static class Builder{
        private Long diskLogId;
        private Double diskUsage;
        private LocalDateTime createdDate;
        private Long diskDiskLogFk;

        public Builder diskLogId(Long diskLogId) {
            this.diskLogId = diskLogId;
            return this;
        }
        public Builder diskUsage(Double diskUsage) {
            this.diskUsage = diskUsage;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }
        public Builder diskDiskLogFk(Long diskDiskLogFk) {
            this.diskDiskLogFk = diskDiskLogFk;
            return this;
        }
        public DiskLog build() {
            return new DiskLog(this);
        }
    }

    @Override
    public String toString() {
        return "DiskLog{" +
                "diskLogId=" + diskLogId +
                ", diskUsage=" + diskUsage +
                ", diskDiskLogFk=" + diskDiskLogFk +
                '}';
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getDiskLogId() {
        return diskLogId;
    }

    public void setDiskLogId(Long diskLogId) {
        this.diskLogId = diskLogId;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Long getDiskDiskLogFk() {
        return diskDiskLogFk;
    }

    public void setDiskDiskLogFk(Long diskDiskLogFk) {
        this.diskDiskLogFk = diskDiskLogFk;
    }
}
