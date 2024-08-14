package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServerInfoMapper {
//    List<ServerInfo> selectAllServerInfo();
    List<ServerInfo> selectAllServerInfo();
    ServerInfo selectServerInfoAtHistory(int serverId);
    ServerInfo selectServerInfoById(Integer id);
    int insertServerInfo(ServerInfo serverInfo);
    int updateServerInfo(ServerInfo serverInfo);
    int deleteServerInfoById(Integer id);
    int deleteAll();
    int isIpExists(String ip);

    Integer findServerIdByIp(String serverIp);
}
