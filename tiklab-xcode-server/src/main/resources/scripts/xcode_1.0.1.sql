-- ---------------------------
-- 远程仓库信息
-- ----------------------------
create table rpy_remote_info(
    id                  varchar(12) PRIMARY KEY,
    rpy_id              varchar (12) NOT NULL,
    name                varchar (64),
    address             varchar(256)  NOT NULL,
    auth_way            varchar (12) NOT NULL,
    account             varchar (32),
    password            varchar (32),
    secret_key          varchar (64),
    timed_state         INT,
    create_time          timestamp
);




























































