package com.jxm.business.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.jxm.business.dto.DashboardFileTypeParam;
import com.jxm.business.dto.DashboardUserFileParam;
import com.jxm.business.dto.UserUploadCountDto;
import com.jxm.business.dto.depUserRelation;
import com.jxm.business.feign.FileService;
import com.jxm.business.feign.UpstageService;
import com.jxm.business.model.NewsHomeDetail;
import com.jxm.business.model.NewsParam;
import com.jxm.business.model.NewsPostParam;
import com.jxm.business.model.NewsShowHomeParam;
import com.jxm.business.service.NewsService;
import com.jxm.business.service.SurveyService;
import com.jxm.business.service.TempService;
import com.jxm.common.api.CommonPage;
import com.jxm.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UpstageService upstageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TempService tempService;

    /**
     * 首页上展示
     */
    @RequestMapping(value = "/getDashboard", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getDashboard() {
        Map<String, Object> map = new HashMap<>(16);
        List<NewsShowHomeParam> NewsShowList=newsService.getNewsToHome();
        NewsHomeDetail newsTopToHome = newsService.getNewsTopToHome();
        List<depUserRelation> depUserRelationList= upstageService.selectUserRelation().getData();
        String jsonStr = JSONUtil.toJsonStr(fileService.getTheNumberOfFileTypes().getData());
        List<DashboardUserFileParam> dashboardUserFileParams = JSONUtil.toList(jsonStr, DashboardUserFileParam.class);
        //获取登录用户所属部门的文件类型数量信息
        Map<Integer, Long> FileTypeNums = dashboardUserFileParams.stream().collect(Collectors.groupingBy(DashboardUserFileParam::getFileType, Collectors.counting()));
        List<DashboardFileTypeParam> dashboardFileTypeParams=new ArrayList<>();
        for (Map.Entry<Integer, Long>  fileTypeNums : FileTypeNums.entrySet()) {
            DashboardFileTypeParam dashboardFileTypeParam = new DashboardFileTypeParam();
            dashboardFileTypeParam.setFileType(fileTypeNums.getKey());
            dashboardFileTypeParam.setNums(fileTypeNums.getValue().intValue());
            dashboardFileTypeParams.add(dashboardFileTypeParam);
        }
        //用户分享文件数量信息
        List<UserUploadCountDto> userUploadCountDtos = fileService.getUserUploadCount().getData();
        //获取最新的调查问卷Id
        String latestId = String.valueOf(tempService.getLatestId());
        map.put("depUserRelationList", depUserRelationList);
        map.put("userUploadCountArrayList", userUploadCountDtos);
        map.put("fileTypeNums", dashboardFileTypeParams);
        map.put("NewsShowList", NewsShowList);
        map.put("newsTopToHome", newsTopToHome);
        map.put("surveyLatestId", latestId);
        return CommonResult.success(map, "获取成功");
    }


    /**
     *后台显示新闻列表
     */
    @RequestMapping(value = "/getNewsBySearch", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getNewsBySearch(@RequestParam(defaultValue = "") String[] date,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "newsType", defaultValue = "0") Long newsType,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        String startDate = null;
        String endDate = null;
        if (date.length == 2) {
            startDate = date[0];
            endDate = date[1];
        }
        PageHelper.startPage(pageNum, pageSize);
        List<NewsPostParam> NewsBrieflyList=newsService.getNewsByDateAndKeyword(startDate, endDate,keyword,newsType);
        return CommonResult.success(CommonPage.restPage(NewsBrieflyList),"请求成功");
    }

    /**
     * 后台查询新闻（用于编辑）
     */
    @RequestMapping(value = "/queryContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<NewsParam> queryTrainContent(@PathVariable Long id){
        return CommonResult.success(newsService.queryNewsContent(id));
    }

    /**
     * 用户点击查看新闻内容
     */
    @RequestMapping(value = "/queryNewsContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<NewsHomeDetail> queryNewsContent(@PathVariable Long id){
        return CommonResult.success(newsService.queryNewsPreContent(id));
    }

    /**
     * 添加新闻内容
     */
    @RequestMapping(value = "/addContent", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addContent(@RequestBody NewsParam newsParam) throws ParseException {
        int count = newsService.addContent(newsParam);
        if(count!=0){
            return CommonResult.success();
        }else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改新闻内容
     */
    @RequestMapping(value = "/updateContent", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateContent(@RequestBody NewsParam trainContentParam){
        int count=newsService.updateContent(trainContentParam);
        if(count!=0)
            return CommonResult.success();
        else
            return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除培训信息
        int count=newsService.delete(id);
        if(count<0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }

    @RequestMapping(value = "/handleBatchDelete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestBody Long[] multipleSelectionId) {
        List<Long> idList= Arrays.stream(multipleSelectionId).collect(Collectors.toList());
        //删除多个培训信息
        int count=newsService.deleteBatchNews(idList);
        if(count<0)
            return CommonResult.failed("删除培训信息错误");
        return CommonResult.success();
    }

}
