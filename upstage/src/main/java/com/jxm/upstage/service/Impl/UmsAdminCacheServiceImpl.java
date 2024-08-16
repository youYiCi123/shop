package com.jxm.upstage.service.Impl;

import com.jxm.common.service.RedisService;
import com.jxm.upstage.common.StringUtils;
import com.jxm.upstage.dto.OnlineUmsAdmin;
import com.jxm.upstage.model.UmsAdmin;
import com.jxm.upstage.service.UmsAdminCacheService;
import com.jxm.upstage.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UmsAdminCacheService实现类
 * Created by macro on 2020/3/13.
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RedisService redisService;
    @Value("${spring.redis.database}")
    private String REDIS_DATABASE;
    @Value("${spring.redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${spring.redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${spring.redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public OnlineUmsAdmin getAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + adminId;
        return (OnlineUmsAdmin) redisService.get(key);
    }

    /**
     * 查询全部数据，不分页
     */
    public List<OnlineUmsAdmin> getAll(){
        List<String> keys = redisService.scan(REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + "*");
        Collections.reverse(keys);
        List<OnlineUmsAdmin> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            OnlineUmsAdmin onlineUserDto = (OnlineUmsAdmin) redisService.get(key);
            onlineUserDtos.add(onlineUserDto);
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    @Override
    public void setAdmin(OnlineUmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getId();
        redisService.set(key, admin, REDIS_EXPIRE);
    }
}
