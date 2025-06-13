-- ---------------------------
-- 密钥ssh表
-- ----------------------------
create table rpy_auth_ssh(
     id                 varchar(12),
     rpy_id              varchar(12) ,
     title                varchar(256),
     create_time          timestamp,
     user_id              varchar(12) ,
     user_time            timestamp,
     value                text,
     type                 varchar(12),
     modulus text
);

-- ---------------------------
-- 合并请求
-- ----------------------------
create table rpy_merge_request(
     id                 varchar(12),
     rpy_id             varchar(12) NOT NULL,
     title              varchar(256) NOT NULL,
     merge_origin       varchar(32) NOT NULL,
     merge_target       varchar(32) NOT NULL,
     create_user        varchar(12),
     merge_state       integer,
     is_clash           integer,
     value               TEXT,
     create_time         timestamp
);
