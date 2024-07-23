-- 테이블이 이미 존재하는 경우 삭제
DROP TABLE IF EXISTS metric_logs;
DROP TABLE IF EXISTS disk;
DROP TABLE IF EXISTS server_info;

-- board 테이블 생성
CREATE TABLE server_info (
                               server_id BIGINT NOT NULL AUTO_INCREMENT,
                               server_os VARCHAR(50) NULL,
                               server_hostname VARCHAR(50) NOT NULL,
                               memory_total BIGINT NULL,
                               purpose VARCHAR(100) NULL,
                               server_ip VARCHAR(128) NOT NULL UNIQUE,
                               PRIMARY KEY (server_id)
);

-- disk 테이블 생성
CREATE TABLE disk (
                        disk_id BIGINT NOT NULL AUTO_INCREMENT,
                        disk_name VARCHAR(255) NULL,
                        disk_total bigint NOT NULL ,
                        created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        disk_server_info_fk BIGINT NOT NULL,
                        PRIMARY KEY (disk_id),
                        FOREIGN KEY (disk_server_info_fk) REFERENCES server_info(server_id) ON DELETE CASCADE
);

-- metric_logs 테이블 생성
CREATE TABLE metric_logs (
                               log_id BIGINT NOT NULL AUTO_INCREMENT,
                               cpu_usage VARCHAR(255) NULL,
                               memory_usage VARCHAR(255) NULL,
                               created_date DATETIME NULL,
                               server_id BIGINT NOT NULL,
                               disk_id BIGINT NOT NULL,
                               disk_usage_1 DOUBLE NULL,
                               disk_usage_2 VARCHAR(255) NULL,
                               disk_usage_3 VARCHAR(255) NULL,
                               disk_usage_4 VARCHAR(255) NULL,
                               disk_usage_5 VARCHAR(255) NULL,
                               other_info VARCHAR(255) NULL, -- 변경된 컬럼 이름
                               PRIMARY KEY (log_id),
                               FOREIGN KEY (server_id) REFERENCES server_info(server_id) ON DELETE CASCADE,
                               FOREIGN KEY (disk_id) REFERENCES disk(disk_id) ON DELETE CASCADE
);