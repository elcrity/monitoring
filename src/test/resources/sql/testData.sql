-- Insert data into server_info
INSERT INTO server_info (server_os, server_hostname, memory_total, purpose, server_ip)
VALUES
    ('Linux', 'server1.example.com', 16384, 'Web Server', '192.168.1.1'),
    ('Windows', 'server2.example.com', 32768, 'Database Server', '192.168.1.2'),
    ('Linux', 'server3.example.com', 8192, 'File Server', '192.168.1.3'),
    ('Windows', 'server4.example.com', 16384, 'Application Server', '192.168.1.4'),
    ('Linux', 'server5.example.com', 65536, 'Backup Server', '192.168.1.5'),
    ('Windows', 'server6.example.com', 32768, 'Email Server', '192.168.1.6'),
    ('Linux', 'server7.example.com', 8192, 'DNS Server', '192.168.1.7'),
    ('Windows', 'server8.example.com', 16384, 'FTP Server', '192.168.1.8'),
    ('Linux', 'server9.example.com', 32768, 'Proxy Server', '192.168.1.9'),
    ('Windows', 'server10.example.com', 65536, 'Domain Controller', '192.168.1.10');

-- Insert data into disk
INSERT INTO disk (disk_server_info_fk, disk_name, disk_total)
VALUES
    (1, 'Disk A1', 50000),
    (1, 'Disk B1', 60000),
    (2, 'Disk C1', 70000),
    (2, 'Disk D1', 80000),
    (3, 'Disk E1', 90000),
    (3, 'Disk F1', 100000),
    (4, 'Disk G1', 110000),
    (4, 'Disk H1', 120000),
    (5, 'Disk I1', 130000),
    (5, 'Disk J1', 140000);

-- Insert data into metric_log
INSERT INTO metric_log (cpu_usage, memory_usage, created_date, server_metric_fk)
VALUES
    (25.0, 55.0, NOW(), 1),
    (30.0, 60.0, NOW() - INTERVAL 1 MINUTE, 1),
    (35.0, 65.0, NOW(), 2),
    (40.0, 70.0, NOW() - INTERVAL 1 MINUTE, 2),
    (45.0, 75.0, NOW(), 3),
    (50.0, 80.0, NOW() - INTERVAL 1 MINUTE, 3),
    (55.0, 85.0, NOW(), 4),
    (60.0, 90.0, NOW() - INTERVAL 1 MINUTE, 4),
    (65.0, 95.0, NOW(), 5),
    (70.0, 100.0, NOW() - INTERVAL 1 MINUTE, 5),
    (75.0, 105.0, NOW(), 6),
    (80.0, 110.0, NOW() - INTERVAL 1 MINUTE, 6),
    (85.0, 115.0, NOW(), 7),
    (90.0, 120.0, NOW() - INTERVAL 1 MINUTE, 7),
    (95.0, 125.0, NOW(), 8),
    (100.0, 130.0, NOW() - INTERVAL 1 MINUTE, 8),
    (105.0, 135.0, NOW(), 9),
    (110.0, 140.0, NOW() - INTERVAL 1 MINUTE, 9),
    (115.0, 145.0, NOW(), 10),
    (120.0, 150.0, NOW() - INTERVAL 1 MINUTE, 10);

-- Insert data into disk_log
INSERT INTO disk_log (disk_usage, disk_disk_log_fk,created_date)
VALUES
    (10.5, 1, NOW() - INTERVAL 1 MINUTE),
    (11.0, 1, NOW()),
    (11.2, 2, NOW() - INTERVAL 1 MINUTE),
    (11.7, 2, NOW()),
    (12.3, 3, NOW() - INTERVAL 1 MINUTE),
    (12.8, 3, NOW()),
    (13.0, 4, NOW() - INTERVAL 1 MINUTE),
    (13.5, 4, NOW()),
    (14.0, 5, NOW() - INTERVAL 1 MINUTE),
    (14.5, 5, NOW());
