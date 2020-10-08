package com.lovecyy.wallet.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.lovecyy.wallet.item.common.convert.TUserConvert;
import com.lovecyy.wallet.item.common.exceptions.UserException;
import com.lovecyy.wallet.item.common.utils.SpringUtils;
import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.pojo.TWalletRepository;
import com.lovecyy.wallet.item.model.qo.UserQO;
import com.lovecyy.wallet.item.service.TUserRelationContractService;
import com.lovecyy.wallet.item.service.TWalletRepositoryService;
import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.mapper.TUsersMapper;
import com.lovecyy.wallet.item.service.TUsersService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class TUsersServiceImpl extends ServiceImpl<TUsersMapper, TUsers> implements TUsersService{


    private final TWalletService tWalletService;
    private final TWalletRepositoryService tWalletRepositoryService;
    private final TUserRelationContractService tUserRelationContractService;

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

    @Transactional
    public TUsers createUserAndWallet(String username,String password){
        //及用户不存在 则进行注册逻辑
        TUsers newUser = TUsers.builder().username(username).password(password).build();
        this.getBaseMapper().insert(newUser);
        //分配一个钱包
        TWalletRepository avaliableAccount = tWalletRepositoryService.getAvaliableAccount();
        Integer userId = newUser.getId();
        TWallet tWallet = TWallet.builder().uid(userId)
                .address(avaliableAccount.getAddress())
                .keyStore(avaliableAccount.getKeyStore())
                .mnemonic(avaliableAccount.getMnemonic())
                .password(avaliableAccount.getPassword())
                .name(avaliableAccount.getName()).privateKey(avaliableAccount.getPrivateKey()).build();
        tWalletService.save(tWallet);
        //绑定用户本币关系
        TUserRelationContract tUserRelationContract = TUserRelationContract.builder().contractName("ETH").contractDecimals(BigInteger.valueOf(18)).contractSymbol("ETH")
                .uid(userId).walletAddress(avaliableAccount.getAddress()).build();
        tUserRelationContractService.save(tUserRelationContract);
        return newUser;
    }

    @Override
    public UserDTO login(UserQO userQO) {
        String username = userQO.getUsername();
        String password = userQO.getPassword();
        QueryWrapper<TUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TUsers.COL_USERNAME,username);
        TUsers tUsers = this.getBaseMapper().selectOne(queryWrapper);
        if (tUsers==null){
            TUsers userAndWallet = SpringUtils.getAopProxy(this).createUserAndWallet(username, password);
            //返回数据
            UserDTO userDTO = TUserConvert.INSTANCE.POTODTO(userAndWallet);
            return userDTO;
        }

        if (!tUsers.getPassword().equals(password)){
            throw new UserException("用户名或密码错误");
        }
        UserDTO userDTO = TUserConvert.INSTANCE.POTODTO(tUsers);
        return userDTO;
    }
}
