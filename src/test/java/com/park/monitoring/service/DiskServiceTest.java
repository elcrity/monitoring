package com.park.monitoring.service;

import com.park.monitoring.mapper.DiskMapper;
import com.park.monitoring.model.Disk;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional(readOnly = true)
@ActiveProfiles("test")
@Sql({"classpath:sql/testTable.sql", "classpath:sql/testData.sql"})
@DisplayName("디스크 서비스 테스트")
public class DiskServiceTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DiskMapper diskMapper;

    DiskService diskService;

    @BeforeEach
    void init(){
        diskService = new DiskService(diskMapper);
    }

    @DisplayName("데이터 조회")
    @Test
    void t00_testFindAll() {
        List<Disk> disks = diskService.findAllDisks();
        assertThat(disks.size()).isGreaterThan(10);
    }
    @DisplayName("데이터 조회 - serverId")
    @Test
    void t01_01testAllDiskFind_byServerId() {
        Integer serverId = 2;
        List<Disk> disks = diskService.findAllDisksByServerId(serverId);
        assertThat(disks.size()).isGreaterThan(1);
    }

    @DisplayName("데이터 조회 - nullServerId")
    @Test
    void t01_02testAllDiskFind_nullId() {
        Integer serverId = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskService.findAllDisksByServerId(serverId));
    }

    @DisplayName("데이터 조회 - notExistId")
    @Test
    void t01_03testAllDiskFind_notExistId() {
        Integer serverId = 22;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->diskService.findAllDisksByServerId(serverId));
    }

    @DisplayName("디스크 데이터 조회 - id")
    @Test
    void t02_01testFindDisk_byId() {
        Integer id = 2;
        Disk disk = diskService.findDiskById(id);
        assertThat(disk).isNotNull();
        assertThat(disk.getDiskName()).isEqualTo("disk2");
    }

    @DisplayName("디스크 데이터 조회 - Null id")
    @Test
    void t02_02testFindDisk_nullId() {
        Integer id = null;
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->
                diskService.findDiskById(id));
    }

    @DisplayName("디스크 데이터 조회 - No id")
    @Test
    void t02_03testFindDisk_noId() {
        Integer id = 23;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()-> diskService.findDiskById(id));
    }

    @DisplayName("디스크 데이터 등록")
    @Test
    void t03_01createDisk(){
        int serverId = 2;
        int result = diskService.insertDisk(serverId);
        assertThat(result).isGreaterThan(0);


    }

    @DisplayName("디스크 데이터 등록 - 필수 값 x")
    @Test
    void t03_02createDisk_isNull(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->diskService.insertDisk(null));
    }

    @DisplayName("디스크 데이터 등록 - 데이터 이상")
    @Test
    void t03_03createDisk_isNull(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->diskService.insertDisk(null));
    }

    @DisplayName("디스크 데이터 수정")
    @Test
    void t0401updateDisk(){
        int id = 1;

        Disk disk = diskMapper.selectDiskById(id);
        int result = diskService.updateDisk(disk);
        assertAll(
                () -> assertThat(result).isEqualTo(1),
                () -> assertThat(diskService.findDiskById(id).getDiskName())
                        .isEqualTo("disk1")

        );
    }

    @DisplayName("디스크 데이터 수정 - 미존재 데이터")
    @Test
    void t04_02updateDisk_notExist(){
            Disk disk = null;
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(()->diskService.updateDisk(disk));
    }

    @DisplayName("디스크 데이터 수정 - null 값")
    @Test
    void t04_03updateDisk_isNull(){
        Disk disk = new Disk.Builder()
                .diskName("I'm test")
                .diskServerInfoFk(null)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskService.updateDisk(disk));
    }

    @DisplayName("디스크 데이터 삭제")
    @Test
    void t05_01deleteDisk(){
        Integer id = 2;
        int result = diskService.deleteDisk(id);
        assertThat(result).isEqualTo(1);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()-> diskService.findDiskById(id));
    }

    @DisplayName("디스크 데이터 삭제 - 미존재 데이터")
    @Test
    void t05_02deleteDisk_notExist(){
        Integer id = 22;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->diskService.deleteDisk(id));
    }
}
