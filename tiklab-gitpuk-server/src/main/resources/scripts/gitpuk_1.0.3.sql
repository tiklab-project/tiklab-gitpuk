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
        create_time          timestamp
);