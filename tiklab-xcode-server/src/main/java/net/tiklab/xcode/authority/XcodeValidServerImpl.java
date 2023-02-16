package net.tiklab.xcode.authority;


import net.tiklab.eam.passport.user.model.UserPassport;
import net.tiklab.eam.server.author.EamAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcodeValidServerImpl implements XcodeValidServer{


    @Autowired
    private EamAuthenticator authenticator;


    private static final Logger logger = LoggerFactory.getLogger(XcodeValidServerImpl.class);


    public boolean validUserNamePassword(String username ,String password,String id){
        logger.info("效验账号信息："+ username + ":"+password);
        try {
            UserPassport userPassport = new UserPassport();
            userPassport.setPassword(password);
            userPassport.setAccount(username);
            userPassport.setDirId(id);
            authenticator.login(userPassport);
            return true;
        }catch (Exception e){
            return false;
        }
    }

















}






























