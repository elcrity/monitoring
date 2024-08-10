package com.park.monitoring.dto;

import com.park.monitoring.model.Disk;

import java.util.List;

public class DashBoardDto {
    Integer serverId;
    String serverOs;
    String serverHostname;
    Long memoryTotal;
    String purpose;
    String serverIp;
    List<Disk> disks;

    public DashBoardDto(Builder builder) {
        this.serverId = builder.serverId;
        this.serverOs = builder.serverOs;
        this.serverHostname = builder.serverHostname;
        this.memoryTotal = builder.memoryTotal;
        this.purpose = builder.purpose;
        this.serverIp = builder.serverIp;
        this.disks = builder.disks;
    }

    public static class Builder{
        private Integer serverId;
        private String serverOs;
        private String serverHostname;
        private Long memoryTotal;
        private String purpose;
        private String serverIp;
        private List<Disk> disks;

        public Builder serverId(Integer serverId){
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
        public Builder memoryTotal(Long memoryTotal){
            this.memoryTotal = memoryTotal;
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
        public Builder disks(List<Disk> disks){
            this.disks = disks;
            return this;
        }

        public DashBoardDto build(){
            return new DashBoardDto(this);
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

    public Long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Long memoryTotal) {
        this.memoryTotal = memoryTotal;
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

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    @Override
    public String toString() {
        return "DashBoardDto{" +
                "serverId=" + serverId +
                ", serverOs='" + serverOs + '\'' +
                ", serverHostname='" + serverHostname + '\'' +
                ", memoryTotal=" + memoryTotal +
                ", purpose='" + purpose + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", disks=" + disks +
                '}';
    }
}
