package com.dingo.encrypt;

public class App {
	public static void main(String[] args) {
		initLog4j();
		EncryptExcel encryptExcel = new EncryptExcel("C:\\test.xlsx", "这就是密码");
		encryptExcel.doEncrypt();
	}

	private static void initLog4j() {
		String rootPath = System.getProperty("user.dir");
		System.setProperty("log_home", rootPath);
	}
}
