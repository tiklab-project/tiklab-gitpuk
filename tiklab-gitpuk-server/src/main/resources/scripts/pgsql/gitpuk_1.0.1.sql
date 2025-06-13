-- ---------------------------
-- WebHook
-- ----------------------------
create table rpy_web_hook(
        id            varchar(12) PRIMARY KEY,
        repository_id    varchar(12) NOT NULL,
        name          varchar(12) NOT NULL,
        url           varchar(248) NOT NULL,
        secret_token  varchar(64) NOT NULL,
        events        varchar(248) NOT NULL,
        enable        integer,
        update_time   timestamp,
        create_time   timestamp
);