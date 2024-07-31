INSERT INTO server_info (server_id, server_os, server_hostname, memory_total, purpose, server_ip)
VALUES
    (1, 'Ubuntu 20.04', 'server1', 16384, 'Web Server', '192.168.1.1'),
    (2, 'Windows Server 2019', 'server2', 32768, 'Database Server', '192.168.1.2'),
    (3, 'CentOS 7', 'server3', 8192, 'File Server', '192.168.1.3'),
    (4, 'Debian 10', 'server4', 20480, 'Application Server', '192.168.1.4'),
    (5, 'Ubuntu 20.04', 'server5', 16384, 'Backup Server', '192.168.1.5'),
    (6, 'Windows Server 2019', 'server6', 32768, 'Mail Server', '192.168.1.6'),
    (7, 'CentOS 7', 'server7', 8192, 'Proxy Server', '192.168.1.7'),
    (8, 'Debian 10', 'server8', 20480, 'Monitoring Server', '192.168.1.8'),
    (9, 'Ubuntu 20.04', 'server9', 16384, 'DNS Server', '192.168.1.9'),
    (10, 'Windows Server 2019', 'server10', 32768, 'ERP Server', '192.168.1.10');

INSERT INTO disk (disk_id, created_date, disk_server_info_fk, disk_name)
VALUES
-- For server_info 1
(1, '2024-07-01 00:00:00', 1, 'disk1'),
(2, '2024-07-01 00:00:00', 1, 'disk2'),

-- For server_info 2
(3, '2024-07-01 00:00:00', 2, 'disk1'),
(4, '2024-07-01 00:00:00', 2, 'disk2'),
(5, '2024-07-01 00:00:00', 2, 'disk3'),

-- For server_info 3
(6, '2024-07-01 00:00:00', 3, 'disk1'),

-- For server_info 4
(7, '2024-07-01 00:00:00', 4, 'disk1'),
(8, '2024-07-01 00:00:00', 4, 'disk2'),

-- For server_info 5
(9, '2024-07-01 00:00:00', 5, 'disk1'),
(10, '2024-07-01 00:00:00', 5, 'disk2'),
(11, '2024-07-01 00:00:00', 5, 'disk3'),

-- For server_info 6
(12, '2024-07-01 00:00:00', 6, 'disk1'),

-- For server_info 7
(13, '2024-07-01 00:00:00', 7, 'disk1'),
(14, '2024-07-01 00:00:00', 7, 'disk2'),

-- For server_info 8
(15, '2024-07-01 00:00:00', 8, 'disk1'),
(16, '2024-07-01 00:00:00', 8, 'disk2'),
(17, '2024-07-01 00:00:00', 8, 'disk3'),

-- For server_info 9
(18, '2024-07-01 00:00:00', 9, 'disk1'),

-- For server_info 10
(19, '2024-07-01 00:00:00', 10, 'disk1'),
(20, '2024-07-01 00:00:00', 10, 'disk2');

INSERT INTO metric_log (
    log_id, cpu_usage, memory_usage, created_date, server_metric_fk,
    disk_usage1, disk_usage2, disk_usage3, disk_usage4,
    disk_total1, disk_total2, disk_total3, disk_total4,
    disk_name1, disk_name2, disk_name3, disk_name4
) VALUES
-- For server_info 1
(1, 75.5, 50.2, NOW() - INTERVAL 3 MINUTE, 1, 20.3, 30.7, 40.4, 10.9, 100.5, 200.6, 3007, 4008, 'disk1', 'disk2', 'disk3', 'disk4'),
(2, 80.8, 55.6, NOW() - INTERVAL 2 MINUTE, 1, 25.4, 35.3, 45.1, 11.8, 110.1, 210.2, 3103, 4104, 'disk1', 'disk2', 'disk3', 'disk4'),
(3, 85.1, 60.7, NOW() - INTERVAL 1 MINUTE, 1, 22.5, 32.8, 42.9, 12.3, 120.5, 220.6, 3207, 4208, 'disk1', 'disk2', 'disk3', 'disk4'),
(4, 70.3, 65.4, NOW(), 1, 24.2, 34.5, 44.6, 13.7, 130.9, 230.0, 3301, 4302, 'disk1', 'disk2', 'disk3', 'disk4'),

-- For server_info 2
(5, 65.4, 70.5, NOW() - INTERVAL 3 MINUTE, 2, 21.8, NULL, NULL, NULL, 140.3, NULL, NULL, NULL, 'disk1', NULL, NULL, NULL),
(6, 90.7, 75.1, NOW() - INTERVAL 2 MINUTE, 2, 23.6, NULL, NULL, NULL, 150.7, NULL, NULL, NULL, 'disk1', NULL, NULL, NULL),
(7, 75.2, 50.3, NOW() - INTERVAL 1 MINUTE, 2, 20.7, NULL, NULL, NULL, 160.1, NULL, NULL, NULL, 'disk1', NULL, NULL, NULL),
(8, 80.4, 55.9, NOW(), 2, 25.5, NULL, NULL, NULL, 170.5, NULL, NULL, NULL, 'disk1', NULL, NULL, NULL),

-- For server_info 3
(9, 85.3, 60.8, NOW() - INTERVAL 3 MINUTE, 3, 22.9, 32.7, NULL, NULL, 180.9, 280.0, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
(10, 70.6, 65.3, NOW() - INTERVAL 2 MINUTE, 3, 24.5, 34.8, NULL, NULL, 190.3, 290.4, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
(11, 65.1, 70.4, NOW() - INTERVAL 1 MINUTE, 3, 21.9, 31.2, NULL, NULL, 200.7, 300.8, NULL, NULL, 'disk1', 'disk2', NULL, NULL),
(12, 90.8, 75.2, NOW(), 3, 23.7, 33.1, NULL, NULL, 210.1, 310.2, NULL, NULL, 'disk1', 'disk2', NULL, NULL),

-- For server_info 4
(13, 75.9, 50.7, NOW() - INTERVAL 3 MINUTE, 4, 20.4, 30.6, 40.5, 50.8, 220.5, 320.6, 4217, 5228, 'disk1', 'disk2', 'disk3', 'disk4'),
(14, 80.1, 55.3, NOW() - INTERVAL 2 MINUTE, 4, 25.2, 35.7, 45.8, 55.3, 230.9, 330.0, 4321, 5332, 'disk1', 'disk2', 'disk3', 'disk4'),
(15, 85.4, 60.9, NOW() - INTERVAL 1 MINUTE, 4, 22.6, 32.3, 42.1, 52.6, 240.3, 340.4, 4425, 5436, 'disk1', 'disk2', 'disk3', 'disk4'),
(16, 70.8, 65.5, NOW(), 4, 24.1, 34.4, 44.9, 54.7, 250.7, 350.8, 4529, 5540, 'disk1', 'disk2', 'disk3', 'disk4');