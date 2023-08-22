package com.jxm.upstage.controller;

import com.alibaba.excel.EasyExcel;
import com.jxm.common.api.CommonResult;
import com.jxm.common.api.ResultCode;
import com.jxm.upstage.Listener.UserExcelListener;
import com.jxm.upstage.dto.ExcelUser;
import com.jxm.upstage.utils.SpringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/excel")
public class ExportUserExcel {

    @RequestMapping(value = "/importUser", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> saveSubTemplateData(MultipartFile file) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(file.getInputStream(), ExcelUser.class,  SpringUtil.getBean(UserExcelListener.class)).sheet().headRowNumber(2).doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取当前线程的request对象
        HttpSession httpSession = request.getSession();
        Integer success = (Integer) httpSession.getAttribute("success"),
                fail = (Integer) httpSession.getAttribute("fail");

        //String   err=(String)httpSession.getAttribute("error");    todo 返回模板第几行第几列格式不正确的信息

        String msg = "成功" + success + "条" + " " + "失败" + fail + "条!";
        if(success == 0)
            return CommonResult.success("导入失败，请按照规范填写信息");
        return CommonResult.success(msg);
    }

}
