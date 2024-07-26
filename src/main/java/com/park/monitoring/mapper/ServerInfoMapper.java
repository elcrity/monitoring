package com.park.monitoring.mapper;

import com.park.monitoring.dto.ServerInfoWithDiskDto;
import com.park.monitoring.model.ServerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServerInfoMapper {
    List<ServerInfo> selectAllServerInfo();
    List<ServerInfoWithDiskDto> selectServerInfoWithDisks();

    ServerInfo selectServerInfoById(Long id);
    int insertServerInfo(ServerInfo serverInfo);
    int updateServerInfo(ServerInfo serverInfo);
    int deleteServerInfoById(Long id);

}
