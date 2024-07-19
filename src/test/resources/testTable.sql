-- 테이블이 이미 존재하는 경우 삭제
DROP TABLE IF EXISTS server_info;

-- board 테이블 생성
CREATE TABLE `server_info` (
                               `server_id` BIGINT NOT NULL AUTO_INCREMENT,
                               `server_os` VARCHAR(50) NULL,
                               `server_hostname` VARCHAR(50) NOT NULL,
                               `memory_total` BIGINT NULL,
                               `purpose` VARCHAR(100) NULL,
                               `server_ip` VARCHAR(128) NULL,
                               PRIMARY KEY (`server_id`)
);