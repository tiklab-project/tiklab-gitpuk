package io.tiklab.gitpuk.authority.lfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tiklab.gitpuk.authority.GitLfsAuthService;
import io.tiklab.gitpuk.authority.request.LfsBatchRequest;
import io.tiklab.gitpuk.authority.request.LfsData;
import io.tiklab.gitpuk.authority.request.LfsObject;
import io.tiklab.gitpuk.authority.response.LfsAction;
import io.tiklab.gitpuk.authority.response.LfsActionDetails;
import io.tiklab.gitpuk.authority.response.LfsBatchResponse;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/*
*  GitLfsAuth 解析客户端传入的lfs文件信息，组装后返回给客户端
*   http: 1. 客户端发起带有/info/lfs/objects/batch请求，并携带lfs文件信息，解析lfs文件信息，并将客户端上传、下载文件内容的路径一并返回给客户端。
*         2. 客户端根据返回的路径执行上传、下载操作
*
*   ssh: 1. 客户端发起带有git-lfs-authenticate请求，不携带lfs文件信息，给你客户端返回上传文件信息的路径。
*        2. 客户单根据返回的文件信息的路径上传文件信息，解析lfs文件信息，并将客户端上传、下载文件内容的路径一并返回给客户端。
*        3. 客户端根据返回的路径执行上传、下载操作
* */
@Service
public class GitLfsAuth implements GitLfsAuthService {
    private static final Logger logger = LoggerFactory.getLogger(GitLfsAuth.class);
    /**
     * 解析客户端上传的数据、拼接后返回给客户端
     * @param lfsData lfsData
     */
    @Override
    public  void HandleLfsBatch(LfsData lfsData) throws IOException {
        LfsBatchRequest lfsBatchRequest = lfsData.getLfsBatchRequest();
        HttpServletResponse resp = lfsData.getResponse();

        String requestURL = lfsData.getRequest().getRequestURL().toString();
        LfsBatchResponse response = new LfsBatchResponse();
        List<LfsAction> actions = new ArrayList<>();

        // 处理请求逻辑，例如查找需要上传/下载的对象
        List<LfsObject> objects = lfsBatchRequest.getObjects();

        for (LfsObject lfsObject:objects){

            Map<String, LfsActionDetails> hashMap = new HashMap<>();

            LfsAction lfsAction = new LfsAction();
            lfsAction.setOid(lfsObject.getOid());
            lfsAction.setSize(lfsObject.getSize());

            // 根据操作类型生成相应的 URL
            LfsActionDetails actionDetails = generateUploadAction(lfsObject, requestURL, lfsData.getType());
            if ("upload".equalsIgnoreCase(lfsBatchRequest.getOperation())) {
                //上传
                hashMap.put("upload",actionDetails);
                lfsAction.setActions(hashMap);
            } else if ("download".equalsIgnoreCase(lfsBatchRequest.getOperation())) {

                //下载交验 文件是否存在
                File file = new File(lfsData.getLfsPath(), lfsObject.getOid());
                if (!file.exists()) {
                    //返回客户端信息
                    resp.setContentType("application/json");
                    new ObjectMapper().writeValue(resp.getWriter(), "external filter 'git-lfs filter-process' failed");
                    return;
                }

                //下载
                hashMap.put("download",actionDetails);
                lfsAction.setActions(hashMap);
            }
            actions.add(lfsAction);
        }
        response.setObjects(actions);

        //返回客户端信息
        resp.setContentType("application/json");
        new ObjectMapper().writeValue(resp.getOutputStream(), response);
    }


    /**
     * 添加推送（拉取） lfs文件的地址
     * @param lfsObject lfsObject
     * @param requestURL 请求全路径
     * @param type http、ssh
     */
    public  LfsActionDetails generateUploadAction(LfsObject lfsObject,String requestURL,String type) {
        String path;
        if (("http").equals(type)){
            //替换 lfs请求的路径
            String replace = requestURL.replace("xcode", "lfs");
            path= replace.substring(0, replace.lastIndexOf(".git"));
        }else {
             path = StringUtils.substringBefore(requestURL, "/objects");
        }
        logger.info("lfs回调地址："+path);
        // 创建上传动作的详细信息
        LfsActionDetails uploadAction = new LfsActionDetails();
        //返回给客户端 上传或者下载lfs文件内容的地址，客户端会根据该地址再次发起请求
        String downloadUrl = String.format(path+"/%s", lfsObject.getOid());
        uploadAction.setHref(downloadUrl);
        // 如果需要，可以在此处添加 HTTP 头信息，例如身份验证
    /*    Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic your-auth-token");
        uploadAction.setHeader(headers);*/

        return uploadAction;
    }

}
