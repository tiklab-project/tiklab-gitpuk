UPDATE pcs_prc_function_group SET name = '仓库设置',
                 function_ids='project,domain_setting,domain_use,domain_role,domain_message,merge_setting,repo_clean,lfs,push_setting,web_hook,repo_mirror'
                 WHERE id = 'project_setting';


INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('code', '代码', 'code', 'code', 2, '2');

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('branch', '分支', 'branch', 'branch', 3, '2');

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('tag', '标签', 'tag', 'tag', 3, '2');

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('merge', '合并', 'merge', 'merge', 3, '2');

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('scan', '代码扫描', 'scan', 'code_scan', 3, '2');

INSERT INTO pcs_prc_function_group (id, name, code, function_ids, sort, type)
VALUES ('ci_cd', 'CI/CD', 'ci_cd', 'ci_cd', 3, '2');



--代码
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code', '代码管理', 'code', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_create_file', '创建文件、文件夹', 'code_create_file', 'code', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_update_file', '编辑文件', 'code_update_file', 'code', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_update_delete', '删除文件', 'code_update_delete', 'code', 3, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_fork', '代码Fork', 'code_fork', 'code', 4, '2');


--分支
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('branch', '分支管理', 'branch', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('branch_add', '添加分支', 'branch_add', 'branch', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('branch_delete', '删除分支', 'branch_delete', 'branch', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('branch_cute', '切换主分支', 'branch_cute', 'branch', 3, '2');

--标签
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('tag', '标签管理', 'tag', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('tag_add', '添加标签', 'tag_add', 'tag', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('tag_delete', '删除标签', 'tag_delete', 'tag', 2, '2');

--合并请求
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge', '合并管理', 'merge', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_add', '添加合并', 'merge_add', 'merge', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_delete', '删除合并', 'merge_delete', 'merge', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_comment', '评论', 'merge_comment', 'merge', 3, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_comment_delete', '删除评论', 'merge_comment_delete', 'merge', 4, '2');

--代码扫描
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_scan', '代码扫描', 'code_scan', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_scan_add', '代码扫描添加', 'code_scan_add', 'code_scan', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('code_scan_delete', '代码扫描移除', 'code_scan_delete', 'code_scan', 2, '2');

--CI/CD
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ci_cd', 'CI/CD管理', 'ci_cd', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ci_cd_add', 'CI/CD添加', 'ci_cd_add', 'ci_cd', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('ci_cd_delete', 'CI/CD移除', 'ci_cd_delete', 'ci_cd', 2, '2');



--仓库设置
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('project', '仓库信息', 'project', null, 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_update', '更新仓库', 'repository_update', 'project', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_delete', '删除仓库', 'repository_delete', 'project', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repository_reset', '重置仓库', 'repository_reset', 'project', 3, '2');


INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_setting', '合并设置', 'merge_setting', null, 18, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('merge_setting_add', '合并设置添加', 'merge_setting_add', 'merge_setting', 18, '2');

INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('lfs', '大文件存储', 'lfs', null, 19, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('lfs_delete', '大文件删除', 'lfs_delete', 'lfs', 1, '2');

INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('push_setting', '推送设置', 'push_setting', null, 20, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('push_setting_add', '推送设置添加', 'push_setting_add', 'push_setting', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('push_setting_delete', '推送设置删除', 'push_setting_delete', 'push_setting', 2, '2');

INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_clean', '仓库清理', 'repo_clean', null, 21, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_clean_exec', '仓库清理执行', 'repo_clean_exec', 'repo_clean', 1, '2');

INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('web_hook', 'webHook管理', 'web_hook', null, 22, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('web_hook_add', 'webHook添加', 'web_hook_add', 'web_hook', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('web_hook_update', 'webHook编辑', 'web_hook_update', 'web_hook', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('web_hook_delete', 'webHook删除', 'web_hook_delete', 'web_hook', 3, '2');

INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_mirror', '仓库镜像', 'repo_mirror', null, 23, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_mirror_add', '仓库镜像添加', 'repo_mirror_add', 'repo_mirror', 1, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_mirror_update', '仓库镜像编辑', 'repo_mirror_update', 'repo_mirror', 2, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_mirror_delete', '仓库镜像删除', 'repo_mirror_delete', 'repo_mirror', 3, '2');
INSERT INTO pcs_prc_function (id, name, code, parent_function_id, sort, type) VALUES ('repo_mirror_push', '镜像推送', 'repo_mirror_push', 'repo_mirror', 4, '2');