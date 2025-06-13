-- ---------------------------
-- lfs大文件
-- ----------------------------
create table rpy_lfs(
      id            varchar(12) PRIMARY KEY,
      repository_id varchar(12) NOT NULL,
      oid           varchar(326) NOT NULL,
      file_type     varchar (12),
      file_size     varchar(12),
      operator      varchar(32),
      is_delete     integer NOT NULL,
      create_time   timestamp,
      update_time   timestamp
);
