-- ---------------------------
-- 日志类型
-- ----------------------------
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_CREATE', '创建仓库', 'gittork');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_UPDATE', '更新仓库', 'gittork');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTK_DELETE', '删除仓库', 'gittork');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_CREATE', '创建仓库组', 'gittork');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_UPDATE', '更新仓库组', 'gittork');
INSERT INTO pcs_op_log_type (id, name, bgroup) VALUES ('GTKGP_DELETE', '删除仓库组', 'gittork');


-- ---------------------------
-- 消息类型
-- ----------------------------
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_CREATE', '创建仓库', '仓库创建消息', 'gittork');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_UPDATE', '更新仓库', '仓库更新消息', 'gittork');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTK_DELETE', '删除仓库', '仓库删除消息', 'gittork');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_CREATE', '创建仓库组', '仓库组创建消息', 'gittork');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_UPDATE', '更新仓库组', '仓库组更新消息', 'gittork');
INSERT INTO pcs_mec_message_type (id, name, description, bgroup) VALUES ('GTKGP_DELETE', '删除仓库组', '仓库组删除消息', 'gittork');


-- ---------------------------
-- 发送消息途径
-- ----------------------------
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_CREATE', 'GTK_CREATE', 1, 'gittork', 'dingding,email,qywechat,site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_UPDATE', 'GTK_UPDATE', 1, 'gittork', 'dingding,email,qywechat,site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTK_DELETE', 'GTK_DELETE', 1, 'gittork', 'site,email,qywechat');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_CREATE', 'GTKGP_CREATE', 1, 'gittork', 'dingding,email,qywechat,site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_UPDATE', 'GTKGP_UPDATE', 1, 'gittork', 'dingding,email,qywechat,site');
INSERT INTO pcs_mec_message_notice (id, message_type_id, type, bgroup, message_send_type_id) VALUES ('GTKGP_DELETE', 'GTKGP_DELETE', 1, 'gittork', 'site,email,qywechat');


-- ---------------------------
-- 发送消息模版
-- ----------------------------
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_CREATE', 'GTK_CREATE', 'site', '创建仓库', '创建仓库',null,'gittork',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_UPDATE', 'GTK_UPDATE', 'site', '更新仓库', '更新仓库',null,'gittork',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTK_DELETE', 'GTK_DELETE', 'site', '删除仓库', '删除仓库',null,'gittork',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_CREATE', 'GTKGP_CREATE', 'site', '创建仓库组', '创建仓库',null,'gittork',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_UPDATE', 'GTKGP_UPDATE', 'site', '更新仓库组', '更新仓库',null,'gittork',null);
INSERT INTO pcs_mec_message_template (id, msg_type_id, msg_send_type_id, title, content,link,bgroup,link_params) VALUES ('GTKGP_DELETE', 'GTKGP_DELETE', 'site', '删除仓库组', '删除仓库',null,'gittork',null);
