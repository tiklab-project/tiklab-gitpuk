package io.tiklab.xcode.setting.service;

import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.setting.model.XcodeUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XcodeUserServiceImpl implements XcodeUserService {


    @Autowired
    UserService userService;

    @Autowired
    DmUserService dmUserService;

    @Autowired
    RepositoryServer repositoryServer;


    @Override
    public List<XcodeUser> findUserAndRpy() {
        List<XcodeUser> arrayList = new ArrayList<>();
        List<Repository> rpyList = repositoryServer.findRpyList();


        List<User> allUser = userService.findAllUser();
        for (User user:allUser){
            XcodeUser xcodeUser = new XcodeUser();

            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(user.getId());
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            List<String> stringList = dmUserList.stream().map(a -> a.getDomainId()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rpyList)){
                List<String> rpyIds = rpyList.stream().filter(a -> ("private").equals(a.getRules())).map(Repository::getRpyId).collect(Collectors.toList());
                List<String> commonList = stringList.stream()
                        .filter(rpyIds::contains)
                        .toList();
                xcodeUser.setRepositoryNum(commonList.size());
            }
            xcodeUser.setUserId(user.getId());
            xcodeUser.setUserName(user.getName());

            arrayList.add(xcodeUser);
        }
        return arrayList;
    }
}
