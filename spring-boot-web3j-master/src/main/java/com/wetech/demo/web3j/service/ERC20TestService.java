package com.wetech.demo.web3j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

// 导入 Web3j 自动生成的 ERC20Test Java 包装类
// !!! 警告：请根据你项目中 `contracts` 目录下的实际生成路径修改此 import 语句 !!!
import com.wetech.demo.web3j.contracts.erc20test.ERC20Test;

@Service
public class ERC20TestService {

    @Autowired
    private Web3j web3j; // 从 Web3jConfig 自动注入

    @Autowired
    private Credentials credentials; // 从 Web3jConfig 自动注入 [cite: 54, 65]

    // 用于存储部署后的合约实例
    private ERC20Test contract;

    // 存储合约地址
    private String contractAddress;

    // 1. 部署合约
    public String deployContract() throws Exception {
        // 使用 Web3j 自动生成的 deploy 方法
        ERC20Test deployedContract = ERC20Test.deploy(
                web3j,
                credentials,
                new DefaultGasProvider() // 自动处理 Gas
        ).send();

        // 保存合约实例和地址
        this.contract = deployedContract;
        this.contractAddress = deployedContract.getContractAddress();

        return this.contractAddress;
    }

    // 2. 实现 mint 接口 [cite: 7]
    public String mint(BigInteger amount) throws Exception {
        checkContractDeployed();
        TransactionReceipt tx = contract.mint(amount).send();
        return tx.getTransactionHash();
    }

    // 3. 实现 transfer 接口 [cite: 7]
    public String transfer(String to, BigInteger value) throws Exception {
        checkContractDeployed();
        TransactionReceipt tx = contract.transfer(to, value).send();
        return tx.getTransactionHash();
    }

    // 4. 实现 balanceOf 接口 [cite: 7]
    public BigInteger balanceOf(String account) throws Exception {
        checkContractDeployed();
        // Read-only (view) functions use .send() and return the value directly
        BigInteger balance = contract.balanceOf(account).send();
        return balance;
    }

    // 5. 实现 approve 接口 [cite: 7]
    public String approve(String spender, BigInteger value) throws Exception {
        checkContractDeployed();
        TransactionReceipt tx = contract.approve(spender, value).send();
        return tx.getTransactionHash();
    }

    // 6. 实现 transferFrom 接口 [cite: 7]
    public String transferFrom(String from, String to, BigInteger value) throws Exception {
        checkContractDeployed();
        TransactionReceipt tx = contract.transferFrom(from, to, value).send();
        return tx.getTransactionHash();
    }

    // 辅助函数：确保合约已部署
    private void checkContractDeployed() {
        if (contract == null) {
            throw new RuntimeException("Contract not deployed. Please call /api/erc20/deploy first.");
        }
    }
}