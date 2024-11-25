package io.tiklab.gitpuk.repository.controller;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.Result;
import io.tiklab.gitpuk.repository.model.RepWebHook;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/RepWeb")
@Api(name = "RepWebHookController",desc = "repRepWebHook的认证")
public class RepWebController {

    @RequestMapping(path="/createRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "createRepWebHook",desc = "创建repRepWebHook")
    @ApiParam(name = "RepWebHook",desc = "RepWebHook",required = true)
    public Result<String> createRepWebHook(@RequestBody @NotNull @Valid Object repWebHook){
        Object a=repWebHook;
        String jsonString = JSONObject.toJSONString(a);
        return Result.ok();
    }
}
