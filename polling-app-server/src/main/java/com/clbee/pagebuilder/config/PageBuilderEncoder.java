package com.clbee.pagebuilder.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PageBuilderEncoder implements PasswordEncoder {

    private final Log logger = LogFactory.getLog(getClass());

    public PageBuilderEncoder() { }

    public String encode(CharSequence rawPassword) {
        String SHA = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(Byte.parseByte(rawPassword.toString()));

            byte byteData[] = messageDigest.digest();
            StringBuffer buffer = new StringBuffer();

            for(int i = 0; i < byteData.length; i++){
                buffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = buffer.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            SHA = null;
        }
        
        logger.info("Encoded password");

        return SHA;
        
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            logger.warn("Empty encoded password");
            return false;
        }

        return equalsNoEarlyReturn(rawPassword.toString(), encodedPassword);
    }

    static boolean equalsNoEarlyReturn(String a, String b) {
        char[] caa = a.toCharArray();
        char[] cab = b.toCharArray();

        if (caa.length != cab.length) {
            return false;
        }

        byte ret = 0;
        for (int i = 0; i < caa.length; i++) {
            ret |= caa[i] ^ cab[i];
        }
        return ret == 0;
    }
}

