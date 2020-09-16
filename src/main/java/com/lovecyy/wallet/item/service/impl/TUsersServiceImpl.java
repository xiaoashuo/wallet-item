package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lovecyy.wallet.item.common.convert.TUserConvert;
import com.lovecyy.wallet.item.common.exceptions.UserException;
import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.qo.UserQO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.mapper.TUsersMapper;
import com.lovecyy.wallet.item.service.TUsersService;
import org.springframework.util.Assert;

@Service
public class TUsersServiceImpl extends ServiceImpl<TUsersMapper, TUsers> implements TUsersService{


    @Override
    public boolean create(UserQO userQO) {
        QueryWrapper<TUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper .eq(TUsers.COL_USERNAME,userQO.getUsername());
        Integer count = this.getBaseMapper().selectCount(queryWrapper);
        Assert.isTrue(count<=0,"用户已存在");
        Date date = new Date();
        userQO.setStatus(0);
        userQO.setGmtCreate(date);
        userQO.setGmtModified(date);
        TUsers tUsers = TUserConvert.INSTANCE.QOTOPO(userQO);
        boolean result = SqlHelper.retBool(this.getBaseMapper().insert(tUsers));
        return result;
    }

    @Override
    public UserDTO login(UserQO userQO) {
        String username = userQO.getUsername();
        String password = userQO.getPassword();
        QueryWrapper<TUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TUsers.COL_USERNAME,username);
        TUsers tUsers = this.getBaseMapper().selectOne(queryWrapper);
        if (tUsers==null||!tUsers.getPassword().equals(password)){
            throw new UserException("用户名或密码错误");
        }
        UserDTO userDTO = TUserConvert.INSTANCE.POTODTO(tUsers);
        return userDTO;
    }
}
