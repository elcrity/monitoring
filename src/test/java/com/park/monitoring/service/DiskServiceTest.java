package com.park.monitoring.service;

import com.park.monitoring.mapper.DiskMapper;
import com.park.monitoring.model.Disk;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
public class DiskServiceTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DiskMapper diskMapper;

    DiskService diskService;

    @BeforeEach
    void init(){
        diskService = new DiskService(diskMapper);
    }

    @DisplayName("디스크 데이터 조회")
    @Test
    void t01_testFindAll() {
        List<Disk> disks = diskService.findAllDisks();
        assertThat(disks.size()).isEqualTo(10);
    }

    @DisplayName("디스크 데이터 조회 - id")
    @Test
    void t02_testFindDisk_byId() {
        Long id = 2L;
        Disk disk = diskService.findDiskById(id);
        assertThat(disk).isNotNull();
        assertThat(disk.getDiskName()).isEqualTo("Disk B1");
    }

    @DisplayName("디스크 데이터 조회 - Null id")
    @Test
    void t03_testFindDisk_nullId() {
        Long id = null;
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(()->
                diskService.findDiskById(id));
    }

    @DisplayName("디스크 데이터 조회 - No id")
    @Test
    void t04_testFindDisk_noId() {
        Long id = 23L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()-> diskService.findDiskById(id));
    }

    @DisplayName("디스크 데이터 등록")
    @Test
    void t05_createDisk(){
        Disk disk = new Disk.Builder()
                .diskName("I'm test")
                .diskTotal(8092L)
                .diskServerInfoFk(2L)
                .build();
        diskService.insertDisk(disk);
        assertThat(diskService.findDiskById(11L))
                .extracting(Disk::getDiskName)
                .isEqualTo("I'm test");
    }

    @DisplayName("디스크 데이터 등록 - 필수 값 x")
    @Test
    void t05_createDisk_isNull(){
        Disk disk = new Disk.Builder()
                .diskName("I'm test")
                .diskTotal(null)
                .diskServerInfoFk(2L)
                .build();
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() ->diskService.insertDisk(disk));
    }

    @DisplayName("디스크 데이터 등록 - 데이터 이상")
    @Test
    void t05_02_createDisk_isNull(){
        Disk disk = new Disk.Builder()
                .diskName("I'm test")
                .diskTotal(8092L)
                .diskServerInfoFk(-2L)
                .build();
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() ->diskService.insertDisk(disk));
    }

    @DisplayName("디스크 데이터 수정")
    @Test
    void t06_updateDisk(){
        Disk disk = new Disk.Builder()
                .diskId(1L)
                .diskName("I'm test")
                .diskTotal(8095L)
                .diskServerInfoFk(2L)
                .build();
        int result = diskService.updateDisk(disk);
        assertAll(
                () -> assertThat(result).isEqualTo(1),
                () -> assertThat(diskService.findDiskById(1L))
                        .extracting(Disk::getDiskName, Disk::getDiskTotal)
                        .containsExactly("I'm test",8095L)
        );
    }

    @DisplayName("디스크 데이터 수정 - 미존재 데이터")
    @Test
    void t07_updateDisk_notExist(){
        Disk disk = new Disk.Builder()
                .diskId(33L)
                .diskName("I'm test")
                .diskTotal(8095L)
                .diskServerInfoFk(2L)
                .build();
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->diskService.updateDisk(disk));
    }

    @DisplayName("디스크 데이터 수정 - null 값")
    @Test
    void t08_updateDisk_isNull(){
        Disk disk = new Disk.Builder()
                .diskId(null)
                .diskName("I'm test")
                .diskTotal(8095L)
                .diskServerInfoFk(2L)
                .build();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->diskService.updateDisk(disk))
                .withMessage("Disk ID가 null입니다.");;
    }

    @DisplayName("디스크 데이터 삭제")
    @Test
    void t09_deleteDisk(){
        Long id = 2L;
        int result = diskService.deleteDisk(id);
        assertThat(result).isEqualTo(1);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()-> diskService.findDiskById(id))
                .withMessage("존재하지 않는 Disk입니다");
    }

    @DisplayName("디스크 데이터 삭제 - 미존재 데이터")
    @Test
    void t10_deleteDisk_notExist(){
        Long id = 22L;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(()->diskService.deleteDisk(id))
                .withMessage("존재하지 않는 disk입니다. id를 확인해주세요");
    }
}
