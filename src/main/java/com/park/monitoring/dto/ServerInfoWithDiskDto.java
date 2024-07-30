package com.park.monitoring.dto;

import com.park.monitoring.model.Disk;

import java.util.List;

public class ServerInfoWithDiskDto {
    Integer serverId;
    String serverOs;
    String serverHostname;
    String purpose;
    String serverIp;
    List<Disk> disk;

    public ServerInfoWithDiskDto() {
    }

    public ServerInfoWithDiskDto(Builder builder) {
        this.serverId = builder.serverId;
        this.serverOs = builder.serverOs;
        this.serverHostname = builder.serverHostname;
        this.purpose = builder.purpose;
        this.serverIp = builder.serverIp;
        this.disk = builder.disk;
    }

    public static class Builder {
        private Integer serverId;
        private String serverOs;
        private String serverHostname;
        private String purpose;
        private String serverIp;
        private List<Disk> disk;


        public Builder serverId(int serverId) {
            this.serverId = serverId;
            return this;
        }

        public Builder serverOs(String serverOs) {
            this.serverOs = serverOs;
            return this;
        }

        public Builder serverHostname(String serverHostname) {
            this.serverHostname = serverHostname;
            return this;
        }

        public Builder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public Builder serverIp(String serverIp) {
            this.serverIp = serverIp;
            return this;
        }

        public Builder disk(List<Disk> disk) {
            this.disk = disk;
            return this;
        }

        public ServerInfoWithDiskDto build() {
            return new ServerInfoWithDiskDto(this);

        }
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerOs() {
        return serverOs;
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public List<Disk> getDisk() {
        return disk;
    }

    public void setDisk(List<Disk> disk) {
        this.disk = disk;
    }

    @Override
    public String toString() {
        return "ServerInfoWithDiskDto{" +
                "serverId=" + serverId +
                ", serverOs='" + serverOs + '\'' +
                ", serverHostname='" + serverHostname + '\'' +
                ", purpose='" + purpose + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", disk=" + disk +
                '}';
    }

}
