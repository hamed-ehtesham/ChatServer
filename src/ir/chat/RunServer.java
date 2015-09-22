package ir.chat;

import ir.chat.util.RSAEncrypt;

import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Created by Mohammad Amin on 20/09/2015.
 */
public class RunServer {
    public static void main(String[] args) throws Exception{
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        Server loginHandler = new Server();
        Thread t = new Thread(loginHandler);
        t.start();



    }
}
