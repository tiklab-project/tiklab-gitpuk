package io.thoughtware.gittork.setting.service;

import io.thoughtware.gittork.repository.model.Repository;
import io.thoughtware.gittork.repository.service.RepositoryServer;
import io.thoughtware.gittork.setting.model.GitTorkUser;
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
public class GitTorkUserServiceImpl implements GitTorkUserService {


    @Autowired
    UserService userService;

    @Autowired
    DmUserService dmUserService;

    @Autowired
    RepositoryServer repositoryServer;


    @Override
    public List<GitTorkUser> findUserAndRpy() {
        List<GitTorkUser> arrayList = new ArrayList<>();
        List<Repository> rpyList = repositoryServer.findRpyList();


        List<User> allUser = userService.findAllUser();
        for (User user:allUser){
            GitTorkUser gitTorkUser = new GitTorkUser();

            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(user.getId());
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            List<String> stringList = dmUserList.stream().map(a -> a.getDomainId()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rpyList)){
                List<String> rpyIds = rpyList.stream().filter(a -> ("private").equals(a.getRules())).map(Repository::getRpyId).collect(Collectors.toList());
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
