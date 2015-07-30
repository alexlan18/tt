package com.wing.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件的读写工具类
 * @author zhongzh
 *
 */
public class FileUtils {
	
	/**
	 * 文件内容写到byte数组
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getContent(File file) throws IOException {
		long length = file.length();
		if(length>Integer.MAX_VALUE) {
			System.err.println("file is too big! file:"+file.getAbsolutePath());
		}
		byte[] buff = new byte[(int) length];
		FileInputStream inputStream = new FileInputStream(file);
		try {
			int off = 0;	//起始位置
			int readnum = 1024;	//每次读取字节数
			if(length < readnum) {
				readnum = (int) length;
			}
			while(inputStream.read(buff, off, readnum)>0) {
				off+=readnum;
				//剩余字节数
				int surplus = (int) (length-off);
				readnum = surplus < readnum ? surplus:readnum;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
		return buff;
	}
	
}
