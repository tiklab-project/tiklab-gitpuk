INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('repo_config', '仓库配置', 'repo_config', 'ssh', 4, 1);

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('integration', '集成开放', 'integration', 'service_integration,openapi', 5, 1);

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('repository', '仓库', 'repository', 'repository,repository_group', 10, 1);

---ssh密钥
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ssh', 'ssh密钥', 'ssh', NULL, 10, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ssh_add', '添加ssh密钥', 'ssh_add', 'ssh',1, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ssh_update', '修改ssh密钥', 'ssh_update', 'ssh',2, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ssh_delete', '删除ssh密钥', 'ssh_delete', 'ssh',3, '1');


-- 服务集成
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('service_integration', '服务集成', 'service_integration', NULL, 20, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('service_integration_add', '添加服务集成', 'service_integration_add', 'service_integration',1, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('service_integration_update', '修改服务集成', 'service_integration_update', 'service_integration',2, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('service_integration_delete', '删除服务集成', 'service_integration_delete', 'service_integration',3, '1');

-- 仓库
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository', '仓库', 'repository', NULL, 110, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_add', '添加仓库', 'repository_add', 'repository',1, '1');
-- 仓库组
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_group', '仓库组', 'repository_group', NULL, 111, '1');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_group_add', '添加仓库组', 'repository_group_add', 'repository_group',1, '1');

