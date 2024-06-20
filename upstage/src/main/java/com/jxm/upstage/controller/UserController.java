package com.jxm.upstage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.common.domain.UserDto;
import com.jxm.common.service.RedisService;
import com.jxm.upstage.domin.LoginCodeEnum;
import com.jxm.upstage.domin.LoginProperties;
import com.jxm.upstage.dto.*;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.model.UmsAdminRoleRelation;
import com.jxm.upstage.model.UmsRole;
import com.jxm.upstage.service.DepService;
import com.jxm.upstage.service.UmsAdminService;
import com.jxm.upstage.service.UmsRoleService;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RefreshScope  //@Value无法动态感知配置文件内容修改
public class UserController {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DepService depService;

    @Resource
    private LoginProperties loginProperties;

    @Autowired
    private UmsRoleService roleService;

    private static final String VerificationCode="Verification-Code";

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "验证码")
    @ResponseBody
    @GetMapping("/code")
    public CommonResult getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = VerificationCode + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisService.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return CommonResult.success(imgResult);
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        return adminService.login(umsAdminLoginParam);
    }

    @ApiOperation(value = "获取当前登录用户的token信息")
    @RequestMapping(value = "/getCurrentAdmin", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getCurrentAdmin() throws ParseException {
        UmsAdmin umsAdmin = adminService.getCurrentAdmin();
        //获取所属部门长id
        Map<String, Object> data = new HashMap<>();
        data.put("userId", umsAdmin.getId());
        data.put("nickName", umsAdmin.getNickName());
        data.put("icon", umsAdmin.getIcon());
        data.put("depId", umsAdmin.getDepId());
        return CommonResult.success(data);
    }

    @ApiOperation(value = "获取当前登录用户的token信息")
        @RequestMapping(value = "/getMimeInfo", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getMimeInfo() throws ParseException {
        UmsAdmin umsAdmin = adminService.getCurrentAdmin();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", umsAdmin.getId().toString());
        data.put("username", umsAdmin.getUsername());
        data.put("nickName", umsAdmin.getNickName());
        data.put("email", umsAdmin.getEmail());
        data.put("phone", umsAdmin.getPhone());
        data.put("motto", umsAdmin.getMotto());
        data.put("icon", umsAdmin.getIcon());
        return CommonResult.success(data);
    }

    @GetMapping("/getUserFileBrief")
    @ResponseBody
    public CommonResult<FileUserBrief> getUserFileBrief() throws ParseException {
        return CommonResult.success(adminService.getUserFileBrief());
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAdminInfo() throws ParseException {
        UmsAdmin umsAdmin = adminService.getCurrentAdmin();
        Map<String, Object> data = new HashMap<>();
        data.put("id",String.valueOf(umsAdmin.getId()));
        data.put("nickName", umsAdmin.getNickName());
        data.put("username", umsAdmin.getUsername());
        data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = adminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "depId", required = false) Long depId,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsAdmin> adminList = adminService.list(keyword,depId, pageSize, pageNum);
        List<DepIdToName> depIdToNameList = depService.getDepIdToName();
        for(UmsAdmin umsAdmin:adminList){
            Optional<DepIdToName> depIdToName = depIdToNameList.stream().filter(t -> t.getId().equals(umsAdmin.getDepId())).findFirst();
            depIdToName.ifPresent(t->umsAdmin.setDepName(t.getDepName()));
        }
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getItem(id);
        return CommonResult.success(admin);
    }

    // todo
//    @ApiOperation("获取指定用户头像信息")
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<UmsAdmin> getAvatar(@PathVariable Long id) {
//        UmsAdmin admin = adminService.getItem(id);
//        return CommonResult.success(admin);
//    }

    @ApiOperation("获取用户联系信息")
    @RequestMapping(value = "/getUmsAdminConcat", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdminConcat> getUmsAdminConcat(@RequestParam("id") Long id) {
        UmsAdminConcat umsAdmin = adminService.getUmsAdminConcat(id);
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("获取所有用户联系信息")
    @RequestMapping(value = "/getUmsAdminConcatList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsAdminConcat>> getUmsAdminConcatList() {
        List<UmsAdminConcat> umsAdminConcatList = adminService.getUmsAdminConcatList();
        return CommonResult.success(umsAdminConcatList);
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除用户信息")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        int count = adminService.deleteBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        int count = adminService.update(id,umsAdmin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        int count = adminService.update(id, admin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("根据用户名获取通用用户信息")
    @RequestMapping(value = "/loadByUsername", method = RequestMethod.GET)
    @ResponseBody
    public UserDto loadUserByUsername(@RequestParam String username) {
        UserDto userDTO = adminService.loadUserByUsername(username);
        return userDTO;
    }


}
