
create table rpy_auth(
    auth_id              varchar(256)  comment '秘钥id',
    code_id              varchar(255)  comment '仓库id',
    title                varchar(256)  comment '标题',
    create_time          varchar(256)  comment '创建时间',
    user_id              varchar(256)  comment '创建人',
    value                longtext  comment '秘钥内容',
    type                 int  comment '秘钥类型',
    primary key (auth_id) USING BTREE
);
