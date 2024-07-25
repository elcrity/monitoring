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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(disks);
        assertEquals(disks.size(), 10);
    }

    @DisplayName("디스크 ReadAll - serverId")
    @Test
    void t00_readDisk_byServerId(){
        Long serverId = 2L;
        List<Disk> disks = diskMapper.selectAllDiskByServerId(2L);
        assertThat(disks).isNotNull();
        assertThat(disks.size()).isGreaterThan(1);
    }


    @DisplayName("디스크 Read by id 테스트")
    @Test
    void t01_readDisk_byId(){
        Long id = 3L;
        Disk disk = diskMapper.selectDiskById(id);
        assertNotNull(disk);
        assertThat(disk.getDiskName())
                .isEqualTo("Disk C1");
    }

    @DisplayName("디스크 Create 테스트")
    @Test
    void t02_createDisk() {
        Disk disk = new Disk.Builder()
                .diskName("Test Disk")
                .diskTotal(18092L)
                .diskServerInfoFk(10L)
                .build();

        int result = diskMapper.insertDisk(disk);
        assertEquals(1, result);
    }

    @DisplayName("디스크 update 테스트")
    @Test
    void t03_updateDisk() {
        Long id = 1L;
        Disk disk = new Disk.Builder()
                .diskId(id)
                .diskName("TDisk")
                .diskTotal(18888L)
                .diskServerInfoFk(2L)
                .build();
        int result = diskMapper.updateDisk(disk);
        assertThat(1).isEqualTo(result);

        Disk updatedDisk = diskMapper.selectDiskById(id);
        assertThat(updatedDisk.getDiskName()).isEqualTo("TDisk");

    }
    @DisplayName("디스크 delete 테스트")
    @Test
    void t04_deleteDisk() {
        Long id = 1L;
        int result = diskMapper.deleteDisk(id);
        assertThat(diskMapper.selectDiskById(id)).isNull();
        assertThat(result).isEqualTo(1);
    }
}
