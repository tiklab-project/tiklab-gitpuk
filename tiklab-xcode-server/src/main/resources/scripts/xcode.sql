create table code_group(
    group_id             varchar(256)  comment '仓库组id',
    name                 varchar(256)  comment '名称',
    type                 int  comment '类型',
    create_time          varchar(256)  comment '创建时间',
    user_id              varchar(256)  comment '创建人',
    address              varchar(256)  comment '仓库地址',
    remarks           varchar(256)  comment '描述',
    PRIMARY KEY (group_id) USING BTREE
);

create table code(
    code_id                   varchar(255)  comment '仓库id',
    group_id             varchar(256)  comment '仓库组id',
    name                 varchar(255)  comment '仓库名称',
    address              varchar(255)  comment '仓库地址',
    create_time           varchar(255)  comment '创建时间',
    type                 int  comment '类型',
    user_id              varchar(255)  comment '创建人',
    remarks           varchar(255)  comment '描述',
    language             varchar(255)  comment '语言',
    state                int  comment '仓库状态',
    PRIMARY KEY (code_id) USING BTREE
);

create table code_branch(
    branch_id            varchar(255)  comment '分支id',
    code_id              varchar(255)  comment '仓库id',
    user_id              varchar(255)  comment '创建人',
    branch_name          varchar(255)  comment '分支名称',
    type                 int  comment '分支类型',
    remarks            int  comment '描述',
    create_time          varchar(255)  comment '创建时间',
    state                int  comment '分支状态',
    PRIMARY KEY (branch_id) USING BTREE
);

create table code_commit(
    commit_id            varchar(256)  comment '提交id',
    branch_id            varchar(255)  comment '分支id',
    time                 varchar(256)  comment '提交时间',
    message              varchar(256)  comment '提交信息',
    user_id              varchar(256)  comment '提交人',
    git_id               varchar(256)  comment '提交git_id',
    primary key (commit_id) USING BTREE
);


create table code_label(
    label_id             varchar(256)  comment '标签id',
    branch_id            varchar(255)  comment '分支id',
    title                varchar(256)  comment '标签标题',
    remarks           varchar(256)  comment '描述',
    create_time          varchar(256)  comment '创建时间',
    user_id              varchar(256)  comment '创建人',
    primary key (label_id) USING BTREE
);

create table code_release(
    release_id           varchar(255)  comment '发行版id',
    label_id             varchar(256)  comment '标签id',
    title                varchar(255)  comment '发行版标题',
    message              varchar(255)  comment '发行信息',
    user_id              varchar(256)  comment '创建人',
    primary key (release_id) USING BTREE
);

create table code_merge(
    merge_id             varchar(256)  comment '合并id',
    code_id              varchar(255)  comment '仓库id',
    create_time          varchar(255)  comment '创建时间',
    title                varchar(256)  comment '合并标题',
    source_id            varchar(256)  comment '原分支',
    target_id            varchar(256)  comment '合并分支',
    time                 varchar(256)  comment '合并时间',
    state                int  comment '合并状态',
    remarks           varchar(256)  comment '描述',
    primary key (merge_id) USING BTREE
);

create table code_review
(
    review_id            varchar(256)  comment '评审id',
    merge_id             varchar(256)  comment '合并id',
    user_id              varchar(256)  comment '评审人',
    state                int  comment '评审状态',
    message              varchar(256)  comment '评审信息',
    primary key (review_id) USING BTREE
);




































































