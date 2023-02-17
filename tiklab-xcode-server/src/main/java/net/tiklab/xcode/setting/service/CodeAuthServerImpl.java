package net.tiklab.xcode.setting.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.user.user.model.User;
import net.tiklab.utils.context.LoginContext;
import net.tiklab.xcode.setting.dao.CodeAuthDao;
import net.tiklab.xcode.setting.entity.CodeAuthEntity;
import net.tiklab.xcode.setting.model.CodeAuth;
import net.tiklab.xcode.until.CodeUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Exporter
public class CodeAuthServerImpl implements CodeAuthServer{

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private CodeAuthDao codeAuthDao;


    /**
     * 创建认证
     * @param codeAuth 信息
     * @return 认证id
     */
    @Override
    public String createCodeAuth(CodeAuth codeAuth) {
        codeAuth.setCreateTime(CodeUntil.date(1,new Date()));
        CodeAuthEntity groupEntity = BeanMapper.map(codeAuth, CodeAuthEntity.class);
        return codeAuthDao.createCodeAuth(groupEntity);
    }

    /**
     * 删除认证
     * @param codeAuthId 认证id
     */
    @Override
    public void deleteCodeAuth(String codeAuthId) {
        codeAuthDao.deleteCodeAuth(codeAuthId);
    }

    /**
     * 更新认证
     * @param codeAuth 认证信息
     */
    @Override
    public void updateCodeAuth(CodeAuth codeAuth) {
        CodeAuthEntity groupEntity = BeanMapper.map(codeAuth, CodeAuthEntity.class);
        codeAuthDao.updateCodeAuth(groupEntity);
    }

    /**
     * 查询单个认证
     * @param codeAuthId 认证id
     * @return 认证信息
     */
    @Override
    public CodeAuth findOneCodeAuth(String codeAuthId) {
        CodeAuthEntity groupEntity = codeAuthDao.findOneCodeAuth(codeAuthId);
        CodeAuth codeAuth = BeanMapper.map(groupEntity, CodeAuth.class);
        joinTemplate.joinQuery(codeAuth);
        return codeAuth;
    }

    /**
     * 查询所有认证
     * @return 认证信息列表
     */
    @Override
    public List<CodeAuth> findAllCodeAuth() {
        List<CodeAuthEntity> groupEntityList = codeAuthDao.findAllCodeAuth();
        List<CodeAuth> list = BeanMapper.mapList(groupEntityList, CodeAuth.class);
        joinTemplate.joinQuery(list);
        if (list == null || list.isEmpty()){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<CodeAuth> findAllCodeAuthList(List<String> idList) {
        List<CodeAuthEntity> groupEntities = codeAuthDao.findAllCodeAuthList(idList);
        List<CodeAuth> list = BeanMapper.mapList(groupEntities, CodeAuth.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 查询用户认证
     * @return 认证集合
     */
    @Override
    public List<CodeAuth> findUserAuth() {
        List<CodeAuth> allCodeAuth = findAllCodeAuth();
        if (allCodeAuth.isEmpty()){
            return Collections.emptyList();
        }
        List<CodeAuth> list = new ArrayList<>();
        for (CodeAuth codeAuth : allCodeAuth) {
            User user = codeAuth.getUser();
            if (!user.getId().equals(LoginContext.getLoginId())){
                continue;
            }
            list.add(codeAuth);
        }

        return list;
    }
    
}




















