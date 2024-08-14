package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.BaseException;
import com.park.monitoring.config.error.Exception.DataIntegrityException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.mapper.ServerInfoMapper;
import com.park.monitoring.model.ServerInfo;
import com.park.monitoring.util.ServerInfoUtil;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.util.List;

@Service
public class ServerInfoService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    ServerInfoMapper serverInfoMapper;
    OperatingSystemMXBean osBean;

    public ServerInfoService(ServerInfoMapper serverInfoMapper) {

        this.serverInfoMapper = serverInfoMapper;

        OperatingSystemMXBean tempOsBean = null;
        while (tempOsBean == null) {
            tempOsBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        }
        this.osBean = tempOsBean;
    }


    public List<ServerInfo> findAllServerInfo() {
        List<ServerInfo> serverInfo = serverInfoMapper.selectAllServerInfo();
        if (serverInfo == null || serverInfo.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return serverInfo;
    }

    public ServerInfo findServerInfoById(Integer id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(id);
        if (serverInfo == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return serverInfo;
    }

    public int findServerIdByIp(String ip) {
        if (ip == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        Integer serverInfo = serverInfoMapper.findServerIdByIp(ip);
        if (serverInfo == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return serverInfo;
    }

    @Transactional
    public int addServerInfo(ServerInfo serverInfo) {
        int result;
        String os = System.getProperty("os.name").toLowerCase();
        serverInfo.setServerOs(os);
        if(serverInfo.getServerIp()==null) {
            serverInfo.setServerIp(ServerInfoUtil.getServerIp(os));
        }
        serverInfo.setMemoryTotal(osBean.getTotalMemorySize() / 1000000);
        System.out.println(serverInfo);
        if (serverInfo.getServerHostname() == null
                || serverInfo.getServerOs() == null
                || serverInfo.getMemoryTotal() == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        try {
            result = serverInfoMapper.insertServerInfo(serverInfo);
        } catch (DuplicateKeyException e) {
            throw new DataIntegrityException(ErrorCode.DUPLICATED_ENTITY);
        }
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        return result;
    }


    @Transactional
    public int updateServerInfo(ServerInfo serverInfo) {
        if (serverInfo.getServerId() == null || serverInfo.getServerIp() == null
                || serverInfo.getServerIp().isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (serverInfoMapper.selectServerInfoById(serverInfo.getServerId()) == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        try {
            int result = serverInfoMapper.updateServerInfo(serverInfo);
            if (result < 1) {
                throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
            }
            return result;
        } catch (DuplicateKeyException e) {
            throw new DataIntegrityException(ErrorCode.DUPLICATED_ENTITY);
        }
    }

    @Transactional
    public int deleteServerInfo(Integer id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (serverInfoMapper.selectServerInfoById(id) == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        int result = serverInfoMapper.deleteServerInfoById(id);
        if (result < 1) throw new BaseException(ErrorCode.UNEXPECTED_ERROR);
        else return result;
    }

    @Transactional
    public int deleteAll() {
        int result = serverInfoMapper.deleteAll();
        if (result > 0) return result;
        else {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
    }
}
