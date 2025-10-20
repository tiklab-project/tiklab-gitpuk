package io.tiklab.gitpuk.setting.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryGroup;
import io.tiklab.gitpuk.repository.service.RepositoryGroupServer;
import io.tiklab.gitpuk.setting.model.GitPukQuery;
import io.tiklab.gitpuk.setting.model.GitPukUser;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.model.UserQuery;
import io.tiklab.user.user.service.UserProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitPukUserServiceImpl implements GitPukUserService {


    @Autowired
    UserProcessor userProcessor;

    @Autowired
    DmUserService dmUserService;

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    RepositoryGroupServer groupServer;

    @Override
    public Pagination<GitPukUser> findUserAndRpy(GitPukQuery gitPukQuery) {
        List<GitPukUser> arrayList = new ArrayList<>();
        List<Repository> rpyList = repositoryServer.findRpyList();
        List<RepositoryGroup> allGroup = groupServer.findAllGroup();

        //条件查询用户
        UserQuery userQuery = new UserQuery();
        if (StringUtils.isNotEmpty(gitPukQuery.getUserName())){
            userQuery.setNickname(gitPukQuery.getUserName());
        }
        userQuery.setStatus(1);
        userQuery.setPageParam(gitPukQuery.getPageParam());
        Pagination<User> userPage = userProcessor.findUserPage(userQuery);

        List<User> dataList = userPage.getDataList();
        for (User user:dataList){

            GitPukUser gitPukUser = findRepoAndGroup(rpyList, allGroup, user.getId());
            gitPukUser.setUserId(user.getId());
            gitPukUser.setUserName(user.getName());
            gitPukUser.setNickName(user.getNickname());
            arrayList.add(gitPukUser);
        }
        return PaginationBuilder.build(userPage,arrayList);
    }

    @Override
    public GitPukUser findNumByUser(GitPukQuery gitPukQuery) {
        List<Repository> rpyList = repositoryServer.findRpyList();
        List<RepositoryGroup> allGroup = groupServer.findAllGroup();
        GitPukUser gitPukUser = findRepoAndGroup(rpyList, allGroup, gitPukQuery.getUserId());
        return gitPukUser;
    }

    //通过用户查询仓库和仓库组数量
    public GitPukUser findRepoAndGroup(List<Repository> rpyList, List<RepositoryGroup> allGroup,String userId){
        GitPukUser gitPukUser = new GitPukUser();

        //用户角色
        DmUserQuery dmUserQuery = new DmUserQuery();
        dmUserQuery.setUserId(userId);
        List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
        //获取角色关联的所有仓库id
        List<String> stringList = dmUserList.stream().map(a -> a.getDomainId()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(rpyList)){
            List<Repository> publicRpy = rpyList.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());
            List<String> rpyIds = rpyList.stream().filter(a -> ("private").equals(a.getRules())).map(Repository::getRpyId).collect(Collectors.toList());
            //获取两个list 相同的数据
            List<String> commonList = stringList.stream()
                    .filter(rpyIds::contains)
                    .toList();
            int publicNUm=0;
            if (CollectionUtils.isNotEmpty(publicRpy)){
                publicNUm = publicRpy.size();
            }
            if (CollectionUtils.isNotEmpty(commonList)){
                publicNUm = commonList.size()+publicNUm;
            }
            gitPukUser.setRepositoryNum(publicNUm);
        }

        //仓库组
        if (CollectionUtils.isNotEmpty(allGroup)){
            List<RepositoryGroup> publicGroup = allGroup.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());
            List<String> groupIds = allGroup.stream().filter(a -> ("private").equals(a.getRules())).map(RepositoryGroup::getGroupId).collect(Collectors.toList());

            //获取两个list 相同的数据
            List<String> commonList = stringList.stream()
                    .filter(groupIds::contains)
                    .toList();
            int publicNUm=0;
            if (CollectionUtils.isNotEmpty(publicGroup)){
                publicNUm = publicGroup.size();
            }
            if (CollectionUtils.isNotEmpty(commonList)){
                publicNUm = commonList.size()+publicNUm;
            }
            gitPukUser.setGroupNum(publicNUm);
        }
      return gitPukUser;
    }
}
