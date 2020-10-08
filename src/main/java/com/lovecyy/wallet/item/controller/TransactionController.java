package com.lovecyy.wallet.item.controller;

import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.TransactionDTO;
import com.lovecyy.wallet.item.model.pojo.TTransaction;
import com.lovecyy.wallet.item.model.qo.TransactionQO;
import com.lovecyy.wallet.item.service.TTransactionService;
import jnr.ffi.annotations.In;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController extends BaseController {


    private static final Logger log=LoggerFactory.getLogger(TransactionController.class);

    private final TTransactionService transactionService;

    /**
     * 获取当前类型所有交易
     * @param transactionQO
     * @return
     */
    @PostMapping("list")
    public R listByTypeAndPage(@RequestBody @Valid TransactionQO transactionQO){
        try {
            TransactionDTO transactionDTO = transactionService.listByTypeAndPage(transactionQO);
            return R.ok(transactionDTO);
        } catch (Exception e) {
            log.error("获取当前所有类型交易出错",e);
            return R.fail(e.getMessage());
        }
    }
}
