package com.lovecyy.wallet.item.controller;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.lovecyy.wallet.item.common.enums.ResultCodes;
import com.lovecyy.wallet.item.common.utils.JWTUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final Cache loginCache;


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
            Map<String,Object> resultMap=new HashMap<>();
            UserDTO userDTO = tUsersService.login(userQO);
            Object o = loginCache.asMap().get(userDTO.getId());
            if (o!=null){
                resultMap.put("token",o);
               // return R.ok("登录成功",resultMap);
                return R.fail("用户已在其他地方登录");
            }
            String jsonStr = JSONUtil.toJsonStr(userDTO);
            String md5Str = JWTUtil.generateToken(jsonStr);
            loginCache.put(userDTO.getId(),md5Str);
            loginCache.put(md5Str,jsonStr);
            resultMap.put("token",md5Str);
            return R.ok("登录成功",resultMap);
        } catch (Exception e) {
            log.error("登录状态异常",e);
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("logout")
    public R logout(){
        TUsers user = getUser();
        if (user==null){
            return R.fail(ResultCodes.TOKEN_EXPIRE);
        }
        if (!StringUtils.isEmpty(loginCache.asMap().get(user.getId()))){
            String md5Str = (String) loginCache.asMap().get(user.getId());

            loginCache.asMap().remove(md5Str);
            loginCache.asMap().remove(user.getId());


        }
        return R.ok();

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
            return R.fail(e.getMessage());
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
