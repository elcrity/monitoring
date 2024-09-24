package com.park.monitoring.service;

import com.park.monitoring.config.error.ErrorCode;
import com.park.monitoring.config.error.Exception.BadRequestException;
import com.park.monitoring.config.error.Exception.NotFoundException;
import com.park.monitoring.dto.DiskInfo;
import com.park.monitoring.mapper.DiskMapper;
import com.park.monitoring.model.Disk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiskService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    DiskMapper diskMapper;

    public DiskService(DiskMapper diskMapper) {
        this.diskMapper = diskMapper;
    }

    public List<Disk> findAllDisks() {
        return diskMapper.selectAllDisk();
    }

    public List<Disk> findAllDisksByServerId(Integer serverId) {
        if (serverId == null) throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        List<Disk> diskList = diskMapper.selectAllDiskByServerId(serverId);
        if (diskList.isEmpty()) throw new NotFoundException(ErrorCode.NOT_FOUND);
        return diskList;
    }

    public Disk findDiskById(Integer id) {
        if (id == null) throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        Disk disk = diskMapper.selectDiskById(id);
        if (disk == null) throw new NotFoundException(ErrorCode.NOT_FOUND);

        return disk;
    }

    public int insertDisk(Integer serverId) {
        File[] diskData = File.listRoots();
        List<Double> diskTotalData = new ArrayList<>();
        List<Double> diskUsageData = new ArrayList<>();
        List<String> diskName = new ArrayList<>();

        for (File disk : diskData) {
            double diskTotal = disk.getTotalSpace();
            double usagePercentage = ((double) (diskTotal - disk.getFreeSpace()) / diskTotal) * 100;

            diskTotalData.add(diskTotal / Math.pow(1024.0, 2));  // MB 단위로 변환
            diskUsageData.add(Double.valueOf(String.format("%.2f", usagePercentage)));
            diskName.add(disk.getAbsolutePath());
        }
        DiskInfo diskInfo = new DiskInfo(diskTotalData, diskUsageData, diskName);
        int result = 0;
        if (serverId == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        for (int i = 0; i < diskInfo.getDiskName().size(); i++) {
            Disk disk = new Disk.Builder()
                    .diskName(diskInfo.getDiskName().get(i))
                    .serverDiskFk(serverId)
                    .build();
            result = diskMapper.insertDisk(disk);
            if (result < 1) {
                throw new RuntimeException("디스크 데이터 입력에 실패했습니다.");
            }

        }
        return result;
    }

    public int updateDisk(Disk disk) {
        if (disk == null) {
            throw new IllegalStateException("수정할 Disk가 존재하지 않습니다.");
        }
        if (disk.getDiskId() == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 데이터베이스 업데이트 시도
        int result = diskMapper.updateDisk(disk);

        // 업데이트 결과에 따라 예외 처리
        if (result < 1) {
            throw new RuntimeException("수정에 실패했습니다.");
        }

        return result;
    }

    public int deleteDisk(Integer id) {
        if (id == null) {
            throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (diskMapper.selectDiskById(id) == null) throw new NotFoundException(ErrorCode.NOT_FOUND);
        int result = diskMapper.deleteDisk(id);
        if (result < 1) throw new RuntimeException("삭제하려는 데이터가 존재하지 않거나 이미 삭제되었습니다.");
        return result;
    }

}
