-- ---------------------------
-- 合并请求动态
-- ----------------------------
create table rpy_merge_condition(
     id                 varchar(12),
     user_id            varchar(12) NOT NULL,
     merge_request_id   varchar(12) NOT NULL,
     repository_id      varchar(12),
     type  varchar(32) not null,
     data  varchar(128),
     create_time timestamp
);

-- ---------------------------
-- 合并请求动态的评论
-- ----------------------------
create table rpy_merge_comment(
    id                 varchar(12),
    merge_condition_id varchar(12) NOT NULL,
    merge_request_id   varchar(12) NOT NULL,
    repository_id      varchar(12),
    comment_user_id      varchar(12),
    reply_user_id         varchar(12),
    data  text,
    create_time timestamp
);
-- ---------------------------
-- 合并完成后的差异commitId
-- ----------------------------
create table rpy_merge_commit(
     id                 varchar(12),
     merge_request_id   varchar(12) NOT NULL,
     repository_id      varchar(12),
     commit_time      timestamp(32),
     commit_id   varchar(128) NOT NULL,
     create_time timestamp
);
