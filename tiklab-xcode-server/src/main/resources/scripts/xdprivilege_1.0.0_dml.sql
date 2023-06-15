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



INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('81da97f94fa7', '1', '9633d9475886');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('2f8bdda13cdc', '1', '428be660dea3');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('ea9f15425a50', '1', '5fb7863b09a8');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('c6ecfb172276', '1', '57a3bcd1e5fe');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('3ea28f3075e1', '1', 'dd81bdbb52bc');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('f3707f5284c0', '1', '9c99b8a096c8');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('6c97b42e52c6', '1', 'e8bf9843bc9d');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('43eb9fa7cedd', '1', 'cb954a7c0be3');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('7435d22be935', '1', '325c2503007f');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('c8e0a257a0e8', '1', '6b61fbe5091a');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('060e1f75fd6f', '1', '043e412151db');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('7cd78017cea3', '1', '447d9998fc00');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('307e2e6fbae2', '1', '925371be8ec6');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('60b1f9be4fae', '1', '585d26bcbdf3');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('9c78b5a65a5f', '1', '890e7d41decf');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('b2bea716a4ca', '1', 'hf43e412151e');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('5965a1d48065', '1', 'hf43e412151e');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('ff961fcf2ebe', '1', '43e7d41decf7');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('5f5b01ed41ca', '1', 'oug5371be8ec');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('0295fd765def', '1', 'hfg5371be8ec');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('ec0f0c77f531', '1', '4235d2624bdf');
INSERT INTO "pcs_prc_role_function" ("id", "role_id", "function_id") VALUES ('4155cd684d3e', '1', 'wqre9998fc00');
