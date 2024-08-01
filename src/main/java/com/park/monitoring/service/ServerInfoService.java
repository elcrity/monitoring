package com.park.monitoring.service;

import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServerInfoService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    ServerInfoMapper serverInfoMapper;

    public ServerInfoService(ServerInfoMapper serverInfoMapper) {
        this.serverInfoMapper = serverInfoMapper;
    }

public List<ServerInfo> findAllServerInfo() {
    List<ServerInfo> serverInfo = serverInfoMapper.selectAllServerInfo();
    if (serverInfo == null) {
        throw new NoSuchElementException("데이터를 조회하는데 실패했습니다. - " + this.getClass());
    }
    return serverInfo;
}

    public ServerInfo findServerInfoById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(id);
        if (serverInfo == null) {
            throw new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음. - " + this.getClass());
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoAtHistory(Integer id) {
        if (id == null) throw new IllegalArgumentException("입력받은 값이 null입니다.");
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoAtHistory(id);
        if (serverInfo == null) throw new NoSuchElementException("해당되는 데이터가 없습니다.");
        return serverInfo;
    }

    @Transactional
    public int addServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerIp() == null
            || serverInfo.getServerHostname() == null
            || serverInfo.getServerOs() == null
            || serverInfo.getMemoryTotal() == null) {
            throw new IllegalArgumentException("필수 값이 null입니다.");
        }
        if(serverInfoMapper.isIpExists(serverInfo.getServerIp()) == 1){
            throw new DataIntegrityViolationException("이미 존재하는 ip입니다.");
        }
        int result = serverInfoMapper.insertServerInfo(serverInfo);
        if (result < 1) throw new RuntimeException("데이터 등록 실패");
        return result;

    }


    @Transactional
    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null
                || serverInfo.getServerIp() == null) {
            throw new IllegalArgumentException("입력받은 값이 비정상입니다. - " + this.getClass());
        }
        if (serverInfoMapper.selectServerInfoById(serverInfo.getServerId()) == null) {
            throw new NoSuchElementException("수정할 Server를 찾을수 없습니다. - " + this.getClass());
        }
        int result = serverInfoMapper.updateServerInfo(serverInfo);
        if (result < 1) {
            throw new RuntimeException("수정에 실패했습니다.");
        }
        return result;
    }

    @Transactional
    public int deleteServerInfo(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        if(serverInfoMapper.selectServerInfoById(id)==null){
            throw new NoSuchElementException("존재하지 않는 서버입니다.");
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if (result < 1) throw new RuntimeException("데이터 삭제 실패");
        else return result;
    }

    @Transactional
    public int deleteAll() {
        int result = serverInfoMapper.deleteAll();
        if (result >= 1) return result;
        else {
            throw new EmptyResultDataAccessException("삭제할 데이터가 존재하지 않습니다.",1);
        }
    }
}
