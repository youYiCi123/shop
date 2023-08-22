package com.jxm.upstage.dto;


import com.jxm.upstage.model.UmsMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 * Created by macro on 2020/2/4.
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    //"子级菜单"
    private List<UmsMenuNode> children;
}
