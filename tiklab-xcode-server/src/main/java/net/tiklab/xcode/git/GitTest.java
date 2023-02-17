package net.tiklab.xcode.git;


import net.tiklab.core.exception.ApplicationException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

public class GitTest {


    public static void main(String[] args) throws Exception {

        pre(keyString);

    }

    static String keyString =
            "AAAAB3NzaC1yc2EAAAADAQABAAABgQCkU2T0B4dXhs1+dLuOpVdyUniMA4mBzKawKunKSRAHGklAmlcOnH81q+MorSps4opWn9lTe0Gh3ZFhVspLNjbHi+7NQeBXR+B84FJ0bulCEDkJSvxFNRUJPtO8PBoQefZZCVjq6XOAnO+g9i2RcoTptzTwoD55/CLxRtfgruywu66ZLR7U0EMvsdDW2MN5UsBH2auhPVcGQ1zXZAlLNgBGRYXwd7V1iQ+SAOj58yclh2LR5X1zwt2ft1dTlabVAbAezCLh0ClqAOv0G/6hkVjt+IrgT91lRVb+gVzzfeLiKp+IjWg9JxbVd4vm4r1F20LZrCO9f+PaF+iOdW8TlM+WGcUThsZ5++qPyvMvH+lQ5lwEltm/D3jLUL5tiVu9xSU6O3U5teBEUtSH/WJKsu/jvAHoAdi9BG1DcjSs/gT+hIMv9SEgFiVIXsiNiKIfvhCHNoVjeT749VKjzazHAhw5usTxInE+Ag2CbQTtzs/dqDOhm2ljHImRS3vyPUTAp/E=";



    private static void  pre(String key){

        byte[] decoded = Base64.getDecoder().decode(key);
        try {
            // 将字节数组包装到缓冲区中
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            //自动更新的SIZEOF_INT的值
            AtomicInteger position = new AtomicInteger();

            String algorithm = readString(byteBuffer, position);

            if (!"ssh-rsa".equals(algorithm)){
                throw new ApplicationException("私钥格式错误，不是ssh-rsa格式");
            }

            // 字符串转换成字节
            BigInteger publicExponent = readMpint(byteBuffer, position);
            BigInteger modulus = readMpint(byteBuffer, position);

            //字节转换成PublicKey公钥
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(keySpec);

            byte[] pubBytes = publicKey.getEncoded();
            SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(pubBytes);
            ASN1Primitive primitive = spkInfo.parsePublicKey();
            String publicKeyString = Base64.getEncoder().encodeToString(primitive.getEncoded());
            System.out.println("解密后数据：\n" + publicKeyString);

            writePEM("pubPkcs1.pem", "RSA PUBLIC KEY", primitive.getEncoded() );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int SIZEOF_INT = 4;

    //字节转换成大数
    private static BigInteger readMpint(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return BigInteger.ZERO;
        }
        return new BigInteger(bytes);
    }

    //字节转换成字符
    private static String readString(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return "";
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    //转换成字节
    private static byte[] readBytes(ByteBuffer buffer, AtomicInteger pos){
        int len = buffer.getInt(pos.get());
        byte[] buff = new byte[len];
        for(int i = 0; i < len; i++) {
            buff[i] = buffer.get(i + pos.get() + SIZEOF_INT);
        }
        pos.set(pos.get() + SIZEOF_INT + len);
        return buff;
    }

    /**
     * 把pem私钥写入到文件
     * @param fileName 文件名称
     * @param header 指定公钥头部
     * @param content 私钥内容字节码
     * @throws IOException 写入失败
     */
    private static void writePEM(String fileName, String header, byte[] content) throws IOException{
        File f = new File(fileName);
        FileWriter fw = new FileWriter(f);
        PemObject pemObject = new PemObject(header, content);
        PemWriter pemWriter = new PemWriter(fw);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
    }























}








































