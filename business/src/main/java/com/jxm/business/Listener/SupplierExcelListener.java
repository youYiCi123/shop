package com.jxm.business.Listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.jxm.business.dto.ExcelCustom;
import com.jxm.business.dto.ExcelSupplier;
import com.jxm.business.service.ExportExcelHandleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@Component
@Scope("prototype")
@Slf4j
public class SupplierExcelListener extends AnalysisEventListener<ExcelSupplier> {
    @Autowired
    private ExportExcelHandleService exportExcelHandleService;

    HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

    private final static Integer SIZE = 1000; //每一千条5数据导一次

    //缓存
    List<ExcelSupplier> linkSupplierList = new ArrayList<>();

    int currentRow;

    Class clazz= Class.forName("com.jxm.business.dto.ExcelSupplier");

    public SupplierExcelListener() throws ClassNotFoundException {
        super();
    }

    @Override
    public void invoke(ExcelSupplier data, AnalysisContext context) {
        if(data.getSupplierName()!=null)
            linkSupplierList.add(data);
        if (linkSupplierList.size() % SIZE == 0) {
            exportExcelHandleService.saveSupplierInfo(linkSupplierList);
            linkSupplierList.clear();//清空
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        exportExcelHandleService.saveSupplierInfo(linkSupplierList);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        currentRow=context.readRowHolder().getRowIndex();
        if(currentRow==0){
            if( !headMap.get(0).equals("合肥天一生物技术研究所有限责任公司"))            //excel第一行表头数据内容
                throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
        }else {
            Map<Integer, String> head = new HashMap<>();
            try {
                head = getIndexNameMap(clazz);   //通过class获取到使用@ExcelProperty注解配置的字段
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Set<Integer> keySet = head.keySet();  //解析到的excel表头和实体配置的进行比对
            for (Integer key : keySet) {
                if (StringUtils.isEmpty(headMap.get(key))) {

                    // request.getSession().setAttribute("error", "表头第" + key + 1 + "列为空，请参照模板填写");  //todo 是否需要看情况吧
                    throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                }
                if (!headMap.get(key).equals(head.get(key))) {

                    //request.getSession().setAttribute("error", "表头第" + key + 1 + "列【" + headMap.get(key) + "】与模板【" + head.get(key) + "】不一致，请参照模板填写");
                    throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                }
            }
        }
    }


    public Map<Integer, String> getIndexNameMap(Class clazz) throws NoSuchFieldException {
        Map<Integer, String> result = new HashMap<>();
        Field field;
        Field[] fields = clazz.getDeclaredFields();     //获取类中所有的属性
        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);//获取根据注解的方式获取ExcelProperty修饰的字段
            if (excelProperty != null) {
                int index = excelProperty.index();         //索引值
                String[] values = excelProperty.value();   //字段值
                StringBuilder value = new StringBuilder();
                for (String v : values) {
                    value.append(v);
                }
                result.put(index, value.toString());
            }
        }
        return result;
    }
}
