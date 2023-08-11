package com.jxm.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取RSA公钥接口
 * 非对称加密（RSA）:  对于加密操作，公钥负责加密，私钥负责解密。对于签名操作，私钥负责签名，公钥负责验签。
 *
 * 例如: 两个端A和B进行通信，A向B发送了一条经过签名和加密的信息；涉及了四个密钥：A公钥、A私钥、B公钥、B私钥。
 * 签名：是为了让B确认这个信息就是A发出的，不是别人
 * 加密：对传输的内容进行保护，即使信息被恶意截取，也无法进行解析。只有B可以查看。
 *
 * 1. A向B发送信息进行签名和加密的具体流程。
 *    A使用自己的私钥对信息进行签名
 *    A使用B的公钥对信息进行加密
 *
 * 2. B接收到A发送的信息进行如下处理：
 *    B用自己的私钥对信息进行解密
 *    B使用A的公钥对进行验签操作
 */
@RestController
@Api(tags = "KeyPairController", description = "获取RSA公钥接口")
@RequestMapping("/rsa")
public class KeyPairController {

    @Autowired
    private KeyPair keyPair;

    @GetMapping("/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
