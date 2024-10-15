-- ---------------------------
-- 权限功能
-- ----------------------------
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('a701ee276b0f','创建仓库', 'gittok_rpy_add',null ,33, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('97127a0a4efe','创建仓库组', 'gittok_rpy_group_add',null ,34, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('6a9a8ea10b81','应用授权', 'gittok_visit_auth',null ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('9c5be1cb62bc','用户仓库管理', 'gittok_user_rpy',null ,35, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('3fc482ea196f','扫描方案管理', 'gittok_scan_scheme','gscan' ,36, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('e1a05662f507','扫描环境管理', 'gittok_scan_scheme','gscan' ,37, 1);

INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('6b5b36e659a8','仓库组设置', 'rpy_group',null ,38, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('f0449a54e22f','删除仓库组', 'rpy_group_delete','6b5b36e659a8' ,40, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('1df120ffaedc','更新仓库组', 'rpy_group_update','6b5b36e659a8' ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('f6aa8cb8bd88','代码扫描管理', 'rpy_scan_manage',null ,41, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('179c18d980e4','仓库组成员', 'rpy_group_user','6b5b36e659a8' ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('0a0bc014ab9c','仓库组权限', 'rpy_group_authority','6b5b36e659a8' ,42, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('7f5b12c75b48','仓库组创建仓库', 'rpy_group_rpy_add','6b5b36e659a8' ,43, 2);


-- ---------------------------
-- 项目权限
-- ----------------------------
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('8887d9d5ce91', '111111', 'a701ee276b0f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('f2b633864384', '111111', '97127a0a4efe');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('f2b5164ff384', '111111', '6a9a8ea10b81');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('12c121ff3142', '111111', '9c5be1cb62bc');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('f2b37851c1ag', '111111', '3fc482ea196f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('1255d1da2b63', '111111', 'e1a05662f507');

INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('940ec2fc645f', '1', 'a701ee276b0f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('6aaa2d0518f4', '1', '97127a0a4efe');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('4e731817b812', '1', 'e5b34be19fab');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('5c5d7ad7a07b', '1', '4cc4e67319a0');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('b91462fd1195', '1', 'cb6c8c3f4048');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('805a20b91e71', '1', '64bdf62686a4');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('b91d1z5fd115', '1', '6a9a8ea10b81');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('89c5z64zc9a1', '1', '9c5be1cb62bc');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('9244a5a9c9r9', '1', '3fc482ea196f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('8e9c8cv9da47', '1', 'e1a05662f507');

INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('731148c74848', '2', 'gssh');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('458c1f193b78', '2', 'a701ee276b0f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('6bbd478481a7', '2', '97127a0a4efe');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('cad7e80edb3f', '2', 'glog');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('9de44b35e97d', '2', 'gnewsway');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('3c4bbadd0c67', '2', 'gnewsscheme');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('c82d6485c1a0', '2', 'gresource');


-- ---------------------------
-- 仓库权限
-- ----------------------------
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('05f2c305a1b3', 'pro_111111', 'f0449a54e22f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('bef8672f384d', 'pro_111111', '1df120ffaedc');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('639deae7cfad', 'pro_111111', '6b5b36e659a8');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('b9080882d842', 'pro_111111', 'f6aa8cb8bd88');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('9d1das35a6de', 'pro_111111', '179c18d980e4');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('5ar3c4r1vq35', 'pro_111111', '0a0bc014ab9c');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('7f926d5c75b4', 'pro_111111', '7f5b12c75b48');

INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('11a2d6q15ac1', '3', 'f0449a54e22f');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('8ac5v8d1ad6f', '3', '1df120ffaedc');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('9afe81c8q1ca', '3', '6b5b36e659a8');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('6q1c7qc5d7g2', '3', 'f6aa8cb8bd88');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('962ca8wv13ew', '3', '179c18d980e4');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('36a1c5eq2acb', '3', '0a0bc014ab9c');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('9ac25aq69q2v', '3', '7f5b12c75b48');

