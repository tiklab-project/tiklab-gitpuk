package io.thoughtware.gittok.setting.service;

import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.gittok.setting.model.GitTokUser;
import io.thoughtware.user.dmUser.model.DmUser;
import io.thoughtware.user.dmUser.model.DmUserQuery;
import io.thoughtware.user.dmUser.service.DmUserService;
import io.thoughtware.user.user.model.User;
import io.thoughtware.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitTokUserServiceImpl implements GitTokUserService {


    @Autowired
    UserService userService;

    @Autowired
    DmUserService dmUserService;

    @Autowired
    RepositoryService repositoryServer;


    @Override
    public List<GitTokUser> findUserAndRpy() {
        List<GitTokUser> arrayList = new ArrayList<>();
        List<Repository> rpyList = repositoryServer.findRpyList();


        List<User> allUser = userService.findAllUser();
        for (User user:allUser){
            GitTokUser gitTorkUser = new GitTokUser();

            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(user.getId());
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            List<String> stringList = dmUserList.stream().map(a -> a.getDomainId()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rpyList)){
                List<String> rpyIds = rpyList.stream().filter(a -> ("private").equals(a.getRules())).map(Repository::getRpyId).collect(Collectors.toList());
                //获取两个list 相同的数据
                List<String> commonList = stringList.stream()
                        .filter(rpyIds::contains)
                        .toList();
                gitTorkUser.setRepositoryNum(commonList.size());
            }
            gitTorkUser.setUserId(user.getId());
            gitTorkUser.setUserName(user.getName());

            arrayList.add(gitTorkUser);
        }
        return arrayList;
    }
}
