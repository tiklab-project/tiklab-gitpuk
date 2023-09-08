package io.tiklab.xcode.setting.service;

import io.tiklab.xcode.setting.model.XcodeUser;

import java.util.List;

public interface XcodeUserService {

    /**
     * 查询用户以及仓库
     */
    List<XcodeUser> findUserAndRpy();
}
