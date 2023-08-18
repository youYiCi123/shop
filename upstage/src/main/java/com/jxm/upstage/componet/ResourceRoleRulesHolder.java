package com.jxm.upstage.componet;

import com.jxm.upstage.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 资源与角色访问对应关系操作组件
 * Created by macro on 2020/7/17.
 */
@Component
public class ResourceRoleRulesHolder {
    @Autowired
    private UmsResourceService resourceService;

    /**
     * PostConstruct是Java自带的注解，在方法上加该注解会在项目启动的时候执行该方法，
     * 也可以理解为在spring容器初始化的时候执行该方法。可作为一些数据的常规化加载，比如数据字典之类的。
     */
    @PostConstruct
    public void initResourceRolesMap(){
        resourceService.initResourceRolesMap();
    }
}
