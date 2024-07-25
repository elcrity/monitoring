package com.park.monitoring.mapper;

import com.park.monitoring.model.Disk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiskMapper {

    List<Disk> selectAllDisk();
    List<Disk> selectAllDiskByServerId(Long serverId);
    Disk selectDiskById(Long id);
    int insertDisk(Disk disk);
    int updateDisk(Disk disk);
    int deleteDisk(Long id);
}
