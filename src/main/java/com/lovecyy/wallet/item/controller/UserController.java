package com.lovecyy.wallet.item.controller;

import cn.hutool.json.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.lovecyy.wallet.item.common.enums.ResultCodes;
import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.pojo.TUserRelationContract;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.model.qo.UserQO;
import com.lovecyy.wallet.item.service.TUserRelationContractService;
import com.lovecyy.wallet.item.service.TUsersService;
import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(UserController.class);


    private final TUsersService tUsersService;

    private final TWalletService tWalletService;
    private final Cache caffeineCache;


    private final TUserRelationContractService tUserRelationContractService;
    @PostMapping
    public R create(UserQO userQO){
        try {
            return tUsersService.create(userQO)?R.ok("创建成功"):R.fail("创建失败");
        } catch (Exception e) {
            log.error("创建用户失败",e);
           return R.fail(e.getMessage());
        }
    }

    @PostMapping("login")
    public R login(UserQO userQO){
        try {
            UserDTO userDTO = tUsersService.login(userQO);
            return R.ok(userDTO);
        } catch (Exception e) {
            log.error("登录状态异常",e);
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("info")
    public R info(){
        try {
            TUsers currentUser = getUser();
            Integer id = currentUser.getId();
            Object result = caffeineCache.asMap().get(id);
            if (result!=null){
                return R.ok(result);
            }
            TUsers user = tUsersService.getById(id);
            List<TWalletDto> wallets = tWalletService.getWallets(user.getId());
            Map<String,Object> map=new HashMap<>();
            map.put("user",user);
            map.put("wallet",wallets);
            caffeineCache.put(id,map);
            return R.ok(map);
        } catch (Exception e) {
            log.error("获取用户个人信息失败",e);
            return R.fail("系统异常,联系客服");
        }
    }

   @GetMapping("/contract/list")
   public R contractList( String walletAddress){
       try {
           TUsers user = getUser();
           if (user==null){
               return R.fail(ResultCodes.TOKEN_EXPIRE);
           }
           Integer userId = user.getId();
           List<TUserRelationContract> tUserRelationContracts = tUserRelationContractService.listByUidAndWalletAddress(userId, walletAddress);
           return R.ok(tUserRelationContracts);
       } catch (Exception e) {
           return R.fail(e.getMessage());
       }


   }
}
