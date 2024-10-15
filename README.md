# GitPuk代码管理工具
GitPuk是一款开源、免费的代码管理工具，提供了代码库、分支、权限管理、在线浏览代码、分支合并等能力。并可以通过操作记录便于追溯代码变更历史

# 功能介绍
### 代码库管理
简洁的代码库管理界面，易用的属性，极大的提高了开发的效率
![](https://community.tiklab.net/api/image/bca228e97d5770ea)

### 分支管理
多分支管理结合分支的合并功能，在团队开发中极大的增加了协同能力；不同分支的代码文件对比，明确的看到代码的变动情况
![](https://community.tiklab.net/api/image/bc995a7b4e12392c)

### 分支合并
自动检测冲突，为开发者提供直观的冲突解决方案。无论是进行简单的分支合并，
还是复杂的跨团队协作，都能够确保代码合并的准确性和一致性
![](https://community.tiklab.net/api/image/f4340a934f68f3e8)

### 代码扫描
代码质量分析， 能够识别代码中的安全隐患、未遵循的编码规范和潜在的性能问题
![](https://community.tiklab.net/api/image/cd16685bbce4b233)

### git-lfs大文件存储
减少了仓库的容量、提高了上传和拉取代码的速度

# 安装
### 系统要求
* Java 16+
* Maven 3.4+
### 克隆仓库
git clone http://e.gitpuk.tiklab.net/devops-itdd/tiklab-gitpuk.git

cd tiklab-gitpuk
### 构建项目
* MAC系统：mvn clean package -P system-mac,env-dev
* Linux系统：mvn clean package -P system-linux,env-dev
* Windows系统：mvn clean package -P system-windows,env-dev

# 快速使用

1. 启动 GitPukApplication
2. 浏览器访问 http://localhost:8090
3. 登录信息，用户名：admin 密码：123456

# 贡献

我们欢迎社区的贡献！如果你有好的建议或发现了问题，请通过以下方式联系我：

联系我们

邮箱: tiklab@126.com

如需了解更多信息，请访问我们的官方网站或加入我们的社区讨论：

官网：https://tiklab.net

# 进阶使用

文档地址：https://doc.tiklab.net/document/78aa45b76d4c

演示地址：https://demo.tiklab.net/gitpuk
