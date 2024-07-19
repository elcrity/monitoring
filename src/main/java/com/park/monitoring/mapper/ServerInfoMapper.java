package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServerInfoMapper {
    List<ServerInfo> getAllServerInfo();
    ServerInfo getServerInfoById(int id);
    int addServerInfo(ServerInfo serverInfo);
    int updateServerInfo(ServerInfo serverInfo);
    int deleteServerInfoById(int id);

}
