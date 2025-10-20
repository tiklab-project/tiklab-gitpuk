package io.tiklab.gitpuk.authority.http;

import io.tiklab.eam.passport.user.service.UserPassportProcessor;
import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.eam.passport.user.model.UserPassport;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ValidUsrPwdServerImpl implements ValidUsrPwdServer {


    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private UserPassportProcessor userPassportProcessor;

    @Autowired
    private DmUserService dmUserService;

    private static final Logger logger = LoggerFactory.getLogger(ValidUsrPwdServerImpl.class);

    //效验用户名密码
    public boolean validUserNamePassword(String username ,String password,String id) {
        logger.info("校验账号信息：" + username + ":" + password);
        try {
            UserPassport userPassport = new UserPassport();
            userPassport.setPassword(password);
            userPassport.setAccount(username);
            userPassport.setDirId(id);


            userPassportProcessor.validLogin(userPassport);
            return true;
        } catch (Exception e) {
            logger.info("校验失败:",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void validUser(String username, String password, String id) {
        UserPassport userPassport = new UserPassport();
        userPassport.setPassword(password);
        userPassport.setAccount(username);
        userPassport.setDirId(id);

        userPassportProcessor.validLogin(userPassport);


    }

    @Override
    public boolean validUserPrivilege(String username,String repositoryId) {
        //查询用户
        User user = userProcessor.findUserByUsername(username, null);
        if (ObjectUtils.isEmpty(user)){
            return false;
        }
        //通过用户和仓库查询 ，是否属于仓库成员
        DmUserQuery dmUserQuery = new DmUserQuery();
        dmUserQuery.setDomainId(repositoryId);
        dmUserQuery.setUserId(user.getId());
        List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
        if (CollectionUtils.isEmpty(dmUserList)){
            return false;
        }
        return true;
    }

}




































































