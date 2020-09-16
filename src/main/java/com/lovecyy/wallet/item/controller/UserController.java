package com.lovecyy.wallet.item.controller;

import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.model.qo.UserQO;
import com.lovecyy.wallet.item.service.TUsersService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(UserController.class);


    private final TUsersService tUsersService;

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
}
