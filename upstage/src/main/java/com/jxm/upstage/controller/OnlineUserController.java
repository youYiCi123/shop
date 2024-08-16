package com.jxm.upstage.controller;

import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.upstage.dto.OnlineUmsAdmin;
import com.jxm.upstage.service.UmsAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online")
@RefreshScope  //@Value无法动态感知配置文件内容修改
public class OnlineUserController {

    @Autowired
    private UmsAdminService adminService;

    @ApiOperation("获取在线用户信息")
    @RequestMapping(value = "/onlineUserList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OnlineUmsAdmin>> onlineUserList(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OnlineUmsAdmin> onlineUmsAdmins = adminService.onlineUserList(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(onlineUmsAdmins));
    }
}
