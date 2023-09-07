INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('xbackups','备份', 'xcode_backups','xbackupsVer' ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('xrecover','恢复', 'xcode_recover','xbackupsVer' ,1, 1);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('xmirror','镜像', 'xcode_mirror',null,1, 2);
INSERT INTO pcs_prc_function (id,name,code,parent_function_id,sort,type) VALUES ('xpshiro','仓库权限', 'xcode_project_shiro',null ,1, 2);




INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('0230645439302', '1', 'xrecover');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('7452315279eb', '1', 'xbackupsVer');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('7452581524544', '3', 'xprojectse');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('7459815279eb', '3', 'xprojectde');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('74592693689b', '3', 'xprojectup');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('7796245ad689b', '3', 'xprojectuse');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('75962693689b', '3', 'xmirror');
INSERT INTO pcs_prc_role_function (id, role_id, function_id) VALUES ('7d93ad236898', '3', 'xpshiro');