package com.park.monitoring.service;

import com.park.monitoring.dto.DiskInfo;
import com.park.monitoring.mapper.DiskMapper;
import com.park.monitoring.model.Disk;
import com.park.monitoring.util.ServerInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        if (serverId == null) throw new NoSuchElementException("serverId를 확인해주세요");
        List<Disk> diskList = diskMapper.selectAllDiskByServerId(serverId);
        if (diskList.isEmpty()) throw new NoSuchElementException("존재하지 않는 서버입니다");
        return diskList;
    }

    public Disk findDiskById(Integer id) {
        if (id == null) throw new NoSuchElementException("디스크 id를 확인해주세요");
        Disk disk = diskMapper.selectDiskById(id);
        if (disk == null) throw new NoSuchElementException("존재하지 않는 Disk입니다");

        return disk;
    }

    public int insertDisk(Integer serverId) {
        DiskInfo diskInfo = ServerInfoUtil.getDiskInfo();
        int result = 0;
        if (serverId == null) {
            throw new IllegalArgumentException("디스크가 등록될 서버 정보를 가져올 수 없습니다.");
        }
        for (int i = 0; i < diskInfo.getDiskName().size(); i++) {
            Disk disk = new Disk.Builder()
                    .diskName(diskInfo.getDiskName().get(i))
                    .diskServerInfoFk(serverId)
                    .build();
            result = diskMapper.insertDisk(disk);
            if (result < 1) {
                throw new RuntimeException("데이터 입력에 실패했습니다.");
            }

        }
        return result;
    }

    public int updateDisk(Disk disk) {
        if (disk == null) {
            throw new IllegalStateException("수정할 Disk 데이터가 null입니다.");
        }
        if (disk.getDiskId() == null) {
            throw new IllegalArgumentException("Disk ID가 null입니다.");
        }
        if (diskMapper.selectDiskById(disk.getDiskId()) == null) {
            throw new NoSuchElementException("ID가 " + disk.getDiskId() + "인 Disk가 존재하지 않습니다.");
        }

        // 데이터베이스 업데이트 시도
        int result = diskMapper.updateDisk(disk);

        // 업데이트 결과에 따라 예외 처리
        if (result == 0) {
            throw new NoSuchElementException("수정에 실패했습니다.");
        }

        return result;
    }

    public int deleteDisk(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("입력된 Id가 null입니다.");
        }
        if (diskMapper.selectDiskById(id) == null) throw new NoSuchElementException("존재하지 않는 disk입니다. id를 확인해주세요");
        int result = diskMapper.deleteDisk(id);

        if (result != 1) throw new NoSuchElementException("삭제하려는 데이터가 존재하지 않거나 이미 삭제되었습니다.");
        return result;
    }

}
