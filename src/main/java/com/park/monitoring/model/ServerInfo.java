package com.park.monitoring.model;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class ServerInfo {
    @Schema(hidden = true, description = "서버Id", example = "1")
    Integer serverId;
    @Schema(description = "서버Os", example = "window11")
    String serverOs;
    @Schema(description = "서버 호스트이름", example = "DESTOP-WE721")
    String serverHostname;
    @Schema(description = "메모리(MB)", example = "8192")
    Long memoryTotal;
    @Schema(description = "서버 용도", example = "테스트용 서버")
    String purpose;
    @Schema(description = "서버Ip", example = "192.168.1.1")
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
        private Integer serverId;
        private String serverOs;
        private String serverHostname;
        private Long memoryTotal;
        private String purpose;
        private String serverIp;

        public Builder serverId(Integer serverId){
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
        public Builder memoryTotal(Long memoryTotal) {
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

    public void setMemoryTotal(Long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getServerOs() {
        return serverOs;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public Long getMemoryTotal() {
        return memoryTotal;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerInfo that)) return false;
        return serverId == that.serverId && Objects.equals(serverOs, that.serverOs) && Objects.equals(serverHostname, that.serverHostname) && Objects.equals(memoryTotal, that.memoryTotal) && Objects.equals(purpose, that.purpose) && Objects.equals(serverIp, that.serverIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, serverOs, serverHostname, memoryTotal, purpose, serverIp);
    }
}
