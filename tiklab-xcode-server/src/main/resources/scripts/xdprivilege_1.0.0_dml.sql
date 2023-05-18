INSERT INTO pcs_prc_role VALUES ('1', '管理员角色', NULL, 'system', '1', 1, 1, 1);
INSERT INTO pcs_prc_role VALUES ('2', '普通角色', NULL, 'system', '1', 1, 0, 0);
INSERT INTO pcs_prc_role VALUES ('3', '项目管理员', NULL, 'system', '2', 1, 1, 1);
INSERT INTO pcs_prc_role VALUES ('4', '项目普通角色', NULL, 'system', '2', 1, 0, 0);


INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('permission','权限', 'xcode_permission',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('f6f51f944133','消息通知类型', 'message_type',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('7d69fed448ee','消息管理', 'message_setting',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('f79c084575fa','版本许可证', 'pipeline_version',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('plugin','插件', 'xcode_plugin',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('c8774229c6b8','操作日志', 'xcode_log',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectall','项目设置', 'xcode_setting',null ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectuser','项目成员', 'xcode_user',null ,6, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectdele','删除项目', 'xcode_delete','projectall' ,2, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('projectup','修改项目', 'xcode_update','projectall' ,3, 2);
