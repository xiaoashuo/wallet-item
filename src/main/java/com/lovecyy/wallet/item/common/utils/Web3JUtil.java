package com.lovecyy.wallet.item.common.utils;

import cn.hutool.core.io.FileUtil;
import com.lovecyy.wallet.item.common.constants.ETHConstants;
import com.lovecyy.wallet.item.common.exceptions.ContractException;
import com.lovecyy.wallet.item.contracts.TokenERC20;
import com.lovecyy.wallet.item.common.exceptions.TransferException;
import com.lovecyy.wallet.item.model.dto.GasInfoDTO;
import com.lovecyy.wallet.item.model.dto.KeystoreDto;
import io.reactivex.disposables.Disposable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.ChainId;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Web3JUtil {
    private static final Logger log = LoggerFactory.getLogger(Web3JUtil.class);

    /**
     * 根据地址获取账户余额
     *
     * @param web3j
     * @param address 地址
     * @return 以外为单位
     */
    public static BigDecimal getBalanceByAddress(Web3j web3j, String address) {
        try {
            EthGetBalance balanceWei = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(balanceWei.getBalance().toString(), Convert.Unit.ETHER);
        } catch (IOException e) {
            log.error("获取账户余额[{}]异常", address, e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取指定块的余额
     *
     * @param address
     * @param blockNumber
     * @return
     */
    public static BigDecimal getBalanceByAddress(Web3j web3j, String address, BigInteger blockNumber) throws IOException {
        EthGetBalance balanceWei = web3j.ethGetBalance(address, new DefaultBlockParameterNumber(blockNumber)).send();
        return FormatConvert.WeiTOEth(balanceWei.getBalance().toString());
    }

    /**
     * 得到账户最新的交易数
     * 帐户状态中还包括 nonce，这是一个序列号，表示帐户执行的交易数量。
     *
     * @param address
     * @return
     */
    public static BigInteger getTransactionCount(Web3j web3j, String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    /**
     * 获得当前所有区块数量
     * 即当前区块高度
     */
    public static BigInteger getBlockNumber(Web3j web3j) throws IOException {
        EthBlockNumber send = web3j.ethBlockNumber().send();
        return send.getBlockNumber();
    }

    /**
     * 获取块信息 通过指定块号
     *
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public static EthBlock.Block getBlockByNumber(Web3j web3j, BigInteger blockNumber) throws IOException {
        //获取block
        //ethGetBlockByNumber Boolean - 为true时返回完整的交易对象，否则仅返回交易哈希
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send().getBlock();
        return block;
    }

    /**
     * 打开钱包
     *
     * @param walletName      钱包文件名 UTF...
     * @param walletPassword  钱包密码
     * @param walletDirectory keystore目录
     * @return
     */
    public static Credentials openWallet(String walletName, String walletPassword, String walletDirectory) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(walletPassword, walletDirectory + File.separator + walletName);
        } catch (Exception e) {
            log.error("打开钱包失败", e);
        }
        return credentials;
    }

    /**
     * 打开钱包
     * @param walletPassword
     * @param content key store 内容
     * @return
     */
    public static Credentials openWalletByJSON(String walletPassword, String content) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadJsonCredentials(walletPassword, content );
        } catch (Exception e) {
            log.error("打开钱包失败", e);
        }
        return credentials;
    }

    /**
     * 打开钱包
     * @param walletPassword
     * @param content
     * @return
     */
    public static Credentials openWallet(String walletPassword, String content) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(walletPassword, content);
        } catch (Exception e) {
            log.error("打开钱包失败", e);
        }
        return credentials;
    }



    /**
     * 通过助记词装载钱包
     *
     * @param password
     * @param mnemonic
     * @return
     */
    public static Credentials openWalletByMnemonic(String password, String mnemonic) {
        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        return credentials;
    }

    /**
     * 装载钱包 通过派生路径
     *
     * @param password
     * @param mnemonic
     * @return
     */
    public static Credentials openWalletByDerivationPath(String password, String mnemonic) {
//Derivation path wanted: // m/44'/60'/0'/0
        int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0};

// Generate a BIP32 master keypair from the mnemonic phrase
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, password));

// Derived the key using the derivation path
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);

