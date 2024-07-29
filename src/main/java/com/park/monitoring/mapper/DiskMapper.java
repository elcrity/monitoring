package com.park.monitoring.mapper;

import com.park.monitoring.model.Disk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiskMapper {

    List<Disk> selectAllDisk();
    List<Disk> selectAllDiskByServerId(Integer serverId);
    Disk selectDiskById(Integer id);
    int insertDisk(Disk disk);
    int updateDisk(Disk disk);
    int deleteDisk(Integer id);
}
