
-- ---------------------------
-- 代码扫描
-- @dsm.cmd.id="1001"
-- ----------------------------
create table rpy_code_scan(
          id             varchar(12) PRIMARY KEY ,
          task_name          varchar (255) NOT NULL,
          repository_id      varchar  (12) NOT NULL,
          deploy_env_id       varchar (12) ,
          deploy_server_id      varchar (12),
          scan_status       varchar (12),
          bugs              varchar (8),
          code_smells       varchar (8),
          vulnerabilities   varchar (12),
          update_time    timestamp,
          create_time    timestamp
);
































































