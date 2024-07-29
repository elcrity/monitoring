package com.park.monitoring.dto;

public class LogHistoryDto {
    private int serverId;
    private String logs;

    public LogHistoryDto() {
    }

    public LogHistoryDto(Builder builder) {
        this.serverId = builder.serverId;
        this.logs = builder.logs;
    }

    public static class Builder{
        private int serverId;
        private String logs;
        public Builder setServerId(int serverId) {
            this.serverId = serverId;
            return this;
        }
        public Builder setLogs(String logs) {
            this.logs = logs;
            return this;
        }
        public LogHistoryDto build() {
            return new LogHistoryDto(this);
        }
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
}
