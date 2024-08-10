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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
public class DiskMapperTest {

    @Autowired
    private DiskMapper diskMapper;


    @DisplayName("디스크 ReadAll 테스트")
    @Test
    void t00_readDisk_all(){
        List<Disk> disks = diskMapper.selectAllDisk();
        assertThat(disks).isNotNull();
        assertThat(disks.size()).isGreaterThan(5);
    }

    @DisplayName("디스크 ReadAll - serverId")
    @Test
    void t00_readDisk_byServerId(){
        int serverId = 2;
        List<Disk> disks = diskMapper.selectAllDiskByServerId(serverId);
        assertThat(disks).isNotNull();
        assertThat(disks.size()).isGreaterThan(1);
    }


    @DisplayName("디스크 Read by id 테스트")
    @Test
    void t01_readDisk_byId(){
        int id = 3;
        Disk disk = diskMapper.selectDiskById(id);
        assertNotNull(disk);
        assertThat(disk.getDiskName())
                .isEqualTo("disk1");
    }

    @DisplayName("디스크 Create 테스트")
    @Test
    void t02_createDisk() {
        Disk disk = new Disk.Builder()
                .diskName("Test Disk")
                .diskServerInfoFk(10)
                .build();

        int result = diskMapper.insertDisk(disk);
        assertEquals(1, result);
    }

    @DisplayName("디스크 update 테스트")
    @Test
    void t03_updateDisk() {
        int id = 1;
        Disk testDisk = diskMapper.selectDiskById(id);
        int result = diskMapper.updateDisk(testDisk);
        assertThat(1).isEqualTo(result);

        Disk updatedDisk = diskMapper.selectDiskById(id);
        assertThat(updatedDisk.getDiskName()).isEqualTo("disk1");

    }
    @DisplayName("디스크 delete 테스트")
    @Test
    void t04_deleteDisk() {
        int id = 1;
        int result = diskMapper.deleteDisk(id);
        assertThat(diskMapper.selectDiskById(id)).isNull();
        assertThat(result).isEqualTo(1);
    }
}
