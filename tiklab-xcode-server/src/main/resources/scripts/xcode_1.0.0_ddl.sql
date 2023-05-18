-- ---------------------------
-- 仓库组表
-- @dsm.cmd.id="1001"
-- ----------------------------
create table rpy_group(
    group_id             varchar(12) ,
    name                 varchar(256)  ,
    type                 int,
    create_time          varchar(256),
    user_id              varchar(12)  ,
    address              varchar(256) ,
    remarks           varchar(256)
);
-- ---------------------------
-- 仓库表
-- @dsm.cmd.id="1002"
-- ----------------------------
create table rpy_repository(
    rpy_id               varchar(12),
    group_id             varchar(12) ,
    name                 varchar(255) ,
    address              varchar(255) ,
    create_time          varchar(255) ,
    type                 int ,
    user_id              varchar(12),
    remarks           varchar(255),
    language             varchar(255),
    state                int
  
);
-- ---------------------------
-- 密钥表
-- @dsm.cmd.id="1003"
-- ----------------------------
create table rpy_auth(
     auth_id              varchar(12),
     code_id              varchar(12) ,
     title                varchar(256),
     create_time          varchar(256),
     user_id              varchar(12) ,
     user_time            varchar(256),
     value                TEXT ,
     type                 int
);



































































