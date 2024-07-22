package com.park.monitoring.model;


public class ServerInfo {
    Long serverId;

    String serverOs;
    String serverHostname;
    double memoryTotal;
    String purpose;
    String serverIp;

    public ServerInfo(){}

    public ServerInfo(Builder builder){
        this.serverId = builder.serverId;
        this.serverOs = builder.serverOs;
        this.serverHostname = builder.serverHostname;
        this.memoryTotal = builder.memoryTotal;
        this.purpose = builder.purpose;
        this.serverIp = builder.serverIp;
    }

    public static class Builder{
        private Long serverId;
        private String serverOs;
        private String serverHostname;
        private double memoryTotal;
        private String purpose;
        private String serverIp;

        public Builder serverId(Long serverId){
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
        public Builder memoryTotal(double memoryTotal) {
            this.memoryTotal = memoryTotal;
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
        public ServerInfo build(){
            return new ServerInfo(this);
        }
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public void setMemoryTotal(double memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Long getServerId() {
        return serverId;
    }

    public String getServerOs() {
        return serverOs;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public double getMemoryTotal() {
        return memoryTotal;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getServerIp() {
        return serverIp;
    }
    @Override
    public String toString() {
        return "ServerInfo{" +
                "serverId=" + serverId +
                ", serverOs='" + serverOs + '\'' +
                ", serverHostname='" + serverHostname + '\'' +
                ", memoryTotal=" + memoryTotal +
                ", purpose='" + purpose + '\'' +
                ", serverIp='" + serverIp + '\'' +
                '}';
    }
}
