-- ---------------------------
-- fork
-- ----------------------------
create table rpy_repository_fork(
        id                      varchar(12) PRIMARY KEY,
        repository_id           varchar(12) NOT NULL,
        fork_repository_id      varchar(12) NOT NULL,
        group_id      varchar(12),
        user_id      varchar(12) NOT NULL,
        create_time             timestamp
);