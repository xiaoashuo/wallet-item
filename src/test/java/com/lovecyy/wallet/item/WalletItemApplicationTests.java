package com.lovecyy.wallet.item;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.benmanes.caffeine.cache.Cache;
import com.lovecyy.wallet.item.common.convert.TWalletConvert;
import com.lovecyy.wallet.item.contracts.TokenERC20;
import com.lovecyy.wallet.item.model.dto.KeystoreDto;
import com.lovecyy.wallet.item.common.utils.*;
import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import com.lovecyy.wallet.item.model.pojo.TWalletRepository;
import com.lovecyy.wallet.item.service.TWalletRepositoryService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@ActiveProfiles("dev")
class WalletItemApplicationTests {

    @Autowired
    Web3j web3j;

    @Autowired
    private Cache caffeineCache;


    public String walletKeyStorePath="E:\\geth\\keysotre";

    @Autowired
    private TWalletRepositoryService tWalletRepositoryService;

    @Test
    public void getAvaliableAccount(){
        TWalletRepository avaliableAccount = tWalletRepositoryService.getAvaliableAccount();
        System.out.println(avaliableAccount);
    }

    @Test
    public void initWallet() throws Exception {
        for (int i = 0; i < 50; i++) {
            KeystoreDto wallet = web3JUtil.createWalletWithMnemonic("123456", walletKeyStorePath);
            String content = wallet.getContent();
            String filename = wallet.getFilename();
            String mnemonic = wallet.getMnemonic();
            Credentials credentials = web3JUtil.openWalletByJSON("123456", content);
            String address = credentials.getAddress();
            String walletPrivateKey = web3JUtil.getWalletPrivateKey(credentials);
            Date date = new Date();
            TWalletRepository build = TWalletRepository.builder().address(address)
                    .mnemonic(mnemonic).name(filename).password("123456").privateKey(walletPrivateKey)
                    .keyStore(content).build();
            tWalletRepositoryService.save(build);
        }



    }
    @Test
    public void testCache() throws InterruptedException {
        caffeineCache.put("test","12222");

        Object test = caffeineCache.asMap().get("test");
        System.out.println(test);
        Thread.sleep(6000);
        Object test1 = caffeineCache.asMap().get("test");
        System.out.println(test1);


    }

