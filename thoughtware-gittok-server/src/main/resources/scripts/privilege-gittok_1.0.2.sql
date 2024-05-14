-- ---------------------------
-- 权限功能
-- ----------------------------
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('b3f722e0996a','分支', 'rpy_branch',null ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('28b1e5324b87','添加分支', 'rpy_branch_add','b3f722e0996a' ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('cb4cdeda1d86','删除分支', 'rpy_branch_delete','b3f722e0996a' ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('d79d105520b5','分支设置', 'rpy_branch_setting','b3f722e0996a' ,29, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('93c7df845857','标签', 'rpy_tag',null ,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('3a14e064a484','添加标签', 'rpy_tag_add','93c7df845857' ,31, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('0913b065ed73','删除标签', 'rpy_tag_delete','93c7df845857' ,32, 2);


-- ---------------------------
-- 项目超级管理员权限
-- ----------------------------
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('f936ea7a8acc', 'pro_111111', 'd79d105520b5');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('219283844190', 'pro_111111', 'cb4cdeda1d86');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('ed5b73ad4ebf', 'pro_111111', '28b1e5324b87');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('d3580be6db60', 'pro_111111', 'b3f722e0996a');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('055b563ed33c', 'pro_111111', '0913b065ed73');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('86e16f9fe522', 'pro_111111', '3a14e064a484');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('0f9185719adc', 'pro_111111', '93c7df845857');


-- ---------------------------
--项目管理员功能
-- ----------------------------
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('fbe416c11959', '3', 'd79d105520b5');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('a32ce92be929', '3', 'cb4cdeda1d86');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('c73c77d260a1', '3', '28b1e5324b87');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('b2701ec6490d', '3', 'b3f722e0996a');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('270492918c8f', '3', '0913b065ed73');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('b90ea375bab1', '3', '3a14e064a484');
INSERT INTO pcs_prc_role_function ("id", "role_id", "function_id") VALUES ('6a292378c893', '3', '93c7df845857');
