-- ---------------------------
-- 打开仓库记录表
-- @dsm.cmd.id="1001"
-- ----------------------------
create table rpy_record_open(
    id               varchar(12) ,
    repository_id    varchar(12)  ,
    user_id          varchar(12),
    new_open_time    timestamp,
    create_time      timestamp
);

-- ---------------------------
-- 仓库提交记录表
-- @dsm.cmd.id="1002"
-- ----------------------------
create table rpy_record_commit(
    id               varchar(12) ,
    repository_id    varchar(12)  ,
    user_id          varchar(12),
    commit_time    timestamp
);
-- ---------------------------
-- 供应管理地址
-- @dsm.cmd.id="1003"
-- ----------------------------
create table rpy_scm_address(
      id             varchar(12) PRIMARY KEY ,
      scm_type       varchar (12) NOT NULL,
      scm_name       varchar(32) NOT NULL,
      scm_address    varchar (32) NOT NULL,
      create_time    timestamp
);

-- ---------------------------
-- 第三方认证管理
-- @dsm.cmd.id="1003"
-- ----------------------------
create table rpy_auth_third(
    id             varchar(12) PRIMARY KEY ,
    auth_name      varchar (32)  NOT NULL,
    auth_type       varchar (12) NOT NULL,
    auth_server     varchar (12)  NOT NULL,
    server_address     varchar(64) NOT NULL,
    maven_address    varchar (64)  NOT NULL,
    user_name    varchar (32),
    pass_word    varchar (32),
    private_key  varchar (328),
    create_time    timestamp
);


































































