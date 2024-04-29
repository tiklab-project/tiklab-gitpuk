INSERT INTO pcs_prc_role VALUES ('1', '管理员角色', NULL, 'system', '1', 1, 0, 1);
INSERT INTO pcs_prc_role VALUES ('2', '普通角色', NULL, 'system', '1', 0, 1, 1);
INSERT INTO pcs_prc_role VALUES ('3', '项目管理员', NULL, 'system', '2', 1, 0, 1);
INSERT INTO pcs_prc_role VALUES ('4', '项目成员', NULL, 'system', '2', 0, 1, 1);

-- ---------------------------
-- 权限功能
-- ----------------------------
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES
('gauthority','权限', 'gittok_authority',null ,1, 1),
('gnewsscheme','消息通知方案', 'gittok_news_scheme',null ,1, 1),
('gnewsway','消息通知方式', 'gittok_news_way',null ,1, 1),
('gplugin','插件', 'gittok_plugin',null ,1, 1),
('gresource','资源监控', 'gittok_resource',null ,1, 1),
('gssh','ssh密钥', 'gittok_ssh',null ,1, 1),
('gscan','代码扫描', 'gittok_scan',null ,1, 1),
('glog','操作日志', 'gittok_log',null ,1, 1),
('gbackups','备份与恢复项目', 'gittok_backups',null ,1, 1),

('rpyauthority','仓库权限', 'rpy_authority',null,1, 2),
('rpysetting','仓库设置', 'rpy_setting',null ,1, 2),
('rpyupdate','修改仓库', 'rpy_update','rpysetting' ,3, 2),
('rpydelete','删除仓库', 'rpy_delete','rpysetting' ,2, 2),
('rpyuser','仓库成员', 'rpy_user',null ,6, 2),
('rpyclean','仓库清理', 'rpy_clean',null ,6, 2),
('rpymirror','仓库镜像', 'rpy_mirror',null ,2, 2);



-- ---------------------------
-- 角色权限功能
-- ----------------------------
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES
('81da97f94fa7', '1', '9633d9475886'),('2f8bdda13cdc', '1', '428be660dea3'),
('ea9f15425a50', '1', '5fb7863b09a8'),('c6ecfb172276', '1', '57a3bcd1e5fe'),
('3ea28f3075e1', '1', 'dd81bdbb52bc'),('f3707f5284c0', '1', '9c99b8a096c8'),
('6c97b42e52c6', '1', 'e8bf9843bc9d'),('43eb9fa7cedd', '1', 'cb954a7c0be3'),
('7435d22be935', '1', '325c2503007f'),('c8e0a257a0e8', '1', '6b61fbe5091a'),
('060e1f75fd6f', '1', '043e412151db'),('7cd78017cea3', '1', '447d9998fc00'),
('307e2e6fbae2', '1', '925371be8ec6'),('60b1f9be4fae', '1', '585d26bcbdf3'),
('9c78b5a65a5f', '1', '890e7d41decf'),('b2bea716a4ca', '1', 'hf43e412151e'),
('ff961fcf2ebe', '1', '43e7d41decf7'),('5f5b01ed41ca', '1', 'oug5371be8ec'),
('0295fd765def', '1', 'hfg5371be8ec'),('ec0f0c77f531', '1', '4235d2624bdf'),
('4155cd684d3e', '1', 'wqre9998fc00'),('e1bdf38322c8', '1', '64bdf62686a4'),

('00e840ea5302', '1', 'gauthority'),('05d66918b2dd', '1', 'gnewsscheme'),
('064d1a5ddbc5', '1', 'gnewsway'),('0fd56de07eaf', '1', 'gplugin'),
('1134dbdbb6d0', '1', 'gresource'),('143e6010ba4b', '1', 'gssh'),
('256bca68cd16', '1', 'gscan'),('39f06b018e83', '1', 'glog'),
('7464415279eb', '1', 'gbackups'),('74217ab2e9bb', '3', 'rpyauthority'),
('0230645439302', '3', 'rpysetting'),('74217ab2e99', '3', 'rpyupdate'),
('0230645439000', '3', 'rpydelete'),('74217ab2e001', '3', 'rpyuser'),
('0230645439002', '3', 'rpyclean'),('74217ab2e003', '3', 'rpymirror'),
('74217ab2e004', '3', 'rpyauthority');

