package net.tiklab.xcode.code.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.xcode.code.service.CloneCodeServer;
import net.tiklab.xcode.code.service.CloneCodeServerImpl;
import net.tiklab.xcode.code.service.Propfind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@Api(name = "CodeController",desc = "仓库")
public class CloneController {

    @Autowired
    CloneCodeServer codeServer;



    private static final Logger logger = LoggerFactory.getLogger(CloneController.class);

    @Propfind
    @RequestMapping(path="/**" )
    @ApiMethod(name = "createCode",desc = "创建仓库")
    public Result<Void> clone(ServletRequest request, ServletResponse response, String service){

         codeServer.clone(request, response, service);

        return Result.ok();
    }

}


























































