package com.wing.framework.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel数据导入导出
 * @author zhongzh
 *
 */
public class CommonExcelUtils {
	
	/**
	 * 对外接口，以web response的方式将数据导出到指定的excel文件中
	 * @param request
	 * @param response
	 * @param fileName 指定的excel文件名，后缀为xls（2003），xlsx（2007）
	 * @param sheetName 指定的sheet表名
	 * @param titles	sheet表的title
	 * @param dataList	要导出的数据列表
	 * @throws IOException
	 */
	public static void exportToWebResponse(HttpServletRequest request,HttpServletResponse response,String fileName,String sheetName,String[] titles,List<Object[]> dataList) throws IOException {
		if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0){ //IE下的编码
			fileName = URLEncoder.encode(fileName, "UTF-8");
		}else {
			fileName = new String(fileName.getBytes("UTF-8"),"iso8859-1");
		}
		//服务器告诉浏览器发送的数据类型
		response.setContentType("application/vnd.ms-excel");
		/*
		 * 当Content-Type 的类型为要下载的类型时 ,这个信息头会告诉浏览器该文件的名字和类型
		 * fileName 要包括后缀
		 */
		response.setHeader("content-disposition", "attachment;filename=" + fileName);
		//数据输出流，web response的方式
		OutputStream out = response.getOutputStream();
		try {
			exportToExcel(out,fileName,sheetName,titles,dataList);
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
			}
		}
		
	}
	
	/**
	 * 将数据导入excel文件然后写到输出流
	 * @param outputStream
	 * @param sheetName
	 * @param titles
	 * @param dataList
	 * @throws IOException 
	 */
	public static void exportToExcel(OutputStream outputStream,String fileName,String sheetName,String[] titles,List<Object[]> dataList) throws IOException {
		//工作簿,默认为支持excel2007的格式
		Workbook workbook = new XSSFWorkbook();
		if(fileName.endsWith(".xls")) {	//excel 2003
			workbook = new HSSFWorkbook();
		}
		//工作表sheet
		Sheet sheet = workbook.createSheet(sheetName);
		//第一行，作为titles
		Row titleRow = sheet.createRow(0);
		for (int i=0;i<titles.length;i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
		}
		//插入数据,sheet表的第一行为title，故从第二行开始，
		for(int j=0;j<dataList.size();j++) {
			Row row = sheet.createRow(j+1);
			Object[] objects = dataList.get(j);
			for(int m=0;m<objects.length;m++) {
				Cell cell = row.createCell(m);
				cell.setCellValue(String.valueOf(objects[m]));
			}
		}
		//将数据写入输出流
		workbook.write(outputStream);
		outputStream.flush();
	}
	
	/**
	 * 解析excel文件，
	 * @param outputStream
	 * @param sheetName
	 * @param titles
	 * @param dataList
	 * @throws IOException 
	 */
	public static void parseExcel(MultipartFile uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		
		//工作簿,默认为支持excel2007的格式
		Workbook workbook = new XSSFWorkbook(uploadFile.getInputStream());
		if(fileName.endsWith(".xls")) {	//excel 2003
			workbook = new HSSFWorkbook(uploadFile.getInputStream());
		}
		int sheetsNum = workbook.getNumberOfSheets();	//sheet数
		for(int i=0;i<sheetsNum;i++) {
			Sheet sheet = workbook.getSheetAt(i);
			for(int m=1;m<sheet.getLastRowNum();m++){
				Row row = sheet.getRow(m);
				String rowStr = "";
				for(int n=0;n<row.getLastCellNum();n++) {
					String cellValue = row.getCell(n).getStringCellValue();
					rowStr += cellValue;
				}
				System.out.println(rowStr);
			}
		}
		
	}
	
}
