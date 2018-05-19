package com.dingo.decrypt;

public class App {
	public static void main(String[] args) {
		initLog4j();
		DecryptExcel decryptExcel = new DecryptExcel("C:\\test.xlsx", "这就是密码");
		decryptExcel.doDecrypt();
	}

	private static void initLog4j() {
		String rootPath = System.getProperty("user.dir");
		System.setProperty("log_home", rootPath);
	}
}
