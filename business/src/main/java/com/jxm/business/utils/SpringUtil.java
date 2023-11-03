package com.jxm.business.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工具类 方便在非spring管理环境中获取bean *
 *
 * BeanFactoryPostProcessor接口与 BeanPostProcessor接口类似,可以对bean的定义(配置元数据)进行处理；也就是spring ioc运行BeanFactoryPostProcessor
 * 在容器实例化任何其他的bean之前读取配置元数据,并有可能修改它；如果业务需要，可以配置多个BeanFactoryPostProcessor的实现类，
 * 通过"order"控制执行次序(要实现Ordered接口)
 *
 *当一个类实现了这个ApplicationContextAware接口之后，这个类就可以方便的获得ApplicationContext对象（spring上下文），Spring发现某个Bean实现了ApplicationContextAware接口，
 * Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContext（参数）方法
 */
@Component
public final class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware
{
    /** Spring应用上下文环境
     *
     * ConfigurableListableBeanFactory接口：继承AutowireCapableBeanFactory接口，AutowireCapableBeanFactory接口又继承了beanfactory接口，
     * 因此，configurableListableBeanFactory应用上下文环境可以通过bean的name或者clazz获取指定的bean；
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     *
     *
     * 获取ApplicationContext实例后，就可以像BeanFactory一样调用getBean(beanName)返回Bean了
     * BeanFactory在初始化容器时，并未实例化Bean，直到第一次访问某个Bean时才实例目标Bean；
     * 而ApplicationContext则在初始化应用上下文时就实例化所有单实例的Bean。因此ApplicationContext的初始化时间会比BeanFactory稍长一些，
     * 不过稍后的调用则没有”第一次惩罚”的问题。
     */
    private static ApplicationContext applicationContext;

    /**
     * 【这里需要知道 BeanFactoryPostProcessor 这个知识点，Bean 如果实现了此接口，
     * 那么在容器初始化以后，Spring 会负责调用里面的 postProcessBeanFactory 方法。】
     *
     *                 // 这里是提供给子类的扩展点，到这里的时候，所有的 Bean 都加载、注册完成了，但是都还没有初始化
     *                 // 具体的子类可以在这步的时候根据吱声业务添加或修改一些特殊的 beanFactory属性
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        SpringUtil.beanFactory = beanFactory;
    }

    /**
     * 我们都知道实现了ApplicationContextAware接口的类会被调用setApplicationContext方法，从而获取到spring容器的上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException
    {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws BeansException
     *
     */
    public static <T> T getBean(Class<T> clz) throws BeansException
    {
        T result = (T) beanFactory.getBean(clz);
        return result;
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name)
    {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     * AopContext.currentProxy获取代理对象，防止同一个类中的方法调用过程中没有执行事务
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker)
    {
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles()
    {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile()
    {
        final String[] activeProfiles = getActiveProfiles();
        if(activeProfiles.length!=0){
            return activeProfiles[0];
        }
        return null;
    }
}

