package com.dingo.decrypt;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

public class DecryptString {
	final Logger log = Logger.getLogger(DecryptString.class.getName());
	private Cipher cipher = null;
	private String orgKey = "";

	public DecryptString(String orgKey) {
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
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			log.info("init finish");
		} catch (Exception e) {
			log.error("error while init", e);
		}
	}

	public String decrypt(String content) {
		try {
			log.info("decrypt start");
			byte[] byteContent = hex2byte(content.getBytes());
			byte[] result = cipher.doFinal(byteContent);
			String returnResult = new String(result, "UTF-8");
			log.info("decrypt finis");
			return returnResult;
			
		} catch (Exception e) {
			return content;
		}
	}

	/**
	 * 把用户输入的用sha256变成标准的256长度 这里变长不会改变完全性，安全性还是要用原始密码长度来保证
	 * 
	 * @param orgKey
	 *            原始密码，可以是随意的字符串，汉字都可以
	 * @return 格式化后的密码
	 */
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

	private byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			log.error("lenth of hex is not right");
			return null;
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
}
