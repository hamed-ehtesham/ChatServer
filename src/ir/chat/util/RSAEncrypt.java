package ir.chat.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.channels.SelectionKey;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;

/**
 * Created by Mohammad Amin on 11/09/2015.
 */
public class RSAEncrypt {
    static private String algorithm = "RSA";
    static private int keysize = 512;
    static private String xform = "RSA/ECB/PKCS1Padding";
    static private String publicKey = null;
    static private PrivateKey privateKey = null;

    public RSAEncrypt() throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        KeyPair keyPair = RSAEncrypt.keyPairGen();
        byte[] t1 = keyPair.getPublic().getEncoded();
        publicKey = encoder.encode(t1);
        privateKey = keyPair.getPrivate();
    }

    private static KeyPair keyPairGen() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(keysize);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }


    public static SealedObject enByPUK(String input , String key) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] sigBytes2 = decoder.decodeBuffer(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
        KeyFactory keyFact = KeyFactory.getInstance("RSA", "BC");
        PublicKey pubKey = keyFact.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return (new SealedObject(input, cipher));
    }


    public static String deByPRK(SealedObject input, PrivateKey key) throws Exception {

        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return (String) input.getObject(cipher);
    }

    public static String getPublicKey(){
        return publicKey;
    }
    public static PrivateKey getPrivateKey(){
        return privateKey;
    }

    public static void setAlgorithm(String algorithm) {
        RSAEncrypt.algorithm = algorithm;
    }

    public static void setKeysize(int keysize) {
        RSAEncrypt.keysize = keysize;
    }


}