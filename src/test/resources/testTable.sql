-- 테이블이 이미 존재하는 경우 삭제
DROP TABLE IF EXISTS board;

-- board 테이블 생성
CREATE TABLE board (
                       id       bigint auto_increment primary key,
                       title    varchar(50) charset utf8mb4 not null,
                       content  varchar(5000) charset utf8mb4 not null,
                       reg_time datetime default current_timestamp() not null
)