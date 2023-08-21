-- ---------------------------
-- 导入第三方仓库的认证
-- ----------------------------
create table rpy_lead_auth(
    id  varchar(12) PRIMARY KEY,
    address varchar(128),
    access_token varchar(128),
    type varchar(32) NOT NULL,
    account varchar(64),
    password varchar(64),
    user_id varchar (12),
    create_time timestamp
);
-- ---------------------------
-- 导入第三方仓库的记录
-- ----------------------------
create table rpy_lead_record(
    id  varchar(12) PRIMARY KEY,
    rpy_id varchar(128) NOT NULL,
    lead_way varchar(32) NOT NULL,
    relevance_id varchar (32),
    lead_state  varchar(12),
    create_time timestamp
);




























































