package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.authority.request.LfsBatchRequest;
import io.tiklab.gitpuk.authority.request.LfsObject;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.model.Repository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryManServiceImpl implements MemoryManService {

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;


    @Override
    public boolean findResMemory() {
        return true;
    }

    @Override
    public boolean isLfsStorage( HttpServletRequest req,LfsBatchRequest lfsBatchRequest) throws IOException {
        String requestURI = req.getRequestURI();
        String[] split = requestURI.split("/");
        // "/xcode/0hz1pjbp/18783894551/gitpuk.git/info/lfs/objects/batch"

        //仓库path
        String rpyName = split[3];
        if (requestURI.startsWith("/xcode")){
             rpyName = StringUtils.substringBefore(rpyName,".git");
        }
        String rpyPath=split[2]+"/"+rpyName;
        Repository repository = repositoryServer.findRepositoryByAddress(rpyPath);

        List<LfsObject> objects = lfsBatchRequest.getObjects();
        //Long allSize = objects.stream().map(LfsObject::getSize).reduce(Long::sum).orElse(0L);

        /*
         * 判断lfs文件夹下面是否和本地上传的oid 相同不会增加内存占用
         * 通过文件的内容生成一个唯一的 SHA-256 哈希值作为 OID
         * */
        String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), repository.getRpyId());
        File directory = new File(rpyLfsPath);
        String[] files = directory.list();

        Long allSize=0L;
        if (!ObjectUtils.isEmpty(files)){
            for (LfsObject lfsObject:objects){
                List<String> stringList = Arrays.stream(files).filter(a -> a.equals(lfsObject.getOid())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(stringList)){
                    allSize+=lfsObject.getSize();
                }
            }
        }
        return this.findResLfsMemory(repository.getRpyId(),allSize);
    }

    @Override
    public boolean findResLfsMemory(String rpyId,Long uploadSize) {

        return true;
    }
}
