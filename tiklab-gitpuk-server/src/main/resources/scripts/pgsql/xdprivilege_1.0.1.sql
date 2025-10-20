

-- ---------------------------
-- 日志类型
-- ----------------------------
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_CREATE', '创建仓库', 'gitpuk');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_UPDATE', '更新仓库', 'gitpuk');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_DELETE', '删除仓库', 'gitpuk');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_CREATE', '创建仓库组', 'gitpuk');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_UPDATE', '更新仓库组', 'gitpuk');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_DELETE', '删除仓库组', 'gitpuk');


-- ---------------------------
-- 消息类型
-- ----------------------------
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_CREATE', '创建仓库', '仓库创建消息', 'gitpuk');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_UPDATE', '更新仓库', '仓库更新消息', 'gitpuk');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_DELETE', '删除仓库', '仓库删除消息', 'gitpuk');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_CREATE', '创建仓库组', '仓库组创建消息', 'gitpuk');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_UPDATE', '更新仓库组', '仓库组更新消息', 'gitpuk');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_DELETE', '删除仓库组', '仓库组删除消息', 'gitpuk');


-- ---------------------------
-- 发送消息途径
-- ----------------------------
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_CREATE', 'GTK_CREATE', 1, 'gitpuk', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_UPDATE', 'GTK_UPDATE', 1, 'gitpuk', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_DELETE', 'GTK_DELETE', 1, 'gitpuk', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_CREATE', 'GTKGP_CREATE', 1, 'gitpuk', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_UPDATE', 'GTKGP_UPDATE', 1, 'gitpuk', 'site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_DELETE', 'GTKGP_DELETE', 1, 'gitpuk', 'site');


-- ---------------------------
-- 发送消息模版
-- ----------------------------
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_CREATE', 'GTK_CREATE', 'site', '创建仓库', '创建仓库',null,'gitpuk',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_UPDATE', 'GTK_UPDATE', 'site', '更新仓库', '更新仓库',null,'gitpuk',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_DELETE', 'GTK_DELETE', 'site', '删除仓库', '删除仓库',null,'gitpuk',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_CREATE', 'GTKGP_CREATE', 'site', '创建仓库组', '创建仓库',null,'gitpuk',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_UPDATE', 'GTKGP_UPDATE', 'site', '更新仓库组', '更新仓库',null,'gitpuk',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_DELETE', 'GTKGP_DELETE', 'site', '删除仓库组', '删除仓库',null,'gitpuk',null);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
('bb09db8fa4d1', 'GTK_CREATE', 'qywechat', NULL, '## 创建仓库\n
> 创建人：<font color=comment>${userName}</font>\n
> 仓库名称：<font color=warning>[${repositoryName}](${qywxurl})</font>','/#/repository/${repositoryPath}/setting/info','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa511', 'GTK_UPDATE', 'qywechat', NULL, '## 更新仓库\n
> 执行人：<font color=comment>${userName}</font>\n
> 更新前名称：<font color=comment>${updateName}</font>\n
> 更新后名称：<font color=warning>[${repositoryName}](${qywxurl})</font>','/#/repository/${repositoryPath}/setting/info','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa512', 'GTK_DELETE', 'qywechat', NULL, '## 删除仓库\n
> 执行人：<font color=comment>${userName}</font>\n
> 仓库名称：<font color=warning>[${repositoryName}](${qywxurl})</font>','/#/repository','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa611', 'GTKGP_CREATE', 'qywechat', NULL, '## 创建仓库组\n
> 执行人：<font color=comment>${userName}</font>\n
> 仓库组名称：<font color=warning>[${groupName}](${qywxurl})</font>','/#/group/${groupName}/setting/info','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa612', 'GTKGP_UPDATE', 'qywechat', NULL, '## 更新仓库组\n
> 执行人：<font color=comment>${userName}</font>\n
> 更新前名称：<font color=comment>${updateName}</font>\n
> 更新后名称：<font color=warning>[${groupName}](${qywxurl})</font>','/#/group/${groupName}/setting/info','gitpuk',NULL);

INSERT INTO pcs_mec_message_template(id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES
    ('bb09db8fa613', 'GTKGP_DELETE', 'qywechat', NULL, '## 删除仓库组\n
> 执行人：<font color=comment>${userName}</font>\n
> 仓库组名称：<font color=warning>[${groupName}](${qywxurl})</font>','/#/group','gitpuk',NULL);

