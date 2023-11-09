package com.jxm.business.controller;

import com.alibaba.excel.EasyExcel;
import com.jxm.business.Listener.CertificateExcelListener;
import com.jxm.business.Listener.CustomExcelListener;
import com.jxm.business.dto.ExcelCertificate;
import com.jxm.business.dto.ExcelCustom;
import com.jxm.business.utils.SpringUtil;
import com.jxm.common.api.CommonResult;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/excel")
public class ExportBusinessExcel {

    @RequestMapping(value = "/importCustom", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> saveSubTemplateData(MultipartFile file) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(file.getInputStream(), ExcelCustom.class,  SpringUtil.getBean(CustomExcelListener.class)).sheet().headRowNumber(2).doRead();
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

    @RequestMapping(value = "/download/custom", method = RequestMethod.GET)
    @ResponseBody
    public void downloadCustomFile(HttpServletResponse response) {
        // 读取resource目下文件
        String templatePath = "classpath:files/custom.xlsx";
        String filename = "客户模板.xlsx";
        File file = null;
        try {
            file = ResourceUtils.getFile(templatePath);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在 "+ filename);
            // todo, 可以在流中返回“文件不存在“，这样用户可以下载到文件，但是内容为”文件不存在”
            return;
        }

        if (file.isFile()) {
            byte[] fileNameBytes = filename.getBytes(StandardCharsets.UTF_8);
            filename = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.UTF_8);

            response.reset();
            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                System.out.println("下载出错"+filename+"，错误原因 "+e.getMessage());
            }
        } else {
            System.out.println("文件不存在 "+ filename);
            // todo, 可以在流中返回“文件不存在“，这样用户可以下载到文件，但是内容为”文件不存在”
        }
    }

    @RequestMapping(value = "/importCertificate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> saveCertificateTemplateData(MultipartFile file) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(file.getInputStream(), ExcelCertificate.class,  SpringUtil.getBean(CertificateExcelListener.class)).sheet().headRowNumber(2).doRead();
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

    @RequestMapping(value = "/download/certificate", method = RequestMethod.GET)
    @ResponseBody
    public void downloadCertificateFile(HttpServletResponse response) {
        // 读取resource目下文件
        String templatePath = "classpath:files/certificate.xlsx";
        String filename = "证书模板.xlsx";
        File file = null;
        try {
            file = ResourceUtils.getFile(templatePath);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在 "+ filename);
            // todo, 可以在流中返回“文件不存在“，这样用户可以下载到文件，但是内容为”文件不存在”
            return;
        }
        if (file.isFile()) {
            byte[] fileNameBytes = filename.getBytes(StandardCharsets.UTF_8);
            filename = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.UTF_8);

            response.reset();
            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                System.out.println("下载出错"+filename+"，错误原因 "+e.getMessage());
            }
        } else {
            System.out.println("文件不存在 "+ filename);
            // todo, 可以在流中返回“文件不存在“，这样用户可以下载到文件，但是内容为”文件不存在”
        }
    }

}
