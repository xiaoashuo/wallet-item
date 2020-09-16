package com.lovecyy.wallet.item.common.utils;

import com.lovecyy.wallet.item.contracts.TokenERC20;
import com.lovecyy.wallet.item.model.dto.KeystoreDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class Web3JUtilWrapper {
    private static final Logger log = LoggerFactory.getLogger(Web3JUtilWrapper.class);

    private final Web3j web3j;

    public Web3j getWeb3j() {
        return web3j;
    }

    /**
     * 根据地址获取账户余额
     * @param address 地址
     * @return 以外为单位
     */
    public BigDecimal getBalanceByAddress(String address) {
        return Web3JUtil.getBalanceByAddress(web3j,address);
    }

    /**
     * 获取指定块的余额
     *
     * @param address
     * @param blockNumber
     * @return
     */
    public BigDecimal getBalanceByAddress(String address, BigInteger blockNumber) throws IOException {
        return Web3JUtil.getBalanceByAddress(web3j, address, blockNumber);
    }

    /**
     * 得到账户最新的交易数
     * 帐户状态中还包括 nonce，这是一个序列号，表示帐户执行的交易数量。
     *
     * @param address
     * @return
     */
    public BigInteger getTransactionCount(String address) throws IOException {
         return Web3JUtil.getTransactionCount(web3j,address);
    }
    /**
     * 获得当前所有区块数量
     * 即当前区块高度
     */
    public   BigInteger getBlockNumber() throws IOException {
         return Web3JUtil.getBlockNumber(web3j);
    }

    /**
     * 获取块信息 通过指定块号
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public    EthBlock.Block getBlockByNumber(BigInteger blockNumber) throws IOException {
        return Web3JUtil.getBlockByNumber(web3j,blockNumber);
    }

    /**
     * 打开钱包
     *
     * @param walletName      钱包文件名 UTF...
     * @param walletPassword  钱包密码
     * @param walletDirectory keystore目录
     * @return
     */
    public Credentials openWallet(String walletName, String walletPassword, String walletDirectory) {
       return Web3JUtil.openWallet(walletName, walletPassword, walletDirectory);
    }
    /**
     * 打开钱包
     * @param walletPassword
     * @param content key store 内容
     * @return
     */
    public  Credentials openWalletByJSON(String walletPassword, String content) {
       return Web3JUtil.openWalletByJSON(walletPassword, content);
    }

    /**
     * 打开钱包
     * @param walletPassword
     * @param content
     * @return
     */
    public  Credentials openWallet(String walletPassword, String content) {
        return Web3JUtil.openWallet(walletPassword, content);
    }



    /**
     * 通过助记词装载钱包
     *
     * @param password
     * @param mnemonic
     * @return
     */
    public Credentials openWalletByMnemonic(String password, String mnemonic) {
        return Web3JUtil.openWalletByMnemonic(password, mnemonic);
    }

    /**
     * 装载钱包 通过派生路径
     *
     * @param password
     * @param mnemonic
     * @return
     */
    public Credentials openWalletByDerivationPath(String password, String mnemonic) {
         return Web3JUtil.openWalletByDerivationPath(password,mnemonic);
    }

    /**
     * 装载钱包 通过密钥
     *
     * @param privateKey
     * @return
     */
    public Credentials openWalletByPrivateKey(String privateKey) {
        return Web3JUtil.openWalletByPrivateKey(privateKey);
    }
    /**
     * 得到钱包私钥
     *
     * @param walletName
     * @param walletPassword
     * @param walletDirectory
     * @return
     */
    public String getWalletPrivateKey(String walletName, String walletPassword, String walletDirectory) {
        return Web3JUtil.getWalletPrivateKey(walletName, walletPassword, walletDirectory);
    }
    /**
     * 得到钱包私钥
     * @param credentials
     * @return
     */
    public  String getWalletPrivateKey(Credentials credentials) {
        return Web3JUtil.getWalletPrivateKey(credentials);
    }
    /**
     * 创建钱包
     *
     * @param password
     * @param directory
     * @return
     * @throws Exception
     */
    public KeystoreDto createWallet(String password, String directory) throws Exception {
        return Web3JUtil.createWallet(password, directory);
    }

    /**
     * 创建钱包带助记词 BIP39
     *
     * @param password
     * @param directory
     * @return
     * @throws Exception
     */
    public KeystoreDto createWalletWithMnemonic(String password, String directory) throws Exception {
        return Web3JUtil.createWalletWithMnemonic(password, directory);
    }

    /**
     * 获取账号列表
     */
    public  List<String> getAccountList() throws IOException {
       return Web3JUtil.getAccountList(web3j);
    }
    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    public   BigInteger getTransactionGasLimit(Transaction transaction) {
        return Web3JUtil.getTransactionGasLimit(web3j, transaction);
    }
    /**
     * 用来获取当前gas价格，该价格由最近的若干块 的gas价格中值决定。
     * @return wei为单位
     * @throws IOException
     */
    public BigInteger getGasPrice() throws IOException {
        return Web3JUtil.getGasPrice(web3j);
    }

    /**
     * 发送交易 离线签名
     *
     * @param credentials
     * @param toAddress
     * @param value
     * @return
     * @throws IOException
     */
    public String sendTransaction(Credentials credentials, String toAddress, BigDecimal value) throws Exception {
        return Web3JUtil.sendTransaction(web3j, credentials, toAddress, value);
    }

    /**
     * 转账以太币交易 自估算gas
     * @param credentials
     * @param toAddress
     * @param value
     * @param gasPrice
     * @return
     * @throws Exception
     */
    public String sendTransaction(Credentials credentials, String toAddress, BigDecimal value,BigDecimal gasPrice) throws Exception {

        return Web3JUtil.sendTransaction(web3j, credentials, toAddress, value,FormatConvert.GWeiTOWei(gasPrice));
    }

    /**
     * 部署合约
     * @param credentials
     * @param contractName
     * @param contractSymbol
     * @param contractDecimals
     * @param contractTotalSupply
     * @return
     */
    public String deploy(Credentials credentials, String contractName,
                         String contractSymbol, BigInteger contractDecimals,
                         BigInteger contractTotalSupply) throws Exception {
       return Web3JUtil.deploy(web3j, credentials, contractName, contractSymbol, contractDecimals, contractTotalSupply);
    }

    public String deploy(Credentials credentials, String contractName,
                         String contractSymbol, BigInteger contractDecimals,
                         BigInteger contractTotalSupply,BigInteger gasPrice,BigInteger gasLimit) throws Exception {
        return Web3JUtil.deploy(web3j, credentials, contractName, contractSymbol, contractDecimals, contractTotalSupply, gasPrice, gasLimit);
    }
    /**
     * 部署合约 根据bin code
     * @param credentials
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param value 转移金额
     * @param init
     * @return
     * @throws Exception
     */
    public  String deploy(Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String init) throws Exception {
        return Web3JUtil.deploy(web3j, credentials, nonce, gasPrice, gasLimit, value, init);
    }
    public static final String BINARY = "608060405234801561001057600080fd5b5060405162000c4538038062000c458339810180604052608081101561003557600080fd5b81019080805164010000000081111561004d57600080fd5b8201602081018481111561006057600080fd5b815164010000000081118282018710171561007a57600080fd5b5050929190602001805164010000000081111561009657600080fd5b820160208101848111156100a957600080fd5b81516401000000008111828201871017156100c357600080fd5b50506020820151604090920151855191945091925061014357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601560248201527f6e616d652063616e6e6f7420626520656d707479200000000000000000000000604482015290519081900360640190fd5b60008351116101b357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601760248201527f73796d626f6c2063616e6e6f7420626520656d70747920000000000000000000604482015290519081900360640190fd5b6000821161022257604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820181905260248201527f646563696d616c73206d7573742062652067726561746572207468616e203020604482015290519081900360640190fd5b6000811161027c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018062000c226023913960400191505060405180910390fd5b835161028f9060009060208701906102cd565b5082516102a39060019060208601906102cd565b506002829055600a9190910a02600381905533600090815260046020526040902055506103689050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061030e57805160ff191683800117855561033b565b8280016001018555821561033b579182015b8281111561033b578251825591602001919060010190610320565b5061034792915061034b565b5090565b61036591905b808211156103475760008155600101610351565b90565b6108aa80620003786000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c806370a082311161007157806370a08231146101eb57806379cc67901461021157806395d89b411461023d578063a9059cbb14610245578063cae9ca5114610273578063dd62ed3e1461032e576100b4565b806306fdde03146100b9578063095ea7b31461013657806318160ddd1461017657806323b872dd14610190578063313ce567146101c657806342966c68146101ce575b600080fd5b6100c161035c565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100fb5781810151838201526020016100e3565b50505050905090810190601f1680156101285780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101626004803603604081101561014c57600080fd5b506001600160a01b0381351690602001356103ea565b604080519115158252519081900360200190f35b61017e610417565b60408051918252519081900360200190f35b610162600480360360608110156101a657600080fd5b506001600160a01b0381358116916020810135909116906040013561041d565b61017e61048d565b610162600480360360208110156101e457600080fd5b5035610493565b61017e6004803603602081101561020157600080fd5b50356001600160a01b031661050b565b6101626004803603604081101561022757600080fd5b506001600160a01b03813516906020013561051d565b6100c16105ee565b6102716004803603604081101561025b57600080fd5b506001600160a01b038135169060200135610648565b005b6101626004803603606081101561028957600080fd5b6001600160a01b03823516916020810135918101906060810160408201356401000000008111156102b957600080fd5b8201836020820111156102cb57600080fd5b803590602001918460018302840111640100000000831117156102ed57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610657945050505050565b61017e6004803603604081101561034457600080fd5b506001600160a01b038135811691602001351661075f565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156103e25780601f106103b7576101008083540402835291602001916103e2565b820191906000526020600020905b8154815290600101906020018083116103c557829003601f168201915b505050505081565b3360009081526005602090815260408083206001600160a01b039590951683529390529190912055600190565b60035481565b6001600160a01b038316600090815260056020908152604080832033845290915281205482111561044d57600080fd5b6001600160a01b038416600090815260056020908152604080832033845290915290208054839003905561048284848461077c565b5060015b9392505050565b60025481565b336000908152600460205260408120548211156104af57600080fd5b3360008181526004602090815260409182902080548690039055600380548690039055815185815291517fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59281900390910190a2506001919050565b60046020526000908152604090205481565b6001600160a01b03821660009081526004602052604081205482111561054257600080fd5b6001600160a01b038316600090815260056020908152604080832033845290915290205482111561057257600080fd5b6001600160a01b0383166000818152600460209081526040808320805487900390556005825280832033845282529182902080548690039055600380548690039055815185815291517fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59281900390910190a250600192915050565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156103e25780601f106103b7576101008083540402835291602001916103e2565b61065333838361077c565b5050565b60008361066481856103ea565b1561075757604051600160e01b638f4ffcb102815233600482018181526024830187905230604484018190526080606485019081528751608486015287516001600160a01b03871695638f4ffcb195948b94938b939192909160a490910190602085019080838360005b838110156106e65781810151838201526020016106ce565b50505050905090810190601f1680156107135780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b15801561073557600080fd5b505af1158015610749573d6000803e3d6000fd5b505050506001915050610486565b509392505050565b600560209081526000928352604080842090915290825290205481565b6001600160a01b03821661078f57600080fd5b6001600160a01b0383166000908152600460205260409020548111156107b457600080fd5b6001600160a01b038216600090815260046020526040902054818101116107da57600080fd5b6001600160a01b038083166000818152600460209081526040808320805495891680855282852080548981039091559486905281548801909155815187815291519390950194927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a36001600160a01b0380841660009081526004602052604080822054928716825290205401811461087857fe5b5050505056fea165627a7a72305820b71cec8cc70b86e23ed739448060b454b40b7ed7f37b0856e323c6384757b3300029746f74616c537570706c79206d7573742062652067726561746572207468616e203020";

    /**
     * 部署合约
     * @param credentials
     * @param gasPrice
     * @param gasLimit
     * @param _name
     * @param _symbol
     * @param _decimals
     * @param _totalSupply
     * @return 合约部署hash
     * @throws Exception
     */
    public  String deploy(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name, String _symbol, BigInteger _decimals, BigInteger _totalSupply) throws Exception {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint256(_decimals),
                new Uint256(_totalSupply)));
        BigInteger transactionCount = Web3JUtil.getTransactionCount(web3j, credentials.getAddress());
        String contractContent = BINARY + encodedConstructor;
        String transactionHash = Web3JUtil.deploy(web3j, credentials, transactionCount, gasPrice, gasLimit, contractContent);
        return  transactionHash;
    }

    /**
     * 部署并等待3s
     * @param credentials
     * @param gasPrice
     * @param gasLimit
     * @param _name
     * @param _symbol
     * @param _decimals
     * @param _totalSupply
     * @return
     * @throws Exception
     */
    public  String deployByWait3(Credentials credentials, String _name,
                          String _symbol, BigInteger _decimals,
                          BigInteger _totalSupply,BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint256(_decimals),
                new Uint256(_totalSupply)));
        BigInteger transactionCount = Web3JUtil.getTransactionCount(web3j, credentials.getAddress());
        String contractContent = BINARY + encodedConstructor;
        String transactionHash = Web3JUtil.deploy(web3j, credentials, transactionCount, gasPrice, gasLimit, contractContent);
        return transactionHash;
    }

    /**
     * 为部署的合约创建标签实列
     * @param contractAddress
     * @return
     */
    public  TokenERC20 load(String contractAddress){
        return Web3JUtil.load(web3j, contractAddress);
    }

    /**
     * 合约事件监听
     * @param contractAddress
     */
    public  void eventContractListener(String contractAddress){
        Web3JUtil.eventContractListener(web3j, contractAddress);
    }

    /**
     * 根据hash获取交易信息
     * @param hash
     * GetTransactionByHash返回的gas是传过去的值 blocknumber有可能为空
     * @return
     */
    public  EthTransaction getTransactionByHash(String hash){
        return Web3JUtil.getTransactionByHash(web3j, hash);
    }
    /**
     * 根据hash获取交易
     * GetTransactionReceipt 返回的gas是真实交易消耗的 blocknumber不为空
     * @param hash
     * @return
     */
    public  TransactionReceipt getTransactionReceiptByHash(String hash){
        return Web3JUtil.getTransactionReceiptByHash(web3j, hash);
    }


}
