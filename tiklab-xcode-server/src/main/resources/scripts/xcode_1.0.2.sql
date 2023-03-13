
alter table code rename as rpy_repository;
alter table code_group rename as rpy_group;

ALTER TABLE rpy_repository CHANGE code_id rpy_id VARCHAR(256);

ALTER TABLE rpy_auth CHANGE code_id rpy_id VARCHAR(256) DEFAULT NULL;
