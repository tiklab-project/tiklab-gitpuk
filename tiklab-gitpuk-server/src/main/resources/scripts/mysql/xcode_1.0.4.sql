ALTER TABLE rpy_auth_ssh add expire_time varchar(32);
ALTER TABLE rpy_auth_ssh add fingerprint VARCHAR(64);
ALTER TABLE rpy_auth_ssh MODIFY create_time TIMESTAMP;
ALTER TABLE rpy_auth_ssh MODIFY user_time TIMESTAMP;
-- ----------------
-- 角色功能
-- ----------------
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('eb48a8475aa5','合并请求', 'rpy_merge',null ,28, 2);

INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES('72eb2b120c91', 'pro_111111', 'eb48a8475aa5');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES('4394f2f25b7c', '3', 'eb48a8475aa5');



-- ------------------
-- 消息类型
-- ------------------
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_RESET', '重置仓库', '仓库重置消息', 'gittok');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('MERGE_CREATE', '创建合并请求', '创建合并请求', 'gittok');


-- ----------------
-- 消息发送途径
-- ----------------
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_RESET', 'GTK_RESET', 1, 'gittok', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('MERGE_CREATE', 'MERGE_CREATE', 1, 'gittok', 'site');


-- ---------------
-- 发送消息模版
-- ---------------
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_RESET', 'GTK_RESET', 'site', '重置仓库', '重置仓库',null,'gittok',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('MERGE_CREATE', 'MERGE_CREATE', 'site', '创建合并请求', '创建合并请求',null,'gittok',null);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa811', 'GTK_RESET', 'qywechat', NULL, '## 重置仓库\n
> 执行人：<font color=comment>${userName}</font>\n
> 仓库名称：<font color=warning>[${repositoryName}](${qywxurl})</font>','/#/repository/${repositoryPath}/code','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa822', 'GTK_RESET', 'qywechat', NULL, '## 创建合并请求\n
> 执行人：<font color=comment>${userName}</font>\n
> 合并名字：<font color=warning>[${mergeName}](${qywxurl})</font>','/#/repository/${repositoryPath}/mergeAdd/${mergeId}','gitpuk',NULL);


-- ---------------
-- 消息发送人
-- ---------------
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('139414cd4061', 'GTK_CREATE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('b4278e92f78d', 'GTK_UPDATE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('f8af4ef8ffe7', 'GTK_DELETE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('5546a83e0aad', 'GTK_RESET', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('5546a83e0acc', 'MERGE_CREATE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('f9588717de80', 'GTKGP_CREATE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('75d248f667ca', 'GTKGP_UPDATE', '111111');
INSERT INTO pcs_mec_message_notice_connect_user (id,message_notice_id,user_id) VALUES ('75d248f66785', 'GTKGP_DELETE', '111111');



-- ---------------
-- 日志类型
-- ---------------
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_RESET', '重置仓库', 'gittok');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('MERGE_CREATE', '创建合并请求', 'gittok');





-- ---------------------------
-- 系统、项目超级管理员权限
-- ----------------------------
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('594bdcc864e9', '111111', '9633d9475886');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('8199d40d18f9', '111111', 'dd81bdbb52bc');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('d039c99fa25a', '111111', '57a3bcd1e5fe');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('177bcdd53f2a', '111111', '428be660dea3');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('fc891dd94dfe', '111111', '5fb7863b09a8');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('8b2651c2a570', '111111', 'e8bf9843bc9d');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('99847e675571', '111111', 'cb954a7c0be3');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('00947bee147d', '111111', '9c99b8a096c8');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('3b3641ca2167', '111111', '325c2503007f');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('04545c905265', '111111', '6b61fbe5091a');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('b492b5f8b62f', '111111', '043e412151db');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('000568e6d858', '111111', '925371be8ec6');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('a15b00509913', '111111', '447d9998fc00');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('2acaf2d543cf', '111111', '890e7d41decf');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('4a7209a98322', '111111', '585d26bcbdf3');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('d904276449cd', '111111', 'wqre9998fc00');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('00493fdacdcb', '111111', '43e7d41decf7');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('b36c8a12f1c6', '111111', 'hfg5371be8ec');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('fe29f46eca20', '111111', 'oug5371be8ec');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('4d8074485088', '111111', 'hf43e412151e');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('ee403ac01cb3', '111111', '4235d2624bdf');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('28da951a9861', '111111', 'e5b34be19fab');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('51c2151c2775', '111111', '4cc4e67319a0');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('fdb4e770ea16', '111111', 'cb6c8c3f4048');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('cdcd73b9edd2', '111111', '64bdf62686a4');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('46f055e9b3d1', '111111', 'gssh');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('6b8d251a87a1', '111111', 'gscan');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('798f0448c94b', '111111', 'gbackups');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('4f2c9abb56b3', '111111', 'gplugin');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('fe6094284a35', '111111', 'glog');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('b31bf5c263d3', '111111', 'gauthority');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('0a4d92d9620e', '111111', 'gnewsway');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('399a7fa57ef4', '111111', 'gnewsscheme');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('2750efb0134d', '111111', 'gresource');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('0ee19ad0965a', 'pro_111111', 'rpyupdate');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('8fa3dd272111', 'pro_111111', 'rpydelete');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('f0f72ea52d5f', 'pro_111111', 'rpysetting');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('f38ea48fc4c8', 'pro_111111', 'rpyuser');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('93e25ab0ded3', 'pro_111111', 'rpyauthority');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('58a191b01ae8', 'pro_111111', 'rpyclean');
INSERT INTO pcs_prc_role_function (id,role_id,function_id) VALUES ('5bdd78db6a4a', 'pro_111111', 'rpymirror');








































