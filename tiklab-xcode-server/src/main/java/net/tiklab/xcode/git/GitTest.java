package net.tiklab.xcode.git;


import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class GitTest {


    public static void main(String[] args) throws Exception {
        // key();

        // PublicKey publicKey = getPublicKeyFromString(keyString);
        // String algorithm = publicKey.getAlgorithm();
        // String format = publicKey.getFormat();
        //
        // System.out.println(algorithm);
        // System.out.println(format);

        String encode = Base64.getEncoder().encodeToString(keyString.getBytes());
        System.out.println(encode);



    }


    public static void key() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 生成密钥对
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();

        // 获取Signature实例并初始化
        Signature sig = Signature.getInstance("DSA");
        sig.initSign(keyPair.getPrivate());

        // 更新要签名的数据
        byte[] data = "Hello, world!".getBytes();
        sig.update(data);

        // 生成签名
        byte[] signature = sig.sign();

        // 验证签名
        sig.initVerify(keyPair.getPublic());
        sig.update(data);
        boolean verified = sig.verify(signature);
        System.out.println("Signature verified: " + verified);
    }

    public static PublicKey getPublicKeyFromString(String keyString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String keyBytes;
        keyBytes = Base64.getEncoder().encodeToString(keyString.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    static String keyString = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDpc86W7JvuWqVAXJuX36DnXZlWhY6s" +
            "mjL9flhyjXMrCwN39xVOl2MmXSrxi3F7XaCoHa948cxYX7LTf5rYTAnxsPwoGApHOUyNsokbRBNP7IEN" +
            "ltzya8XPbznMOHYA09l0o5povMIhb9sWKJeYD72/s8qC6Th7UBv6D5vTj9" +
            "B3uZYolrlIe5lsyXO7OH7/osx8ZHux/rsuLR91TQ00cJm1N1CTFQ0eVT" +
            "mrVPqOCClyR3Os8xT3ufd+PztoDJMt1YnYammq5b/MOzJWBJVJ+4E" +
            "ZumvuE7fHVOrdE6aBCqTZPA9B2TzxdEC0a5q5IfEJlOVKU/xlBD3N" +
            "l3YWKkk4PP6hbOSlUSX7RjsoPSVB1Ou3l4tiP9qmwrjafwuNCjhe" +
            "eo5+NFIJ+A+NmY+QAF8QoEfKXDxVfSSk6w6mRdvLaEPoVmsY+7JNo" +
            "ePUd5hSL4YiufPhzOeMRePb/X+nfy8TtkR+aKpW3llsin/zr/NsCa2W" +
            "pGy2w5DCCA4SHV7KtRTkEoc= admin@DESKTOP-UNOU1SR\n";




























}








































