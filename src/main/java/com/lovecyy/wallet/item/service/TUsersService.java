package com.lovecyy.wallet.item.service;

import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lovecyy.wallet.item.model.qo.UserQO;

public interface TUsersService extends IService<TUsers>{

    /**
     * 创建用户
     * @param userQO
     * @return true|false
     */
    boolean create(UserQO userQO);

    /**
     * 用户登录
     * @param userQO
     * @return
     */
    UserDTO login(UserQO userQO);


}
