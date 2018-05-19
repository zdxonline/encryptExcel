package com.dingo.decrypt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DecryptExcel {
	final Logger log = Logger.getLogger(DecryptExcel.class.getName());

	private String filePath;
	private String key;
	private Workbook workbook;

	public DecryptExcel(String filePath, String key) {
		this.filePath = filePath;
		this.key = key;
	}

	public void doDecrypt() {
		try {
			workbook = openFile();
			DecryptString decryptStringEr = new DecryptString(key);
			for (int indexOfSheet = 0; indexOfSheet < workbook.getNumberOfSheets(); indexOfSheet++) {
				Sheet sheet = workbook.getSheetAt(indexOfSheet);
				for (int indexOfRow = 1; indexOfRow < sheet.getLastRowNum()+1; indexOfRow++) {// 表头不加密 index从1开始
					Row row = sheet.getRow(indexOfRow);
					for (int indexOfcell = 0; indexOfcell < row.getLastCellNum(); indexOfcell++) {
						Cell cell = row.getCell(indexOfcell);
						String orgValue = cell.toString();
						log.info(orgValue);
						String decryptEdValue = decryptStringEr.decrypt(orgValue);
						cell.setCellValue(decryptEdValue);
					}
				}
			}
			writeFile();
		} catch (Exception e) {
			log.error("error while do decrypt", e);
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					log.error("error while close workbook", e);
				}
			}
		}
	}

	private Workbook openFile() throws Exception {
		if (filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(filePath);
				boolean is03Excell = filePath.matches("^.+\\.(?i)(xls)$") ? true : false;
				workbook = is03Excell ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis);
			} finally {
				if (null != fis) {
					try {
						fis.close();
					} catch (IOException e) {
						log.error("error while close fis", e);
					}
				}
			}
		}
		return workbook;
	}

	private void writeFile() throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);// 写数据到这个路径上
			workbook.write(fos);
			fos.flush();
			fos.close();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
	}
}
