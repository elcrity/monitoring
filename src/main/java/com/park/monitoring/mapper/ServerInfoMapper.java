package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;

import java.util.List;

public interface ServerInfoMapper {
    List<ServerInfo> getAllServerInfo();
    ServerInfo getServerInfoById(int id);
    int addServerInfo(ServerInfo serverInfo);
    int updateServerInfo(ServerInfo serverInfo);
    int deleteServerInfoById(int id);

}
