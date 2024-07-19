package com.park.monitoring.mapper;

import com.park.monitoring.model.ServerInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("local")
@Sql({"classpath:testTable.sql", "classpath:testData.sql"})
public class ServerInfoMapperTest {
    private static final Logger log = LoggerFactory.getLogger(ServerInfoMapperTest.class);

    @Autowired
    private ServerInfoMapper serverInfoMapper;

    private ServerInfo serverInfo;

    @Test
    public void getAllServerInfo(){
        List<ServerInfo> servers = serverInfoMapper.getAllServerInfo();
        assertNotNull(servers);
        assertEquals(servers.size(), 10);
    }

}
