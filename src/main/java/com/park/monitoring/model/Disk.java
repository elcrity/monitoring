package com.park.monitoring.model;

import java.time.LocalDateTime;

public class Disk {
    Long diskId;
    String diskName;
    Long diskTotal;
    LocalDateTime createdDate;
    Long diskServerInfoFk;

    public Disk() {
    }

    public Disk(Builder builder) {
        this.diskId = builder.diskId;
        this.createdDate = builder.createdDate;
        this.diskTotal = builder.diskTotal;
        this.diskName = builder.diskName;
        this.diskServerInfoFk = builder.diskServerInfoFk;
    }

    public static class Builder {
        private Long diskId;
        private String diskName;
        private Long diskTotal;
        private LocalDateTime createdDate;
        private Long diskServerInfoFk;

        public Builder diskId(Long diskId) {
            this.diskId = diskId;
            return this;
        }
        public Builder diskTotal(Long diskTotal) {
            this.diskTotal = diskTotal;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder diskName(String diskName) {
            this.diskName = diskName;
            return this;
        }

        public Builder diskServerInfoFk(Long diskServerInfoFk) {
            this.diskServerInfoFk = diskServerInfoFk;
            return this;
        }

        public Disk build() {
            return new Disk(this);
        }
    }

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long disk_id) {
        this.diskId = disk_id;
    }

    public Long getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(Long diskTotal) {
        this.diskTotal = diskTotal;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime created_date) {
        this.createdDate = created_date;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String disk_name) {
        this.diskName = disk_name;
    }

    public Long getDiskServerInfoFk() {
        return diskServerInfoFk;
    }

    public void setDiskServerInfoFk(Long disk_server_info_fk) {
        this.diskServerInfoFk = disk_server_info_fk;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "disk_id=" + diskId +
                ", created_date=" + createdDate +
                ", disk_name='" + diskName + '\'' +
                ", disk_server_info_fk=" + diskServerInfoFk +
                '}';
    }
}

