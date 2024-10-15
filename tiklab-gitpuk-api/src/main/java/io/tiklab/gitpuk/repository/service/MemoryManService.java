package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.authority.request.LfsBatchRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface MemoryManService {

    /**
     * 查询是否有剩余内存
     */
    boolean findResMemory();


    /**
     * 判断lfs空间是否足够
     * @param request request
     * @param lfsBatchRequest 客户端上传的lfs问价信息
     */
    boolean isLfsStorage(HttpServletRequest request, LfsBatchRequest lfsBatchRequest) throws IOException;

    /**
     * 查询lfs内存是否够用
     * @param rpyId 仓库id
     * @param uploadSize 上传大小
     */
    boolean findResLfsMemory(String rpyId,Long uploadSize);

}
