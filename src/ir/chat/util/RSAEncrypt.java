package ir.chat.util;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;

/**
 * Created by Mohammad Amin on 11/09/2015.
 */
public class RSAEncrypt {
    static private String algorithm = "RSA";
    static private int keysize = 1024;
    static private String xform = "RSA/ECB/PKCS1Padding";

    public static KeyPair keyPairGen() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(keysize);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }


    public static SealedObject enByPUK(String input, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return (new SealedObject( input, cipher));
    }


    public static String deByPRK(SealedObject input, PrivateKey key) throws Exception {

        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return (String)input.getObject(cipher);
    }

    public static void setAlgorithm(String algorithm){
        RSAEncrypt.algorithm = algorithm;
    }

    public static void setKeysize(int keysize){
        RSAEncrypt.keysize = keysize;
    }


}