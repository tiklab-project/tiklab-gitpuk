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
    remarks           varchar(256),
    color        int
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
    state                int,
    category              int,
    size            bigint,
    color           int
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

-- ---------------------------
-- 定时任务
-- ----------------------------
create table rpy_time_task(
      id            varchar(12) PRIMARY KEY,
      scan_play_id       varchar (12),
      task_name          varchar(524),
      task_type     varchar(32),
      task_way      int,
      exec_state     int,
      create_time   timestamp,
      update_time  timestamp
);

-- ---------------------------
-- 定时任务 的实例
-- ----------------------------
create table rpy_time_task_instance(
       id          varchar(12) PRIMARY KEY,
       time_task_id varchar (12),
       exec_object_id varchar(12),
       task_way      int,
       cron varchar(255) ,
       week_day int,
       exec_state     varchar(32),
       exec_time  varchar(12),
       create_time  timestamp
);






























































