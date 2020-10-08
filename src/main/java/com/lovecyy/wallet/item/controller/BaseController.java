package com.lovecyy.wallet.item.controller;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.lovecyy.wallet.item.common.utils.JWTUtil;
import com.lovecyy.wallet.item.common.utils.SpringUtils;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import org.springframework.util.Assert;

public class BaseController {

    private static final Cache loginCache= SpringUtils.getBean("loginCache");


    public TUsers getUser(){
        String token = JWTUtil.getToken();
        Assert.hasText(token,"请求头丢失");
        String o = (String) loginCache.asMap().get(token);
        Assert.notNull(o,"token已失效,请重新登录");
        return JSONUtil.toBean(o,TUsers.class);
    }
}
