-- ---------------------------
-- 仓库组表
-- @dsm.cmd.id="1001"
-- ----------------------------
create table rpy_group(
    group_id             varchar(12)  comment '仓库组id',
    name                 varchar(256)  comment '名称',
    type                 int  comment '类型',
    create_time          varchar(256)  comment '创建时间',
    user_id              varchar(12)  comment '创建人',
    address              varchar(256)  comment '仓库地址',
    remarks           varchar(256)  comment '描述',
    PRIMARY KEY (group_id) USING BTREE
);
-- ---------------------------
-- 仓库表
-- @dsm.cmd.id="1002"
-- ----------------------------
create table rpy_repository(
    rpy_id               varchar(12)  comment '仓库id',
    group_id             varchar(12)  comment '仓库组id',
    name                 varchar(255)  comment '仓库名称',
    address              varchar(255)  comment '仓库地址',
    create_time          varchar(255)  comment '创建时间',
    type                 int  comment '类型',
    user_id              varchar(12)  comment '创建人',
    remarks           varchar(255)  comment '描述',
    language             varchar(255)  comment '语言',
    state                int  comment '仓库状态',
    PRIMARY KEY (rpy_id) USING BTREE
);
-- ---------------------------
-- 密钥表
-- @dsm.cmd.id="1003"
-- ----------------------------
create table rpy_auth(
     auth_id              varchar(12)  comment '秘钥id',
     code_id              varchar(12)  comment '仓库id',
     title                varchar(256)  comment '标题',
     create_time          varchar(256)  comment '创建时间',
     user_id              varchar(12)  comment '创建人',
     user_time            varchar(256),
     value                longtext  comment '秘钥内容',
     type                 int  comment '秘钥类型',
     primary key (auth_id) USING BTREE
);



































































