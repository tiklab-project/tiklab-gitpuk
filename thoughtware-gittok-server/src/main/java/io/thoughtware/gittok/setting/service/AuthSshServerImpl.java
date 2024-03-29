package io.thoughtware.gittok.setting.service;

import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.setting.dao.AuthSshDao;
import io.thoughtware.gittok.setting.entity.AuthSshEntity;
import io.thoughtware.gittok.setting.model.AuthSsh;
import io.thoughtware.gittok.setting.model.AuthSshQuery;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Exporter
public class AuthSshServerImpl implements AuthSshServer {


    @Autowired
    private AuthSshDao authSshDao;

    @Autowired
    private JoinTemplate joinTemplate;
    /**
     * 创建认证
     * @param authSsh 信息
     * @return 认证id
     */
    @Override
    public String createAuthSsh(AuthSsh authSsh) {
        authSsh.setCreateTime(RepositoryUtil.date(1,new Date()));
        AuthSshEntity groupEntity = BeanMapper.map(authSsh, AuthSshEntity.class);
        return authSshDao.createAuthSsh(groupEntity);
    }

    /**
     * 删除认证
     * @param AuthSshId 认证id
     */
    @Override
    public void deleteAuthSsh(String AuthSshId) {
        authSshDao.deleteAuthSsh(AuthSshId);
    }

    /**
     * 更新认证
     * @param authSsh 认证信息
     */
    @Override
    public void updateAuthSsh(AuthSsh authSsh) {
        AuthSshEntity groupEntity = BeanMapper.map(authSsh, AuthSshEntity.class);
        authSshDao.updateAuthSsh(groupEntity);
    }

    /**
     * 查询单个认证
     * @param AuthSshId 认证id
     * @return 认证信息
     */
    @Override
    public AuthSsh findOneAuthSsh(String AuthSshId) {
        AuthSshEntity groupEntity = authSshDao.findOneAuthSsh(AuthSshId);
        AuthSsh authSsh = BeanMapper.map(groupEntity, AuthSsh.class);
        // joinTemplate.joinQuery(authSsh);
        return authSsh;
    }

    /**
     * 查询所有认证
     * @return 认证信息列表
     */
    @Override
    public List<AuthSsh> findAllAuthSsh() {
        List<AuthSshEntity> groupEntityList = authSshDao.findAllAuthSsh();
        List<AuthSsh> list = BeanMapper.mapList(groupEntityList, AuthSsh.class);
        // joinTemplate.joinQuery(list);
        if (list == null || list.isEmpty()){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<AuthSsh> findAllAuthSshList(List<String> idList) {
        List<AuthSshEntity> groupEntities = authSshDao.findAllAuthSshList(idList);
        List<AuthSsh> list = BeanMapper.mapList(groupEntities, AuthSsh.class);
        // joinTemplate.joinQuery(list);
        return list;
    }

    @Override
    public List<AuthSsh> findAuthSshList(AuthSshQuery authSshQuery) {
        List<AuthSshEntity> authSshEntity =  authSshDao.findAuthSshList(authSshQuery);
        List<AuthSsh> authSshList = BeanMapper.mapList(authSshEntity, AuthSsh.class);

        joinTemplate.joinQuery(authSshList);

   /*     if (allAuthSsh.isEmpty()){
            return Collections.emptyList();
        }*/

        return authSshList;
    }
    
}




















