-- ---------------------------
-- 远程仓库信息
-- ----------------------------
create table rpy_import_auth(
    id  varchar(12) PRIMARY KEY,
    address varchar(128),
    access_token varchar(128),
    type varchar(32) NOT NULL,
    account varchar(64),
    password varchar(64),
    user_id varchar (12),
    create_time timestamp
);




























































