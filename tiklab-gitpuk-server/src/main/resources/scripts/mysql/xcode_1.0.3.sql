-- ---------------------------
-- 仓库收藏
-- ----------------------------
create table rpy_repository_collect(
       id               varchar(12) ,
       repository_id         varchar(12) NOT NULL,
       user_id          varchar(12),
       create_time     timestamp
);
-- ---------------------------
-- 仓库分支
-- ----------------------------
create table rpy_repository_branch(
   id                   varchar(12) ,
   repository_id        varchar(12) NOT NULL,
   branch_name          varchar(64),
   branch_id            varchar(128),
   create_user          varchar(12),
   create_time          timestamp
);
-- ---------------------------
-- 合并请求审核人
-- ----------------------------
create table rpy_merge_auditor(
    id                 varchar(12),
    merge_request_id   varchar(12) NOT NULL,
    user_id            varchar(12) NOT NULL,
    repository_id      varchar(12),
    audit_status        integer,
    create_time timestamp
);
