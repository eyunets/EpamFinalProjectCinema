package com.epam.training.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.log4j.Log4j;
@Log4j
public class PasswordEncoder {
	public static String encode(String pwd) {
		MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try{
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pwd.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        }

        BigInteger bigInteger = new BigInteger(1, digest);
        StringBuilder encryptedPassword = new StringBuilder(bigInteger.toString(16));

        while (encryptedPassword.length() < 16){
            encryptedPassword.insert(0, "0");
        }

        return encryptedPassword.toString();
	}

}
