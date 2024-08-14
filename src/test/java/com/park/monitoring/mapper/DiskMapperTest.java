package com.park.monitoring.mapper;

import com.park.monitoring.model.Disk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("디스크 매퍼 테스트")
public class DiskMapperTest {

    @Autowired
    private DiskMapper diskMapper;


    @DisplayName("디스크 ReadAll 테스트")
    @Test
    void t00_readDisk_all(){
        assertThat(diskMapper.selectAllDisk().size()).isGreaterThan(1);
    }

    @DisplayName("디스크 ReadAll - serverId")
    @Test
    void t00_readDisk_byServerId(){
        int serverId = 2;
        assertThat(diskMapper.selectAllDiskByServerId(serverId).size()).isGreaterThan(1);
    }


    @DisplayName("디스크 Read by id 테스트")
    @Test
    void t01_readDisk_byId(){
        int id = 1;
        assertThat(diskMapper.selectDiskById(id)).isNotNull();
    }

    @DisplayName("디스크 Create 테스트")
    @Test
    void t02_createDisk() {
        Disk disk = new Disk.Builder()
                .diskName("Test Disk")
                .diskServerInfoFk(10)
                .build();

        int result = diskMapper.insertDisk(disk);
        assertThat(diskMapper.insertDisk(disk)).isEqualTo(1);
    }

    @DisplayName("디스크 update 테스트")
    @Test
    void t03_updateDisk() {
        Disk disk = new Disk.Builder()
                .diskId(1)
                .diskName("test")
                .diskServerInfoFk(2)
                .build();
        assertThat(diskMapper.updateDisk(disk)).isEqualTo(1);


    }
    @DisplayName("디스크 delete 테스트")
    @Test
    void t04_deleteDisk() {
        int id = 1;
        assertThat(diskMapper.deleteDisk(id)).isEqualTo(1);
    }
}
