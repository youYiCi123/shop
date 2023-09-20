package com.jxm.upstage.service;



import com.jxm.common.api.CommonResult;
import com.jxm.common.domain.UserDto;
import com.jxm.upstage.dto.UmsAdminLoginParam;
import com.jxm.upstage.dto.UmsAdminParam;
import com.jxm.upstage.dto.UpdateAdminPasswordParam;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;


/**
 * 后台管理员Service
 * Created by macro on 2018/4/26.
 */
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 获取用户对应的角色
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<UmsAdmin> list(String keyword, Long depId,Integer pageSize, Integer pageNum);

    /**
     * 根据用户id获取用户
     */
    UmsAdmin getItem(Long id);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);
    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 批量删除指定用户
     */
    @Transactional
    int deleteBatch(List<Long> idList);


    /**
     * 修改指定用户信息
     */
    int update(Long id, UmsAdmin admin);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);


    /**
     * 登录功能
     * @return 调用认证中心返回结果
     */
    CommonResult login(UmsAdminLoginParam umsAdminLoginParam);


    /**
     * 获取当前登录后台用户
     */
    UmsAdmin getCurrentAdmin() throws ParseException;

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 获取缓存服务
     */
    UmsAdminCacheService getCacheService();

    /**
     * 获取所有用户手机号
     */
    List<String> getAllUserPhone();

    /**
     * 批量保存用户信息
     */
    int saveBatch(List<UmsAdmin> umsAdmins);

}
