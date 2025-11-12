package com.wetech.demo.web3j.controller;

import com.wetech.demo.web3j.service.ERC20TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/erc20") // 仿照 SimpleStorageController
public class ERC20TestController {

    @Autowired
    private ERC20TestService erc20TestService;

    // 部署合约的接口
    // 访问: POST http://localhost:8080/api/erc20/deploy
    @PostMapping("/deploy")
    public String deployContract() {
        try {
            String address = erc20TestService.deployContract();
            return "Contract deployed successfully at address: " + address;
        } catch (Exception e) {
            return "Error deploying contract: " + e.getMessage();
        }
    }

    // --- 以下是作业要求的5个接口 ---

    // 1. mint
    // 访问: POST http://localhost:8080/api/erc20/mint?value=100000000000000000000
    @PostMapping("/mint")
    public String mint(@RequestParam BigInteger value) {
        try {
            String txHash = erc20TestService.mint(value);
            return "Mint successful, transaction hash: " + txHash;
        } catch (Exception e) {
            return "Error in mint: " + e.getMessage();
        }
    }

    // 2. transfer
    // 访问: POST http://localhost:8080/api/erc20/transfer?to=0x...&value=100
    @PostMapping("/transfer")
    public String transfer(@RequestParam String to, @RequestParam BigInteger value) {
        try {
            String txHash = erc20TestService.transfer(to, value);
            return "Transfer successful, transaction hash: " + txHash;
        } catch (Exception e) {
            return "Error in transfer: " + e.getMessage();
        }
    }

    // 3. balanceOf
    // 访问: GET http://localhost:8080/api/erc20/balanceOf?account=0x...
    @GetMapping("/balanceOf")
    public String balanceOf(@RequestParam String account) {
        try {
            BigInteger balance = erc20TestService.balanceOf(account);
            return "Balance of " + account + " is: " + balance.toString();
        } catch (Exception e) {
            return "Error in balanceOf: " + e.getMessage();
        }
    }

    // 4. approve
    // 访问: POST http://localhost:8080/api/erc20/approve?spender=0x...&value=100
    @PostMapping("/approve")
    public String approve(@RequestParam String spender, @RequestParam BigInteger value) {
        try {
            String txHash = erc20TestService.approve(spender, value);
            return "Approve successful, transaction hash: " + txHash;
        } catch (Exception e) {
            return "Error in approve: " + e.getMessage();
        }
    }

    // 5. transferFrom
    // 访问: POST http://localhost:8080/api/erc20/transferFrom?from=0x...&to=0x...&value=100
    @PostMapping("/transferFrom")
    public String transferFrom(@RequestParam String from, @RequestParam String to, @RequestParam BigInteger value) {
        try {
            String txHash = erc20TestService.transferFrom(from, to, value);
            return "TransferFrom successful, transaction hash: " + txHash;
        } catch (Exception e) {
            return "Error in transferFrom: " + e.getMessage();
        }
    }
}