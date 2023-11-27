-- ---------------------------
-- 扫描计划
-- ----------------------------
create table rpy_scan_play(
      id          varchar(12) PRIMARY KEY,
      play_name  varchar (128) NOT NULL,
      repository_id varchar(12) NOT NULL,
      branch  varchar(32) NOT NULL,
      scan_scheme_id varchar(12) NOT NULL,
      scan_time timestamp,
      create_time  timestamp
);
-- ---------------------------
-- 扫描记录
-- ----------------------------
create table rpy_scan_record(
        id          varchar(12) PRIMARY KEY,
        scan_play_id         varchar(12) NOT NULL,
        scan_object           varchar (64),
        repository_id         varchar  (12) NOT NULL,
        scan_user_id           varchar (12),
        scan_result           varchar (12),
        scan_way                varchar (32),
        all_trouble         integer,
        severity_trouble      integer,
        error_trouble         integer,
        notice_trouble           integer,
        suggest_trouble         integer,
        create_time  timestamp
);

-- ---------------------------
-- 扫描问题列表
-- ----------------------------
create table rpy_scan_issues(
    id                 varchar(12) PRIMARY KEY,
    scan_record_id     varchar(12) NOT NULL,
    issues_severity    varchar(12),
    scan_issues_key    varchar(32),
    file_name          varchar(248),
    rule_name          varchar(12),
    lead_in_time       timestamp,
    issues_line        integer,
    issues_message     text,
    create_time        timestamp
);


-- ---------------------------
-- 扫描方案
-- ----------------------------
create table rpy_scan_scheme(
    id          varchar(12) PRIMARY KEY,
    scheme_name  varchar (128) NOT NULL,
    language     varchar(12),
    scan_way    varchar(12),
    describe         varchar (246),
    create_time  timestamp
);

-- ---------------------------
-- 扫描方案规则关系
-- ----------------------------
create table rpy_scan_scheme_ruleset(
        id          varchar(12) PRIMARY KEY,
        scan_scheme_id  varchar (12) NOT NULL,
        rule_set_id     varchar(12),
        language        varchar(12),
        create_time  timestamp
);
-- ---------------------------
-- 扫描方案sonar关系
-- ----------------------------
create table rpy_scan_scheme_sonar(
     id          varchar(12) PRIMARY KEY,
     scan_scheme_id  varchar (12) NOT NULL,
     deploy_env_id     varchar(12) NOT NULL,
     deploy_server      varchar(12) NOT NULL,
     create_time  timestamp
);

-- ---------------------------
-- 扫描规则集
-- ----------------------------
create table rpy_scan_rule_set(
      id          varchar(12) PRIMARY KEY,
      rule_set_name  varchar (128) NOT NULL,
      rule_set_type  varchar(12),
      describe       text,
      language     varchar(12),
      create_time  timestamp
);
-- ---------------------------
-- 扫描规则
-- ----------------------------
create table rpy_scan_rule(
      id          varchar(12) PRIMARY KEY,
      rule_set_id  varchar(12) NOT NULL,
      rule_name  varchar (128) NOT NULL,
      scan_tool varchar(32),
      rule_overview varchar(528),
      problem_level    integer,
      create_time  timestamp,
      describe       text
);






















































