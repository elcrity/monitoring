select * from metric_log
where created_date >= '2024-09-07 00:00:00' and created_date < '2024-09-08 00:00:00' and server_metric_fk=20;

call InsertLogs('2024-09-11 07:00:00', '2024-09-11 08:00:00', 20);

delete from metric_log where  created_date >= '2024-09-08 00:00:00' and created_date < '2024-09-09 00:00:00';

SET @start_date = '2024-09-10 00:00:00';

INSERT INTO metric_log (
    cpu_usage, memory_usage, created_date, server_metric_fk,
    disk_usage1, disk_usage2, disk_usage3, disk_usage4,
    disk_total1, disk_total2, disk_total3, disk_total4,
    disk_name1, disk_name2, disk_name3, disk_name4
) VALUES
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 1 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 2 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 3 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 4 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 5 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 6 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 7 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 8 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 9 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 10 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
      (ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), @start_date + INTERVAL 11 MINUTE, 21, ROUND(40 + RAND()*20,2), ROUND(40 + RAND()*20,2), NULL, NULL, 952956, 71645, NULL, NULL, 'disk1', 'disk2', NULL, NULL);

INSERT INTO monitoring.server_info (server_id, server_ip, server_os, server_hostname, memory_total, purpose)
VALUES (21, '192.168.2.66', 'window', 'win', 8912, 'tt');
