-- ---------------------------
-- 仓库组表
-- ----------------------------
create table rpy_group(
    group_id             varchar(12) ,
    name                 varchar(256)  ,
    type                 int,
    create_time          varchar(256),
    user_id              varchar(12)  ,
    address              varchar(256) ,
    rules               VARCHAR(12),
    remarks           varchar(256)
);
-- ---------------------------
-- 仓库表
-- ----------------------------
create table rpy_repository(
    rpy_id               varchar(12),
    group_id             varchar(12) ,
    name                 varchar(255) ,
    address              varchar(255) ,
    create_time          varchar(255) ,
    type                 int ,
    user_id              varchar(12),
    remarks              varchar(255),
    language             varchar(255),
    rules                   VARCHAR(12),
    classify_state          VARCHAR(12),
    update_time           varchar(255),
    state                int
  
);
-- ---------------------------
-- 密钥表
-- ----------------------------
create table rpy_auth(
     auth_id              varchar(12),
     rpy_id              varchar(12) ,
     title                varchar(256),
     create_time          varchar(256),
     user_id              varchar(12) ,
     user_time            varchar(256),
     value                TEXT ,
     type                 int
);
-- ---------------------------
-- 代码扫描
-- ----------------------------
create table rpy_code_scan(
      id                    varchar(12) PRIMARY KEY ,
      task_name             varchar (255) NOT NULL,
      repository_id         varchar  (12) NOT NULL,
      deploy_env_id         varchar (12) ,
      deploy_server_id      varchar (12),
      scan_status           varchar (12),
      bugs                  varchar (8),
      code_smells           varchar (8),
      vulnerabilities       varchar (12),
      update_time           timestamp,
      create_time           timestamp
);

-- ---------------------------
-- 代码扫描日志
-- ----------------------------
create table rpy_code_scan_instance(
       id              varchar(12) PRIMARY KEY ,
       repository_id   varchar (12) NOT NULL,
       task_name       varchar (64),
       run_state       varchar (12),
       log_address     varchar (255) NOT NULL,
       create_time     timestamp
);

-- ---------------------------
-- 打开仓库记录表
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
-- ----------------------------
create table rpy_record_commit(
      id               varchar(12) ,
      repository_id    varchar(12)  ,
      user_id          varchar(12),
      commit_time    timestamp
);
-- ---------------------------
-- 环境配置
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
-- ----------------------------
create table rpy_deploy_server(
      id             varchar(12) PRIMARY KEY ,
      task_name     varchar (64) NOT NULL,
      server_name      varchar (32)  NOT NULL,
      auth_type    varchar (12)  NOT NULL,
      server_address     varchar(255) NOT NULL,
      user_name    varchar (32),
      pass_word    varchar (32),
      private_key  varchar (328),
      create_time    timestamp
);































































