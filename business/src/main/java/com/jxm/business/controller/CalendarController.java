package com.jxm.business.controller;

import com.jxm.business.dto.CalendarDto;
import com.jxm.business.dto.CalendarUserDto;
import com.jxm.business.model.CalendarParam;
import com.jxm.business.model.CertificateParam;
import com.jxm.business.service.CalendarService;
import com.jxm.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @ApiOperation(value = "添加日程")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody CalendarUserDto calendarUserDto) throws ParseException {
        int count= calendarService.add(calendarUserDto);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改日程")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@RequestBody CalendarUserDto calendarUserDto){
        int count= calendarService.update(calendarUserDto);
        if (count<0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "查询指定用户的日程")
    @RequestMapping(value = "/query/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CalendarDto>> queryContent(@PathVariable Long id){
        return CommonResult.success(calendarService.queryContent(id));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        //删除日程信息
        int count=calendarService.delete(id);
        if(count<0)
            return CommonResult.failed("删除日程信息错误");
        return CommonResult.success();
    }
}
