package com.park.monitoring.mapper;

import com.park.monitoring.model.Disk;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testServerData.sql","classpath:sql/testDiskData.sql"})
public class DiskMapperTest {

    @Autowired
    private DiskMapper diskMapper;


    @DisplayName("디스크 ReadAll 테스트")
    @Test
    void t00_readDisk_all(){
        List<Disk> disks = diskMapper.getAllDisk();

        assertNotNull(disks);
        assertEquals(disks.size(), 20);
    }

    @DisplayName("디스크 Read by id 테스트")
    @Test
    void t01_readDisk_byId(){
        Long id = 3L;
        Disk disk = diskMapper.getDiskById(id);
        assertNotNull(disk);
        assertThat(disk.getDiskName())
                .isEqualTo("Disk C1");
    }

    @DisplayName("디스크 Create 테스트")
    @Test
    void t02_createDisk() {
        Disk disk = new Disk.Builder()
                .diskName("Test Disk")
                .createdDate(LocalDateTime.now())
                .diskServerInfoFk(10L)
                .build();

        int result = diskMapper.insertDisk(disk);
        assertEquals(1, result);

        Disk createdDisk = diskMapper.getDiskById(21L);
        assertThat(createdDisk.getDiskName()).isEqualTo("Test Disk");
    }

    @DisplayName("디스크 update 테스트")
    @Test
    void t03_updateDisk() {
        Long id = 1L;
        Disk disk = new Disk.Builder()
                .diskId(id)
                .diskName("TDisk")
                .createdDate(LocalDateTime.now())
                .diskServerInfoFk(2L)
                .build();
        int result = diskMapper.updateDisk(disk);
        assertThat(1).isEqualTo(result);

        Disk updatedDisk = diskMapper.getDiskById(id);
        assertThat(updatedDisk.getDiskName()).isEqualTo("TDisk");

    }
    @DisplayName("디스크 delete 테스트")
    @Test
    void t04_deleteDisk() {
        Long id = 1L;
        diskMapper.deleteDisk(id);
        assertThat(diskMapper.getDiskById(id)).isNull();
        assertThat(diskMapper.getAllDisk().size()).isEqualTo(19);
    }
}
