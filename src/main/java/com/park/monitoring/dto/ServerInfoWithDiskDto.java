package com.park.monitoring.dto;

public class ServerInfoWithDiskDto {
    Integer serverId;
    String serverOs;
    String serverHostname;
    String purpose;
    String serverIp;
//    JSON 객체
    String disks;

    public ServerInfoWithDiskDto(){}

    public ServerInfoWithDiskDto(Builder builder) {
        this.serverId = builder.serverId;
        this.serverOs = builder.serverOs;
        this.serverHostname = builder.serverHostname;
        this.purpose = builder.purpose;
        this.serverIp = builder.serverIp;
        this.disks = builder.disks;
    }
    public static class Builder{
        private Integer serverId;
        private String serverOs;
        private String serverHostname;
        private String purpose;
        private String serverIp;
        private String disks;


        public Builder serverId(int serverId){
            this.serverId = serverId;
            return this;
        }
        public Builder serverOs(String serverOs){
            this.serverOs = serverOs;
            return this;
        }
        public Builder serverHostname(String serverHostname){
            this.serverHostname = serverHostname;
            return this;
        }
        public Builder purpose(String purpose){
            this.purpose = purpose;
            return this;
        }
        public Builder serverIp(String serverIp){
            this.serverIp = serverIp;
            return this;
        }
        public Builder disks(String disks){
            this.disks = disks;
            return this;
        }

        public ServerInfoWithDiskDto build(){
            return new ServerInfoWithDiskDto(this);

        }
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
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

    public String getDisks() {
        return disks;
    }

    public void setDisks(String disks) {
        this.disks = disks;
    }

    @Override
    public String toString() {
        return "ServerInfoWithDiskDto{" +
                "serverId=" + serverId +
                ", serverOs='" + serverOs + '\'' +
                ", serverHostname='" + serverHostname + '\'' +
                ", purpose='" + purpose + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", disks='" + disks + '\'' +
                '}';
    }
}
