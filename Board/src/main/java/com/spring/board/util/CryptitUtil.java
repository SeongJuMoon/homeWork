package com.spring.board.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;

public final class CryptitUtil {
	
	private final static String CRYPTO_ALGORITHM = "SHA-256";
	
	public static String encryptSha(final String targetString) {

		String retString = null ;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(CRYPTO_ALGORITHM);
			messageDigest.reset();
			messageDigest.update(targetString.getBytes(StandardCharsets.UTF_8));

			retString = String.format("%040x", new BigInteger(1, messageDigest.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		return 	retString;
	}
	
}
