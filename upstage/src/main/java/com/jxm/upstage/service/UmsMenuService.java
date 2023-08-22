package com.jxm.upstage.service;





import com.jxm.upstage.dto.UmsMenuNode;

import java.util.List;

/**
 * 后台菜单管理Service
 * Created by macro on 2020/2/2.
 */
public interface UmsMenuService {

    /**
     * 树形结构返回所有菜单列表
     */
    List<UmsMenuNode> treeList();

}
