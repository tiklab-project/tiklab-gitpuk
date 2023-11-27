
-- ---------------------------
-- 扫描方案的规则的关联表
-- ----------------------------
create table rpy_scan_scheme_rule(
      id          varchar(12) PRIMARY KEY,
      scan_scheme_id varchar(12) NOT NULL,
      scheme_ruleset_id  varchar(12) NOT NULL,
      rule_Id  varchar (12) NOT NULL,
      problem_level    integer,
      is_disable       integer,
      create_time  timestamp
);
-- ---------------------------
-- 扫描记录的明细
-- ----------------------------
create table rpy_scan_record_instance(
         id                 varchar(12) PRIMARY KEY,
         scan_record_id     varchar(12) NOT NULL,
         scan_play_id       varchar(12),
         file_name          varchar(248),
         file_path           varchar(248),
         problem_level      integer,
         problem_line       integer,
         rule_name          varchar(64),
         repair_overview    varchar(524),
         repair_desc   text,
         problem_overview   varchar(524),
         problem_desc       text,
         problem_state      varchar(12),
         import_user        varchar(32),
         import_time        timestamp,
         create_time        timestamp
);






















































