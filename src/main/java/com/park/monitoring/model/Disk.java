package com.park.monitoring.model;

import java.time.LocalDateTime;

public class Disk {
    Integer diskId;
    String diskName;
    Integer serverDiskFk;
    LocalDateTime createdDate;

    public Disk() {
    }

    public Disk(Builder builder) {
        this.diskId = builder.diskId;
        this.diskName = builder.diskName;
        this.serverDiskFk = builder.serverDiskFk;
        this.createdDate = builder.createdDate;
    }

    public static class Builder {
        private Integer diskId;
        private String diskName;
        private Integer serverDiskFk;

        private LocalDateTime createdDate;

        public Builder diskId(Integer diskId) {
            this.diskId = diskId;
            return this;
        }

        public Builder diskName(String diskName) {
            this.diskName = diskName;
            return this;
        }

        public Builder serverDiskFk(Integer serverDiskFk) {
            this.serverDiskFk = serverDiskFk;
            return this;
        }

        public Disk build() {
            return new Disk(this);
        }
    }

    public Integer getDiskId() {
        return diskId;
    }

    public void setDiskId(int disk_id) {
        this.diskId = disk_id;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String disk_name) {
        this.diskName = disk_name;
    }

    public Integer getDiskServerInfoFk() {
        return serverDiskFk;
    }

    public void setDiskServerInfoFk(int serverDiskFk) {
        this.serverDiskFk = serverDiskFk;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "diskId=" + diskId +
                ", diskName='" + diskName + '\'' +
                ", diskServerInfoFk=" + serverDiskFk +
                ", createdDate=" + createdDate +
                '}';
    }
}

