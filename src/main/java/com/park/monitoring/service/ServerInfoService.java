package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServerInfoService {

    ServerInfoMapper serverInfoMapper;

    public ServerInfoService(ServerInfoMapper serverInfoMapper) {
        this.serverInfoMapper = serverInfoMapper;
    }

    public List<ServerInfo> findAllServerInfo() {
        List<ServerInfo> serverInfos = serverInfoMapper.getAllServerInfo();
        if (serverInfos == null) {
            throw new NoSuchElementException("등록된 서버 정보 없음.");
        }
        return serverInfos;
    }

    public ServerInfo findServerInfoByIp(String ip) {
        if (ip == null) {
            throw new IllegalArgumentException("입력된 IP가 null입니다.");
        }
        ServerInfo serverInfo = serverInfoMapper.getServerInfoByIp(ip);
        if (serverInfo == null) {
            throw new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음.");
        }
        if (serverInfo.getServerIp() == null) {
            throw new IllegalStateException("데이터의 필수 값이 null입니다.");
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        ServerInfo serverInfo = serverInfoMapper.getServerInfoById(id);
        if (serverInfo == null) {
            throw new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음.");
        }
        if (serverInfo.getServerIp() == null) {
            throw new IllegalStateException("데이터의 필수 값이 null입니다.");
        }
        return serverInfo;
    }

    public int addServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerIp() == null) {
            throw new IllegalStateException("데이터의 필수 값이 null입니다.");
        }
        try {
            return serverInfoMapper.addServerInfo(serverInfo);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("데이터 무결성 위반: " + e.getMessage(), e);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("잘못된 인수 전달: " + e.getMessage(), e);
        }
    }


    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null) {
            throw new IllegalArgumentException("서버 ID가 null입니다.");
        }
        //필수값 없음, 타입 미스매치
        try {
            if(serverInfoMapper.getServerInfoById(serverInfo.getServerId()) == null)
                throw new NoSuchElementException("존재하지 않는 서버 ID입니다.");
            return serverInfoMapper.updateServerInfo(serverInfo);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("데이터 무결성 위반: " + e.getMessage(), e);
        }
    }

    public int deleteServerInfoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if(result == 1) return result;
        else throw new NoSuchElementException("존재하지 않는 id. 삭제 실패");
    }
}
