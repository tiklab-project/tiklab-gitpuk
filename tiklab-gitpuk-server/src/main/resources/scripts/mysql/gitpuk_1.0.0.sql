-- ---------------------------
-- lfs大文件
-- ----------------------------
create table rpy_lfs(
      id            varchar(12) PRIMARY KEY,
      repository_id varchar(12) NOT NULL,
      oid           varchar(326) NOT NULL,
      file_type     varchar (12),
      file_size     varchar(12),
      operator      varchar(32),
      is_delete     integer NOT NULL,
      create_time   timestamp,
      update_time   timestamp
);

-- ---------------------------
-- WebHook
-- ----------------------------
create table rpy_web_hook(
         id            varchar(12) PRIMARY KEY,
         repository_id    varchar(12) NOT NULL,
         name          varchar(12) NOT NULL,
         url           varchar(248) NOT NULL,
         secret_token  varchar(64) NOT NULL,
         events        varchar(248) NOT NULL,
         enable        integer,
         update_time   timestamp,
         create_time   timestamp
);

-- ---------------------------
-- fork
-- ----------------------------
create table rpy_repository_fork(
        id                      varchar(12) PRIMARY KEY,
        repository_id           varchar(12) NOT NULL,
        fork_repository_id      varchar(12) NOT NULL,
        group_id      varchar(12),
        user_id      varchar(12) NOT NULL,
        create_time             timestamp
);

-- ---------------------------
-- 系统集成地址
-- ----------------------------
create table rpy_integration_address(
        id                   varchar(12) PRIMARY KEY,
        code                 varchar(64) NOT NULL,
        integration_address     varchar(64) NOT NULL,
        auth_type   varchar(12)  NOT NULL,
        account     varchar(64) NOT NULL,
        password     varchar(64) NOT NULL,
        create_time          timestamp
);

-- ---------------------------
-- 集成关联表
-- ----------------------------
create table rpy_integration_relevancy(
      id                   varchar(12) PRIMARY KEY,
      repository_id        varchar(12) NOT NULL,
      relevancy_id         varchar(12) NOT NULL,
      type                 varchar(16),
      create_time          timestamp
);