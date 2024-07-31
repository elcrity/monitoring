package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("서버 테스트")
public class ServerInfoMapperTest {
    private static final Logger log = LoggerFactory.getLogger(ServerInfoMapperTest.class);

    private final ServerInfoMapper serverInfoMapper;

    @Autowired
    public ServerInfoMapperTest(ServerInfoMapper serverInfoMapper) {
        this.serverInfoMapper = serverInfoMapper;

    }

    @DisplayName("조회 - 전체")
    @Test
    void t00_getAllServerInfo(){
        List<ServerInfo> servers = serverInfoMapper.selectAllServerInfo();

        assertNotNull(servers);
        assertEquals(servers.size(), 10);
    }

    @DisplayName("조회 - Id")
    @Test
    void t01_getServerInfoById(){
        int serverId = 3;
        ServerInfo server = serverInfoMapper.selectServerInfoById(serverId);
        assertNotNull(server);
        assertThat(server.getServerHostname())
                .isEqualTo("server3");

        ServerInfo checkIdInfo = serverInfoMapper.selectServerInfoById(serverId);
        assertNotNull(checkIdInfo);
        assertThat(checkIdInfo.getServerId()).isEqualTo(serverId);
    }

    @DisplayName("조회 - history")
    @Test
    void t03_getServerInfoAtHistory(){
        int serverId = 1;
        ServerInfo dto = serverInfoMapper.selectServerInfoAtHistory(serverId);
        System.out.println("================= : " + dto.toString());
        assertThat(dto).isNotNull();
        assertThat(dto.getServerId()).isEqualTo(serverId);
    }

    @DisplayName("등록")
    @Test
    void t04_addServerInfo() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000L)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();

        int result = serverInfoMapper.insertServerInfo(serverInfo);
        assertEquals(1, result);
    }

    @DisplayName("수정")
    @Test
    void t05_updateServerInfo() {
        int serverId = 1;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000L)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();
        int result = serverInfoMapper.updateServerInfo(updateInfo);
        ServerInfo updatedInfo = serverInfoMapper.selectServerInfoById(serverId);
        assertEquals(1,  result);
        assertEquals("윈도우", updatedInfo.getServerOs());
        assertEquals("park1104", updatedInfo.getServerHostname());
        assertEquals(16000, updatedInfo.getMemoryTotal());
        assertEquals("ftp 서버", updatedInfo.getPurpose());
        assertEquals("192.168.2.60", updatedInfo.getServerIp());
    }

    @DisplayName("삭제")
    @Test
    void t06_deleteServerInfo() {
        int serverId = 1;
        ServerInfo serverInfo = serverInfoMapper.selectServerInfoById(serverId);
        assertNotNull(serverInfo);
        int result = serverInfoMapper.deleteServerInfoById(serverId);
        assertEquals(1, result);
        assertNull(serverInfoMapper.selectServerInfoById(serverId));
    }

    @DisplayName("전부 삭제")
    @Test
    void t07_deleteAll() {
        int result = serverInfoMapper.deleteAll();

        assertThat(serverInfoMapper.selectAllServerInfo()).isEmpty();
    }
}
