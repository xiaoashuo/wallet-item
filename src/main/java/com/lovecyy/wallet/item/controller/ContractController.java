package com.lovecyy.wallet.item.controller;

import com.lovecyy.wallet.item.common.utils.Web3JUtil;
import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.pojo.ContractInfo;

import com.lovecyy.wallet.item.model.pojo.TContract;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.qo.ContractQO;
import com.lovecyy.wallet.item.service.TContractService;
import com.lovecyy.wallet.item.service.TWalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.util.List;

/**
 * 合约controller
 * @author Yakir
 */
@RequestMapping("/contract")
@RestController
@Validated
@RequiredArgsConstructor
public class ContractController extends BaseController {

    private static final Logger log= LoggerFactory.getLogger(ContractController.class);


    private final TContractService tContractService;
    private final TWalletService tWalletService;

    /**
     * 部署合约
     * @param contractQo
     * @return
     */
    @PostMapping("/deploy")
    public R deploy(ContractQO contractQo){
        try {
            TUsers user = getUser();
            contractQo.setUserId(user.getId());
            TWallet wallet = tWalletService.getWallet(user.getId(), contractQo.getAddress());
            if (wallet==null){
                return R.fail("钱包地址不存在");
            }
            String txHash=tContractService.deploy(contractQo,wallet);
            return R.ok(R.CommonResponse.SUCCESS,txHash);
        } catch (Exception e) {
            log.error("合约部署失败",e);
            return R.fail("合约部署失败"+e.getLocalizedMessage());
        }
    }

    /**
     * 合约地址
     * @param address 合约地址
     * @return
     */
    @GetMapping("/info")
    public R info(String address){
        try {

            Integer id = getUser().getId();
            ContractInfo info = tContractService.info(id,address);
            return R.ok(info);
        } catch (Exception e) {
            log.error("获取合约信息失败",e);
            return R.fail("获取合约信息失败"+e.getLocalizedMessage());
        }
    }

    @GetMapping("list")
    public R list(){
        try {
            TUsers user = getUser();
            Integer userId = user.getId();
            List<TContract> list= tContractService.listByUserId(userId);
            return R.ok(list);
        } catch (Exception e) {
            log.error("根据用户id查询合约列表出现异常",e);
            return R.fail(e.getMessage());
        }
    }



}
