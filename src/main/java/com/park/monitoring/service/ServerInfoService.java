package com.park.monitoring.service;

import com.park.monitoring.dto.ServerInfoWithDiskDto;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServerInfoService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ServerInfoMapper serverInfoMapper;

    public ServerInfoService(ServerInfoMapper serverInfoMapper) {
        this.serverInfoMapper = serverInfoMapper;
    }

    public List<ServerInfo> findAllServerInfo() {
        List<ServerInfo> serverInfos = serverInfoMapper.selectAllServerInfo();
        if (serverInfos == null) {
            throw new NoSuchElementException("등록된 서버 정보 없음.");
        }
        return serverInfos;
    }

    public ServerInfo findServerInfoById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(id);
        if (serverInfo == null) {
            throw new NoSuchElementException("service, 해당 ip로 가져온 데이터 없음.");
        }
        return serverInfo;
    }

    public List<ServerInfoWithDiskDto> findServerInfoWithDisk() {
        List<ServerInfoWithDiskDto> serverInfo = serverInfoMapper.selectServerInfoWithDisks();
        if (serverInfo.isEmpty()) {
            throw new NoSuchElementException("해당되는 데이터가 없습니다");
        }
        return serverInfo;
    }

    public ServerInfoWithDiskDto findServerInfoAtHistory(Integer id){
        if (id == null) throw new IllegalArgumentException("입력받은 Id값이 null입니다.");
        ServerInfoWithDiskDto serverInfo = serverInfoMapper.selectServerInfoAtHistory(id);
        if (serverInfo == null) throw new DataIntegrityViolationException("해당되는 데이터가 없습니다.");
        return serverInfo;
    }

    public int addServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerIp() == null) {
            throw new IllegalStateException("데이터의 필수 값이 null입니다.");
        }
        try {
            return serverInfoMapper.insertServerInfo(serverInfo);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("데이터 무결성 위반: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 값 전달: " + e.getMessage(), e);
        }
    }


    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null) {
            throw new IllegalArgumentException("서버 id를 확인해주세요");
        }
        if (serverInfo == null) {
            throw new IllegalArgumentException("수정할 Server 데이터가 null입니다.");
        }
        if(serverInfo.getServerIp() == null){
            throw new IllegalArgumentException("입력된 데이터를 확인해주세요. Ip주소 없음");
        }
        if (serverInfoMapper.selectServerInfoById(serverInfo.getServerId()) == null) {
            throw new NoSuchElementException("ID가 " + serverInfo.getServerId() + "인 Server가 존재하지 않습니다.");
        }
        //필수값 없음, 타입 미스매치
        int result = serverInfoMapper.updateServerInfo(serverInfo);
        if (result == 0) {
            throw new NoSuchElementException("수정에 실패했습니다.");
        }
        return result;

    }

    public int deleteServerInfo(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if (result >= 1) return result;
        else {
            throw new NoSuchElementException("존재하지 않는 id. 삭제 실패");
        }
    }

    public int deleteAll(){
        int result = serverInfoMapper.deleteAll();
        if( result >= 1) return result;
        else {
            throw new DataIntegrityViolationException("데이터 삭제 실패");
        }
    }
}
