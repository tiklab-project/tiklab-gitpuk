-- ---------------------------
-- 合并请求动态
-- ----------------------------
create table rpy_merge_condition(
     id                 varchar(12),
     user_id            varchar(12) NOT NULL,
     merge_request_id   varchar(12) NOT NULL,
     repository_id      varchar(12),
     type  varchar(18) not null,
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

ALTER TABLE rpy_merge_request ADD  parent_commit varchar(64);
ALTER TABLE rpy_merge_request ADD  current_commit varchar(64);

























































