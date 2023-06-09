
-- ---------------------------
-- 代码扫描
-- @dsm.cmd.id="1001"
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
-- @dsm.cmd.id="1002"
-- ----------------------------
create table rpy_code_scan_instance(
        id              varchar(12) PRIMARY KEY ,
        repository_id   varchar (12) NOT NULL,
        task_name       varchar (64),
        run_state       varchar (12),
        log_address     varchar (255) NOT NULL,
        create_time     timestamp
);































































