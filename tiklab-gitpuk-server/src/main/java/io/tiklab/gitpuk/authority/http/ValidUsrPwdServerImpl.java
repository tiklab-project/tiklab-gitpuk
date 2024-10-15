package io.tiklab.gitpuk.authority.http;

import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.eam.passport.user.model.UserPassport;
import io.tiklab.eam.passport.user.service.UserPassportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidUsrPwdServerImpl implements ValidUsrPwdServer {

    @Autowired
    private UserPassportService userPassportService;

    private static final Logger logger = LoggerFactory.getLogger(ValidUsrPwdServerImpl.class);

    //效验用户名密码
    public boolean validUserNamePassword(String username ,String password,String id) {
        logger.info("校验账号信息：" + username + ":" + password);
        try {
            UserPassport userPassport = new UserPassport();
            userPassport.setPassword(password);
            userPassport.setAccount(username);
            userPassport.setDirId(id);

            userPassportService.validLogin(userPassport);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void validUser(String username, String password, String id) {
        UserPassport userPassport = new UserPassport();
        userPassport.setPassword(password);
        userPassport.setAccount(username);
        userPassport.setDirId(id);

        userPassportService.validLogin(userPassport);
    }

}




































































