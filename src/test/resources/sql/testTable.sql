-- 테이블이 이미 존재하는 경우 삭제

DROP TABLE IF EXISTS disk_log;
DROP TABLE IF EXISTS metric_log;
DROP TABLE IF EXISTS disk;
DROP TABLE IF EXISTS server_info;

CREATE TABLE server_info
(
    server_id       BIGINT       NOT NULL auto_increment,
    server_os       VARCHAR(50)  NULL,
    server_hostname VARCHAR(50)  NULL,
    memory_total    BIGINT       not NULL,
    purpose         VARCHAR(100) NULL,
    server_ip       VARCHAR(128) NOT NULL unique ,
    PRIMARY KEY (server_id)
);

CREATE TABLE disk
(
    disk_id          BIGINT         NOT NULL AUTO_INCREMENT,
    created_date     DATETIME    DEFAULT CURRENT_TIMESTAMP,
    disk_server_info_fk BIGINT      NOT NULL,
    disk_name        VARCHAR(50) NULL,
    disk_total       BIGINT      not NULL,
    PRIMARY KEY (disk_id),
    FOREIGN KEY (disk_server_info_fk) REFERENCES server_info (server_id) ON DELETE CASCADE
);

CREATE TABLE metric_log
(
    log_id       BIGINT      NOT NULL AUTO_INCREMENT,
    cpu_usage    DOUBLE   not NULL,
    memory_usage DOUBLE   not NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    server_metric_fk    BIGINT   NOT NULL,
    PRIMARY KEY (log_id),
    FOREIGN KEY (server_metric_fk) REFERENCES server_info (server_id) ON DELETE CASCADE
);

CREATE TABLE disk_log
(
    disk_log_id        INT    NOT NULL AUTO_INCREMENT,
    disk_usage         DOUBLE not NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    disk_disk_log_fk            BIGINT NOT NULL,
    PRIMARY KEY (disk_log_id),
    FOREIGN KEY (disk_disk_log_fk) REFERENCES disk (disk_id) ON DELETE CASCADE
);

-- board 테이블 생성
# CREATE TABLE server_info (
#                                server_id BIGINT NOT NULL AUTO_INCREMENT,
#                                server_os VARCHAR(50) NULL,
#                                server_hostname VARCHAR(50) NOT NULL,
#                                memory_total BIGINT NULL,
#                                purpose VARCHAR(100) NULL,
#                                server_ip VARCHAR(128) NOT NULL UNIQUE,
#                                PRIMARY KEY (server_id)
# );
#
# -- disk 테이블 생성
# CREATE TABLE disk (
#                         disk_id BIGINT NOT NULL AUTO_INCREMENT,
#                         disk_name VARCHAR(255) NULL,
#                         disk_total bigint NOT NULL ,
#                         created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
#                         disk_server_info_fk BIGINT NOT NULL,
#                         PRIMARY KEY (disk_id),
#                         FOREIGN KEY (disk_server_info_fk) REFERENCES server_info(server_id) ON DELETE CASCADE
# );
#
# -- metric_logs 테이블 생성
# CREATE TABLE metric_log (
#                                log_id bigint NOT NULL,
#                                cpu_usage VARCHAR(255) NULL,
#                                memory_usage VARCHAR(255) NULL,
#                                created_date datetime NULL,
#                                server_id bigint NOT NULL,
#                                disk_id bigint NOT NULL AUTO_INCREMENT,
#                                disk_usage_1 double NULL,
#                                disk_usage_2 double NULL,
#                                disk_usage_3 double NULL,
#                                disk_usage_4 double NULL,
#                                disk_usage_5 double NULL,
#                                disk_usage_6 double NULL,
#                                disk_usage_7 double NULL,
#                                disk_usage_8 double NULL,
#                                disk_usage_9 double NULL,
#                                disk_usage_10 double NULL,
#                                PRIMARY KEY (log_id)
# );