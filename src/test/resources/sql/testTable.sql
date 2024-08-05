DROP TABLE IF EXISTS disk_log;
DROP TABLE IF EXISTS metric_log;
DROP TABLE IF EXISTS disk;
DROP TABLE IF EXISTS server_info;

CREATE TABLE server_info (
                             server_id int NOT NULL auto_increment,
                             server_os varchar(50) not NULL,
                             server_hostname varchar(50) NOT NULL,
                             memory_total BIGINT not NULL,
                             purpose varchar(100) NULL,
                             server_ip VARCHAR(128) not NULL unique ,
                             PRIMARY KEY (server_id)
);

CREATE TABLE disk (
                      disk_id int NOT NULL auto_increment,
                      created_date datetime not NULL default now(),
                      disk_server_info_fk int NOT NULL,
                      disk_name varchar(50) NULL,
                      PRIMARY KEY (disk_id)
);

CREATE TABLE metric_log (
                            log_id int NOT NULL auto_increment,
                            cpu_usage double NOT NULL,
                            memory_usage double NOT NULL,
                            created_date datetime NULL default CURRENT_TIMESTAMP,
                            server_metric_fk int NOT NULL,
                            disk_usage1 double not NULL,
                            disk_usage2 double NULL,
                            disk_usage3 double NULL,
                            disk_usage4 double NULL,
                            disk_total1 bigint not NULL,
                            disk_total2 bigint NULL,
                            disk_total3 bigint NULL,
                            disk_total4 bigint NULL,
                            disk_name1 varchar(30) not NULL,
                            disk_name2 varchar(30) NULL,
                            disk_name3 varchar(30) NULL,
                            disk_name4 varchar(30) NULL,
                            PRIMARY KEY (log_id)
);

CREATE INDEX idx_server_ip ON server_info (server_ip);