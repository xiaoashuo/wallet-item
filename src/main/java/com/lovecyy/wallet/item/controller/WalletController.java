package com.lovecyy.wallet.item.controller;

import cn.hutool.json.JSONUtil;
import com.lovecyy.wallet.item.model.dto.AccountDTO;
import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.dto.TokenDTO;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.TokenQO;
import com.lovecyy.wallet.item.service.TWalletService;
import com.lovecyy.wallet.item.common.utils.Web3JUtilWrapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包controller
 * @author Yakir
 */
@RequiredArgsConstructor
@RequestMapping("wallet")
@RestController
@Validated
public class WalletController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(WalletController.class);


    private final TWalletService tWalletService;

    @PostMapping
    public R createWallet(@Length(min = 8,max = 50,message = "密码长度不正确") String pwd){
        try {
            TUsers user = getUser();
            TWalletDto wallet = tWalletService.createWallet(user.getId(), pwd);
            return R.ok(wallet);
        } catch (Exception e) {
            log.error("创建钱包失败",e);
            return R.fail("钱包创建失败");
        }
    }
    @GetMapping
    public R getWallets(){
        TUsers user = getUser();
        try {
            List<TWalletDto> wallets = tWalletService.getWallets(user.getId());
            return R.ok(wallets);
        } catch (Exception e) {
            log.error("获取钱包列表错误",e);
            return R.fail("获取钱包列表失败"+e.getLocalizedMessage());
        }
    }

    @DeleteMapping
    public R delWallet(@NotNull(message = "钱包id不能为空") Integer id){
        try {
            TUsers user = getUser();
            boolean result = tWalletService.delWalletById(user.getId(), id);
            return result?R.ok("删除成功"):R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除钱包失败",e);
            return R.fail("删除失败");
        }
    }

    @GetMapping("balance")
    public R balance(String address){
        try {
            AccountDTO accountDTO = tWalletService.balance(address);
            return R.ok(accountDTO);
        } catch (Exception e) {
            log.error("获取钱包余额失败",e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * eth代币转账
     * @param tokenQO
     * @return
     */
    @PostMapping("transfer")
    public R transfer(TokenQO tokenQO){
        Integer userId = getUser().getId();
        try {
            TokenDTO tokenDTO = tWalletService.transfer(userId, tokenQO);
            return R.ok(tokenDTO);
        } catch (Exception e) {
            log.error("ETH转账失败",e);
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("/export/{address}")
    public void export(HttpServletResponse response,@PathVariable String address){
        ServletOutputStream outputStream=null;
        try {
            TUsers user = getUser();
            Integer id = user.getId();
            TWallet wallet = tWalletService.getWallet(id, address);
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            String fileName = address + ".txt";
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
             outputStream = response.getOutputStream();
            String s = JSONUtil.toJsonStr(wallet);
            outputStream.write(s.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            log.error("导出密钥文件失败",e);
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
