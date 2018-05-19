package com.dingo.encrypt;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

public class EncryptString {

	final Logger log = Logger.getLogger(EncryptString.class.getName());
	private Cipher cipher = null;
	private String orgKey = "";

	public EncryptString(String orgKey) {
		this.orgKey = orgKey;
		this.init();
	}

	private void init() {
		try {
			log.info("init start");
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(getFormatedKey(orgKey).getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			log.info("init finish");
		} catch (Exception e) {
			log.error("error while init", e);
		}
	}

	public String encrypt(String content) throws Exception {
		log.info("encrypt start");
		byte[] result = cipher.doFinal(content.getBytes("UTF-8"));
		String returnResult = byte2hex(result);
		log.info("encrypt finis");
		return returnResult;
	}

	private String getFormatedKey(final String orgKey) throws Exception {
		MessageDigest messageDigest;
		String encodeStr = "";
		log.info("get formate key start");
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(orgKey.getBytes("UTF-8"));
		encodeStr = byte2hex(messageDigest.digest());
		log.info("get formate key finish");
		return encodeStr;
	}

	private String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
}