    @Test
    void contextLoads() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        System.out.println("当前块数"+blockNumber.getBlockNumber());
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        System.out.println("当前天然器价格"+ethGasPrice.getGasPrice());
    }
    @Autowired
    private Web3JUtilWrapper web3JUtil;



    @Test
    public void getInfo() throws IOException, ExecutionException, InterruptedException {
//        TokenERC20 load = web3JUtil.load("0x54ecba37ec3fcbf3382dce9225233ca036256e01");
//        System.out.println(load.isValid());
      //  new TokenERC20Util()
        String contractAddress="0xb58b768dde60bee6288e68435e7ab4a7d27465be";
//        String tokenName = TokenUtil.getTokenName(web3j, contractAddress);
//        String tokenSymbol = TokenUtil.getTokenSymbol(web3j, contractAddress);
//        int tokenDecimals = TokenUtil.getTokenDecimals(web3j, contractAddress);
//        BigInteger tokenTotalSupply = TokenUtil.getTokenTotalSupply(web3j, contractAddress);
//        System.out.println("token 名称"+tokenName);
//        System.out.println("token 符号"+tokenSymbol);
//        System.out.println("token 精度"+tokenDecimals);
//        System.out.println("token 总发行量"+tokenTotalSupply);
//        ContractInfo contractInfo = TokenUtil.getContractInfo(web3j, contractAddress);
//        System.out.println(contractInfo);
        BigInteger tokenBalance = TokenUtil.getTokenBalance(web3JUtil.getWeb3j(), "0x122d22fb59d8b376e4ed60eb87c5b87b5c38a5ef", contractAddress);
        BigInteger numberRemoveDecimal = FormatConvert.getNumberRemoveDecimal(tokenBalance, TokenUtil.getTokenDecimals(web3j, contractAddress));
        System.out.println(numberRemoveDecimal);
        BigInteger tokenBalance1 = TokenUtil.getTokenBalance(web3JUtil.getWeb3j(), "0xefa643de227ee76db5478b4bb17919daccdcb967",  contractAddress);
        BigInteger numberRemoveDecima2 = FormatConvert.getNumberRemoveDecimal(tokenBalance1, TokenUtil.getTokenDecimals(web3j, contractAddress));

        System.out.println(numberRemoveDecima2);
//        String hash = TokenUtil.signTokenTransaction(web3j, "0x122d22fb59d8b376e4ed60eb87c5b87b5c38a5ef", "0xefa643de227ee76db5478b4bb17919daccdcb967",
//                BigDecimal.TEN, "ba5b8be73d4b39b7f38b88f67ebe1ea10616e9fc717622c17acb3a560ecbe848",
//                contractAddress);
//        System.out.println(hash);

    }

    @Test
    public void getAccounts() throws IOException {
        List<String> accountList = web3JUtil.getAccountList();
        System.out.println(accountList);
    }

    @Test
    public void getBalance(){
        //String address="0x30cA9a3A45919659478D35466b8FDaD1F704768e";
        String address="0x122d22fb59d8b376e4ed60eb87c5b87b5c38a5ef";
        BigDecimal balanceByAddress = web3JUtil.getBalanceByAddress(address);
        System.out.println(balanceByAddress);
    }
    @Test
    public void getTransactionCount() throws IOException {
       // String address="0x30cA9a3A45919659478D35466b8FDaD1F704768e";
        String address="0x122d22fb59d8b376e4ed60eb87c5b87b5c38a5ef";
        BigInteger transactionCount = web3JUtil.getTransactionCount(address);
        System.out.println(transactionCount);
    }


    @Test
    public void getBlockNumber() throws Exception {
//        BigInteger blockNumber = web3JUtil.getBlockNumber();
//        System.out.println(blockNumber);
//        EthBlock.Block blockByNumber = web3JUtil.getBlockByNumber(BigInteger.valueOf(2));
//        System.out.println(blockByNumber);
        TokenERC20 load = web3JUtil.load("0xf30De73989Cb7ecD7Cab085e60a37d89D9382094");
        String send = load.name().send();
        System.out.println(send);

//        System.out.println(load.isValid());
//        String tokenName = getTokenName(web3j, "0xf30De73989Cb7ecD7Cab085e60a37d89D9382094");
//        System.out.println(tokenName);
    }


    @Test
    public void loadKeyStore() throws Exception {
//        String walletPassword="123456";
//        String walletDirectory="E:\\geth\\privatechain\\privateKeyStore";
//        String walletName="UTC--2020-09-03T03-04-56.595000000Z--122d22fb59d8b376e4ed60eb87c5b87b5c38a5ef.json";
//        Credentials credentials = web3JUtil.openWallet(walletName, walletPassword, walletDirectory);
//        String address = credentials.getAddress();
////        System.out.println(address);
////        String privateKey = web3JUtil.getWalletPrivateKey(walletName, walletPassword, walletDirectory);
////        System.out.println(privateKey);
//        String deploy = web3JUtil.deploy(credentials, Convert.toWei("22", Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(4000000), "zhangsan", "z",BigInteger.TEN , BigInteger.TEN.multiply(BigInteger.TEN));
//        System.out.println(deploy);
        String key="{\"address\":\"95ac5db4b65196a31af016d94ea1ba388da38405\",\"id\":\"866fb551-9e11-4a62-b265-628b82bf43e6\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"a9b9240665fea8aa0c4eec5e3e4b85fd6cb086e092254f811b2edbe6c6bf214d\",\"cipherparams\":{\"iv\":\"802bd1e2606acf9a5f3ab3ad5eea0da6\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"8a95fb646d0b2c748607f23175d94bd0e10934716c4fa43c7ca7d06651f9055f\"},\"mac\":\"7dd0b16004bffeb4d0eacec3b6df3edb045fe3eda51931da15f89ef19b2edaf0\"}}";
        Credentials credentials = web3JUtil.openWalletByJSON("12345678", key);
        System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));

    }

    @Test
    public void generateMnemonic(){
        String password="123456";
        String mnemonic = "candy maple cake sugar pudding cream honey rich smooth crumble sweet treat";

        Credentials credentials = web3JUtil.openWalletByMnemonic(password, mnemonic);
        System.out.println(credentials.getAddress());
        System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println(credentials.getEcKeyPair().getPublicKey().toString(16));
        String derivationPassword = null; // no encryption
        String derivationMnemonic = "candy maple cake sugar pudding cream honey rich smooth crumble sweet treat";

        Credentials credentials1 = web3JUtil.openWalletByDerivationPath(derivationPassword, derivationMnemonic);
        System.out.println(credentials1.getAddress());
        System.out.println(credentials1.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println(credentials1.getEcKeyPair().getPublicKey().toString(16));
        Credentials credentials2 = web3JUtil.openWalletByPrivateKey("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");
        System.out.println("---------------------");
        System.out.println(credentials2.getAddress());
        System.out.println(credentials2.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println(credentials2.getEcKeyPair().getPublicKey().toString(16));
    }

    @Test
    public void createWallet(){
        String password="";
        String walletDirectory="E:\\geth\\privatechain\\privateKeyStore";
        try {
            KeystoreDto wallet = web3JUtil.createWallet(password, walletDirectory);
            System.out.println(wallet);
            KeystoreDto walletWithMnemonic = web3JUtil.createWalletWithMnemonic(password, walletDirectory);
            System.out.println(walletWithMnemonic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void  gasInfo() throws IOException {
//        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//        System.out.println(gasPrice);
//        BigInteger bigInteger = FormatConvert.WeiTOGWei(gasPrice);
//        System.out.println(bigInteger);
        TransactionReceipt transactionReceiptByHash = web3JUtil.getTransactionReceiptByHash("0xb0958a298380de120130f14d9261a828ed30fe5cfba2f6f27e8abd84f8dc2104");
        System.out.println(transactionReceiptByHash);
        EthTransaction transactionByHash = web3JUtil.getTransactionByHash("0xb0958a298380de120130f14d9261a828ed30fe5cfba2f6f27e8abd84f8dc2104");
        System.out.println(transactionByHash);
    }

    @Test
    public void eventTest() throws InterruptedException {
        web3JUtil.eventContractListener(web3j,"0xccd0b20da757d26d74ce042d7c5b348cf0cacc70");
        Thread.sleep(5000000);
    }

    @Test
    public void getTrx(){
        TransactionReceipt transactionReceiptByHash = web3JUtil.getTransactionReceiptByHash("0x9d625da7038c5cdd578b6d87a3c7eb687d059939979ea874a9a40e0ad7868ae1");

        EthTransaction transactionByHash = web3JUtil.getTransactionByHash("0x9d625da7038c5cdd578b6d87a3c7eb687d059939979ea874a9a40e0ad7868ae1");
        System.out.println(transactionByHash);
    }
}
