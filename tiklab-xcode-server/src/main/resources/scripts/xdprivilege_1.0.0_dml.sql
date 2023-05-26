INSERT INTO pcs_prc_role VALUES ('1', '管理员角色', NULL, 'system', '1', 1, 1, 1);
INSERT INTO pcs_prc_role VALUES ('2', '普通角色', NULL, 'system', '1', 1, 0, 0);
INSERT INTO pcs_prc_role VALUES ('3', '项目管理员', NULL, 'system', '2', 1, 1, 1);
INSERT INTO pcs_prc_role VALUES ('4', '项目普通角色', NULL, 'system', '2', 1, 0, 0);


INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('permission','权限', 'xcode_permission',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('messtype','消息通知类型', 'message_type',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('messmanage','消息管理', 'message_setting',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('version','版本许可证', 'xcode_version',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('plugin','插件', 'xcode_plugin',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('loginexce','操作日志', 'xcode_log',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectall','项目设置', 'xcode_setting',null ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectuser','项目成员', 'xcode_user',null ,6, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectdele','删除项目', 'xcode_delete','projectall' ,2, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectup','修改项目', 'xcode_update','projectall' ,3, 2);


INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('00e840ea5302', '1', 'permission');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('05d66918b2dd', '1', 'messtype');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('064d1a5ddbc5', '1', 'messmanage');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('0fd56de07eaf', '1', 'version');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('1134dbdbb6d0', '1', 'plugin');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('143e6010ba4b', '1', 'loginexce');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('256bca68cd16', '1', 'projectall');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('28b4ec49b63c', '1', 'projectuser');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('3572dd063f4f', '1', 'projectdele');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('39f06b018e83', '1', 'projectup');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('732aa5077352', '2', 'version');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('74217ab2e9eb', '2', 'loginexce');
