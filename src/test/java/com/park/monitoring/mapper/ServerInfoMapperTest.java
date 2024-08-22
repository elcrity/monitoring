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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("서버 매퍼 테스트")
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
        assertThat(serverInfoMapper.selectAllServerInfo().size()).isGreaterThan( 10);
    }

    @DisplayName("조회 - Id")
    @Test
    void t01_getServerInfoById(){
        assertThat(serverInfoMapper.selectServerInfoById(1))
                .isNotNull();
    }

    @DisplayName("등록")
    @Test
    void t02_addServerInfo() {
        ServerInfo serverInfo = new ServerInfo.Builder()
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000L)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();
        assertThat(serverInfoMapper.insertServerInfo(serverInfo)).isEqualTo(1);
    }

    @DisplayName("수정")
    @Test
    void t03_updateServerInfo() {
        int serverId = 1;
        ServerInfo updateInfo = new ServerInfo.Builder()
                .serverId(serverId)
                .serverOs("윈도우")
                .serverHostname("park1104")
                .memoryTotal(16000L)
                .purpose("ftp 서버")
                .serverIp("192.168.2.60")
                .build();
        assertThat(serverInfoMapper.updateServerInfo(updateInfo)).isEqualTo(1);
    }

    @DisplayName("삭제")
    @Test
    void t04_deleteServerInfo() {
        assertThat(serverInfoMapper.deleteServerInfoById(1)).isEqualTo(1);
    }

    @DisplayName("findByIp")
    @Test
    void t05_findByIp(){
        assertThat(serverInfoMapper.findServerIdByIp("192.168.1.1")).isEqualTo(1);
    }
}
