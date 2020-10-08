package com.lovecyy.wallet.item;
import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.lovecyy.wallet.item.common.convert.TWalletConvert;
import com.lovecyy.wallet.item.common.utils.FormatConvert;
import com.lovecyy.wallet.item.common.utils.Web3JUtil;
import com.lovecyy.wallet.item.model.dto.GasInfoDTO;
import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;

public class Web3JClientTest {

    public static void main(String[] args) throws IOException {
        BigInteger zero = BigInteger.ZERO;
        BigInteger one = BigInteger.ONE;
        System.out.println(one.compareTo(zero)<=0);
//        GasInfoDTO gasInfoDTO = Web3JUtil.gasInfo();
//        System.out.println(gasInfoDTO);

//        Document document = Jsoup.connect("https://cn.etherscan.com/gastracker").get();
//        Elements gasTracker = document.select("div[class='row text-center mb-3']");
//
//        String lowPrice = gasTracker.select("div[class='h4 text-success mb-1']").text();
//        String lowTime = gasTracker.select("div[class='text-secondary']").first().text();
//        String avgPrice = gasTracker.select("span[class='h4 text-primary mb-1']").text();
//        String avgTime = gasTracker.select("div[class='text-secondary']").eq(1).text();
//        String highPrice = gasTracker.select("span[class='h3 mb-0']").text();
//        String highTime = gasTracker.select("div[class='text-secondary']").last().text();
//        System.out.println("low "+lowPrice+"--"+lowTime);
//        System.out.println("avg "+avgPrice+"--"+avgTime);
//        System.out.println("high "+highPrice+"--"+highTime);

    }
}
