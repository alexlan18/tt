package com.wing.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * IO处理工具类
 * @author zhongzh
 *
 */
public class IOUtil {
	
	/**
	 * 从HttpRequest中读取输入数据
	 * @param req
	 * @param charset
	 * @return HttpRequest输入数据的String形式
	 * @throws IOException
	 */
	public static String getPostDataFromRequest(HttpServletRequest req,String charset) throws IOException{
		
		ServletInputStream inputStream=req.getInputStream();
		InputStreamReader inputReader=new InputStreamReader(inputStream,charset);
		BufferedReader br=new BufferedReader(inputReader);
		StringBuffer inputBuffer=new StringBuffer();
		char[] buf=new char[1024];
		
		int readLen=br.read(buf);
		while(readLen!=-1){
			inputBuffer.append(buf,0,readLen);
			readLen=br.read(buf);
		}
		br.close();
		String str=inputBuffer.toString();
		return str;
	}
	
	/**
	 * 从给定的输入流中读取输入数据
	 * @param req
	 * @param charset
	 * @return 输入流中数据的String形式
	 * @throws IOException
	 */
	public static String getPostDataFromInputStream(InputStream inputStream,String charset) throws IOException{
		
		InputStreamReader inputReader=new InputStreamReader(inputStream,charset);
		BufferedReader br=new BufferedReader(inputReader);
		StringBuffer inputBuffer=new StringBuffer();
		char[] buf=new char[1024];
		
		int readLen=br.read(buf);
		while(readLen!=-1){
			inputBuffer.append(buf,0,readLen);
			readLen=br.read(buf);
		}
		br.close();
		String str=inputBuffer.toString();
		return str;
	}
}