// Load the wallet for the derived key
        Credentials credentials = Credentials.create(derivedKeyPair);
        return credentials;
    }

    /**
     * 装载钱包 通过密钥
     *
     * @param privateKey
     * @return
     */
    public static Credentials openWalletByPrivateKey(String privateKey) {
        return Credentials.create(privateKey);
    }
    /**
     * 得到钱包私钥
     *
     * @param walletName
     * @param walletPassword
     * @param walletDirectory
     * @return
     */
    public static String getWalletPrivateKey(String walletName, String walletPassword, String walletDirectory) {
        Credentials credentials = openWallet(walletName, walletPassword, walletDirectory);
        Assert.notNull(credentials, "failed to open wallet ");
        return credentials.getEcKeyPair().getPrivateKey().toString(16);
    }

    /**
     * 得到钱包私钥
     * @param credentials
     * @return
     */
    public static String getWalletPrivateKey(Credentials credentials) {
        Assert.notNull(credentials, "credentials cannot be null ");
        return credentials.getEcKeyPair().getPrivateKey().toString(16);
    }
    /**
     * 创建钱包
     *
     * @param password
     * @param directory
     * @return
     * @throws Exception
     */
    public static KeystoreDto createWallet(String password, String directory) throws Exception {
        try {
            String walletName = WalletUtils.generateNewWalletFile(password, new File(directory));
            String path = directory + File.separator + walletName;
            String result = FileUtil.readUtf8String(path);
            return new KeystoreDto(walletName, result);
        } catch (Exception e) {
            log.error("创建钱包失败", e);
            throw e;
        }
    }

    /**
     * 创建钱包带助记词 BIP39
     *
     * @param password
     * @param directory
     * @return
     * @throws Exception
     */
    public static KeystoreDto createWalletWithMnemonic(String password, String directory) throws Exception {
        try {
            Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password, new File(directory));
            String path = directory + File.separator + bip39Wallet.getFilename();
            String result = FileUtil.readUtf8String(path);
            return new KeystoreDto(bip39Wallet.getFilename(), result, bip39Wallet.getMnemonic());
        } catch (Exception e) {
            log.error("创建钱包失败", e);
            throw e;
        }
    }


    /**
     * 获取账号列表
     */
    public static List<String> getAccountList(Web3j web3j) throws IOException {
        EthAccounts ethAccounts = web3j.ethAccounts().send();
        return ethAccounts.getAccounts();
    }

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    public static BigInteger getTransactionGasLimit(Web3j web3j, Transaction transaction) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasLimit;
    }

    /**
     * 获取当前gasPrice
     *
     * @return
     * @throws IOException
     */
    public static BigInteger getGasPrice(Web3j web3j) throws IOException {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        return ethGasPrice.getGasPrice();
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
    public static String sendTransaction(Web3j web3j, Credentials credentials, String toAddress, BigDecimal value) throws Exception {
        return sendTransaction(web3j, credentials, toAddress, value, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
    }

    /**
     * 发送交易 离线签名
     *
     * @param credentials
     * @param toAddress
     * @param value
     * @param gasPrice
     * @param gasLimit
     * @return
     * @throws IOException
     */
    public static String sendTransaction(Web3j web3j, Credentials credentials, String toAddress, BigDecimal value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        String address = credentials.getAddress();
        //从以太币转为wei最小单位
        BigInteger transactionValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        //nonce
        BigInteger nonce = getTransactionCount(web3j, address);
        //RawTransaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, transactionValue);
        // Sign the transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        // Convert it to Hexadecimal String to be sent to the node
        String hexValue = Numeric.toHexString(signedMessage);
        // Send transaction
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        // Get the transaction hash
        String transactionHash = ethSendTransaction.getTransactionHash();
        //后置处理
        afterTransactionErrorCode(ethSendTransaction);
        log.info("发送人[{}] 接收人[{}] 金额[{}] ETH 交易hash [{}] ", address, toAddress, transactionValue, transactionHash);
        return transactionHash;
    }

    /**
     * 自估算gasLimit
     * @param web3j
     * @param credentials
     * @param toAddress
     * @param value
     * @param gasPrice
     * @param gasLimit
     * @return
     * @throws Exception
     */
    public static String sendTransaction(Web3j web3j, Credentials credentials, String toAddress, BigDecimal value, BigInteger gasPrice) throws Exception {
        String address = credentials.getAddress();
        //从以太币转为wei最小单位
        BigInteger transactionValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        //nonce
        BigInteger nonce = getTransactionCount(web3j, address);
        //估算以太gas Limit
        Transaction transaction = Transaction.createEtherTransaction(address, nonce, gasPrice, null, toAddress, transactionValue);
        EthEstimateGas send = web3j.ethEstimateGas(transaction).send();
        Assert.notNull(send.getResult(),send.getError()!=null?send.getError().getMessage():null);
        BigInteger gasLimit = send.getAmountUsed();
        log.info("评估交易GasLimit",gasLimit);
        //RawTransaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, transactionValue);
        // Sign the transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        // Convert it to Hexadecimal String to be sent to the node
        String hexValue = Numeric.toHexString(signedMessage);
        // Send transaction
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        // Get the transaction hash
        String transactionHash = ethSendTransaction.getTransactionHash();
        //后置处理
        afterTransactionErrorCode(ethSendTransaction);
        log.info("发送人[{}] 接收人[{}] 金额[{}] ETH 交易hash [{}] ", address, toAddress, transactionValue, transactionHash);
        return transactionHash;
    }

    /**
     * 后置处理判断错误码
     *
     * @param ethSendTransaction
     * @throws InterruptedException
     */
    public static void afterTransactionErrorCode(EthSendTransaction ethSendTransaction) throws InterruptedException {
        if (ethSendTransaction.getError()!=null&&ethSendTransaction.getError().getCode() != 1) {
            System.out.println("error data:" + ethSendTransaction.getError().getData());
            System.out.println("error msg:" + ethSendTransaction.getError().getMessage());
            System.out.println("error code:" + ethSendTransaction.getError().getCode());
            throw new TransferException(ethSendTransaction.getError().getMessage());
        }
    }

    /**
     * 后置处理循环等待
     *
     * @param transactionHash
     * @throws InterruptedException
     * @throws IOException
     */
    public static void afterTransactionCircleWait(Web3j web3j, String transactionHash) throws InterruptedException, IOException {
        //正如前面解释的那样，当已签名的事务传播到网络中时，取决于许多因素(天然气价格、拥塞控制) ，
        // 需要一些时间才能看到事务被挖掘并添加到最后一个块中。
        //这就是为什么下面的代码包含一个简单的循环，通过调用方法
        // web3.ethGetTransactionReceipt (< txhash >)
        // ，每3秒验证一次事务是否被挖掘。发送()。
        // Wait for transaction to be mined
        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            System.out.println("checking if transaction " + transactionHash + " is mined....");
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(transactionHash).send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait 3 sec
        } while (!transactionReceipt.isPresent());

        System.out.println("Transaction " + transactionHash + " was mined in block # " + transactionReceipt.get().getBlockNumber());
        //System.out.println("Balance: " + Convert.fromWei(web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance().toString(), Convert.Unit.ETHER));
    }

    /**
     * 签名交易
     */
    public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                         BigInteger value, String data, byte chainId, String privateKey) throws IOException {
        byte[] signedMessage;
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 部署合约
     *
     * @param credentials
     * @param contractName
     * @param contractSymbol
     * @param contractDecimals
     * @param contractTotalSupply
     * @return
     */
    public static String deploy(Web3j web3j, Credentials credentials, String contractName,
                                String contractSymbol, BigInteger contractDecimals,
                                BigInteger contractTotalSupply) throws Exception {
        TokenERC20 contract = TokenERC20.deploy(web3j, credentials, new DefaultGasProvider(),
                contractName, contractSymbol, contractDecimals, contractTotalSupply).send();
        return contract.getContractAddress();
    }

    public static String deploy(Web3j web3j, Credentials credentials, String contractName,
                                String contractSymbol, BigInteger contractDecimals,
                                BigInteger contractTotalSupply, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenERC20 contract = TokenERC20.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit),
                contractName, contractSymbol, contractDecimals, contractTotalSupply).sendAsync().get();
        return contract.getContractAddress();
    }


    /**
     * 部署合约 根据bin code
     * 无构造
     * /
     * RawTransaction rawTransaction = RawTransaction.createContractTransaction(
     * <nonce>,
     * <gasPrice>,
     * <gasLimit>,
     * <value>,
     * "0x <compiled smart contract code>");
     * 若带构造方法 则
     * String encodedConstructor =
     * FunctionEncoder.encodeConstructor(Arrays.asList(new Type(value), ...));
     * Transaction transaction = Transaction.createContractTransaction(
     * <fromAddress>,
     * <nonce>,
     * <gasPrice>,
     * <gasLimit>,
     * <value>,
     * "0x <compiled smart contract code>" + encodedConstructor);
     *   String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
     *                 new Utf8String(_symbol),
     *                 new Uint256(_decimals),
     *                 new Uint256(_totalSupply)));
     * @param web3j
     * @param credentials
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param value 转移金额
     * @param init
     * @return 交易hash
     * @throws Exception
     */
    public static String deploy(Web3j web3j, Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String init) throws Exception {
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, value, init);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        if (ethSendTransaction.hasError()){
            throw new ContractException(ethSendTransaction.getError().getMessage());
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    /**
     * 部署合约
     * @param web3j
     * @param credentials
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param init
     * @return 部署hash
     * @throws Exception
     */
    public static String deploy(Web3j web3j, Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String init) throws Exception {
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, init);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction.hasError()){
            throw new ContractException(ethSendTransaction.getError().getMessage());
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    /**
     * 为部署的合约创建标签实列
     *
     * @param contractAddress
     * @return
     */
    public static TokenERC20 load(Web3j web3j, String contractAddress) {
        Credentials creds = Credentials.create(contractAddress);
        return TokenERC20.load(creds.getAddress(), web3j, creds, new DefaultGasProvider());
    }

    /**
     * 合约事件监听
     * 单独一线程 一直监听即可
     * @param contractAddress
     */
    public static void eventContractListener(Web3j web3j, String contractAddress) {
        final org.web3j.protocol.core.methods.request.EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
                contractAddress);
        Event TRANSFER_EVENT = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
                }, new TypeReference<Address>(true) {
                }, new TypeReference<Uint256>() {
                }));
        String topicData = EventEncoder.encode(TRANSFER_EVENT);
        ethFilter.addSingleTopic(topicData);
        Disposable subscribe = web3j.ethLogFlowable(ethFilter).subscribe(
                (logEvent) -> {
                    BigInteger blockNumber = logEvent.getBlockNumber();
                    // 提取转账记录
                    log.info("BlockNumber=", blockNumber);
                    //块hash
                    String blockHash = logEvent.getBlockHash();
                    //交易hash
                    String transactionHash = logEvent.getTransactionHash();
                    //合约地址
                    String txContractAddress = logEvent.getAddress();
                    List<String> topics = logEvent.getTopics();
                    String fromAddress = topics.get(1);
                    String toAddress = topics.get(2);
                    String value = logEvent.getData();
                    String timestamp = "";

                    try {
                        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(logEvent.getBlockNumber()), false).send();
                        timestamp = String.valueOf(ethBlock.getBlock().getTimestamp());
                    } catch (IOException e) {
                        log.warn("Block timestamp get failure,block number is {}", logEvent.getBlockNumber());
                        log.error("Block timestamp get failure,{}", e);
                    }
                    //from address  "0x" + fromAddress.substring(26)
                    //to address  "0x" + toAddress.substring(26)
                    //value   new BigDecimal(new BigInteger(value.substring(2), 16)).divide(BigDecimal.valueOf(1000000000000000000.0), 18, BigDecimal.ROUND_HALF_EVEN)
                }
        );
        //取消订阅
      //   subscribe.dispose();
    }

    /**
     * 根据hash获取交易信息
     *
     * @param hash GetTransactionByHash返回的gas是传过去的值 blocknumber有可能为空
     * @return
     */
    public static EthTransaction getTransactionByHash(Web3j web3j, String hash) {
        Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(hash);
        try {

            EthTransaction transaction = request.send();
            return transaction;
        } catch (IOException e) {
            log.error("获取交易记录失败...", e);
        }
        return null;

    }

    /**
     * 根据hash获取交易
     * GetTransactionReceipt 返回的gas是真实交易消耗的 blocknumber不为空
     *
     * @param hash
     * @return
     */
    public static TransactionReceipt getTransactionReceiptByHash(Web3j web3j, String hash) {
        Request<?, EthGetTransactionReceipt> request = web3j.ethGetTransactionReceipt(hash);
        try {
            Optional<TransactionReceipt> transactionReceipt = request.send().getTransactionReceipt();
            if (!transactionReceipt.isPresent()) {
                return null;
            }
            TransactionReceipt result = transactionReceipt.get();
            System.out.println(result.getStatus());
            return result;
        } catch (IOException e) {
            log.error("获取交易凭据失败...", e);
        }
        return null;

    }

    /**
     * 获取gas信息 主链上的 包括预估时间
     * @return
     */
    public static GasInfoDTO gasInfo() {
        try {
            Document  document = Jsoup.connect(ETHConstants.GAS_price).get();
            Elements gasTracker = document.select("div[class='row text-center mb-3']");
            String lowPrice = gasTracker.select("div[class='h4 text-success mb-1']").text();
            String lowTime = gasTracker.select("div[class='text-secondary']").first().text();
            String avgPrice = gasTracker.select("span[class='h4 text-primary mb-1']").text();
            String avgTime = gasTracker.select("div[class='text-secondary']").eq(1).text();
            String highPrice = gasTracker.select("span[class='h3 mb-0']").text();
            String highTime = gasTracker.select("div[class='text-secondary']").last().text();
            return new GasInfoDTO(lowPrice, lowTime, avgPrice, avgTime, highPrice, highTime);
        } catch (IOException e) {
            log.error("获取gas信息失败");
            return null;
        }

    }


}
