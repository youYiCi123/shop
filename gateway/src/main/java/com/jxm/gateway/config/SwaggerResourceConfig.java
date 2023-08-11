package com.jxm.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger资源配置
 * 可以从一个统一的入口访问接口文档，然后通过路由转发将实际请求转发到对应的微服务上
 * 统一的入口就是网关的入口。再根据服务名转发到不同的微服务中的swagger-ui。
 *
 * 1. @Primary作用: 在spring 中使用注解，常使用@Autowired， 默认是根据类型Type来自动注入的。但有些特殊情况，对同一个接口，
 * 可能会有几种不同的实现类，而默认只会采取其中一种。Spring无法确定具体注入的类（有多个实现，不知道选哪个）。当给指定的组件添加@Primary是，默认会注入@Primary配置的组件
 */
@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;

    /**
     * 1. swagger文档信息实际上是通过v2/api-docs这个接口获取的，这个接口是swagger自带的，调用该接口返回的json数据，
     * 就是我们要在页面中展示的接口文档数据。所以我们通过网关来实现swagger的接口转发，实际上转发的就是v2/api-docs接口。
     * 2. swagger-resources获取的是本服务的api-docs访问路径，我们可以通过重写这个接口实现获取到所有微服务的api-docs访问路径。
     *
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //获取所有路由的ID，即在application.yml中spring.cloud.gateway.routes.id
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //过滤出配置文件中定义的路由->过滤出Path Route Predicate->根据路径拼接成api-docs路径->生成SwaggerResource
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).forEach(route -> {
            route.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                    .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                    .replace("**", "v2/api-docs"))));
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
