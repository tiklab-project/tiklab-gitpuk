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
-- 环境配置
-- @dsm.cmd.id="1003"
-- ----------------------------
create table rpy_deploy_env(
      id             varchar(12) PRIMARY KEY ,
      env_type       varchar (32) NOT NULL,
      env_name       varchar(128) NOT NULL,
      env_address    varchar (255) NOT NULL,
      create_time    timestamp
);

-- ---------------------------
-- 服务配置
-- @dsm.cmd.id="1004"
-- ----------------------------
create table rpy_deploy_server(
    id             varchar(12) PRIMARY KEY ,
    task_name     varchar (64) NOT NULL,
    server_name      varchar (32)  NOT NULL,
    server_type       varchar (12) NOT NULL,
    auth_type    varchar (12)  NOT NULL,
    server_address     varchar(255) NOT NULL,
    user_name    varchar (32),
    pass_word    varchar (32),
    private_key  varchar (328),
    create_time    timestamp
);































































