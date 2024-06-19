package com.jxm.upstage.controller;

import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import com.jxm.upstage.dto.DepIdToName;
import com.jxm.upstage.dto.DepParam;
import com.jxm.upstage.dto.DepUser;
import com.jxm.upstage.dto.depUserRelation;
import com.jxm.upstage.model.Dep;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.service.DepService;
import com.jxm.upstage.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Api(tags = "depController", description = "部门管理")
@RequestMapping("/dep")
public class depController {

    @Autowired
    private UmsAdminService adminService;

   @Autowired
    private DepService depService;

    @ApiOperation(value = "添加部门")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Dep> register(@Validated @RequestBody DepParam depParam) {
        Dep dep = depService.add(depParam);
        if (dep == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(dep);
    }

    @ApiOperation(value = "查询用户部门信息")
    @RequestMapping(value = "/getDepByDeptId", method = RequestMethod.GET)
    @ResponseBody
    public Dep getDepByDeptId(Long deptId){
       Dep dep;
       dep = depService.getDepByDeptId(deptId);
       return dep;
    }


    @ApiOperation("根据部门名分页获取部门列表")
    @RequestMapping(value = "/allInfo", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<Dep>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<Dep> depList = depService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(depList));
    }

    @ApiOperation("获取所有部门idName关系")
    @RequestMapping(value = "/getDepIdToName", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DepIdToName>> getDepIdToName(){
        return  CommonResult.success(depService.getDepIdToName());
    }

    @ApiOperation("根据部门id分页获取部门下的用户数据")
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<DepUser>> details(@RequestParam(value = "deptId", required = false) Long deptId,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<DepUser> depUserList = depService.details(deptId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(depUserList));
    }

    @ApiOperation("根据登录用户所在部门的所有成员")
    @RequestMapping(value = "/getColleague", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DepUser>> getColleague() throws ParseException {
        UmsAdmin umsAdmin = adminService.getCurrentAdmin();
        List<DepUser> depUserList = depService.details(umsAdmin.getDepId(),30,1);
        return CommonResult.success(depUserList);
    }

    @ApiOperation("修改指定部门信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody Dep dep) {
        int count = depService.update(id, dep);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除指定部门信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = depService.delete(id);
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
        int count = depService.deleteBatch(idList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("查询所有部门人员级联信息")
    @RequestMapping(value = "/selectUserRelation", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<depUserRelation>> selectUserRelation() {
           List<depUserRelation> depUserRelationList= depService.selectUserRelation();
            return CommonResult.success(depUserRelationList);
    }

    @ApiOperation("根据用户id获取部门长id")
    @RequestMapping(value = "/selectDepHeadIdByUser", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Long> selectDepHeadIdByUser(Long userId) {
        return CommonResult.success(depService.selectDepHeadIdByUser(userId));
    }
}
