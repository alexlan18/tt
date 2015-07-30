package com.wing.framework.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

/**
 * 检测编码方式
 * @author zhongzh
 */
public class CharsetDetector
{

    private boolean found = false;
    private String result;
    private int lang;

    public String[] detectChineseCharset(InputStream in) throws IOException
    {
    	lang = nsPSMDetector.CHINESE;
    	String[] prob = null;
    	BufferedInputStream imp = null;
    	try {
			
            nsDetector det = new nsDetector(lang);

            det.Init(new nsICharsetDetectionObserver()
            {

                public void Notify(String charset)
                {
                    found = true;
                    result = charset;
                }
            });
            imp = new BufferedInputStream(in);
            byte[] buf = new byte[1024];
            int len;
            boolean isAscii = true;
            while ((len = imp.read(buf, 0, buf.length)) != -1)
            {
                if (isAscii)
                    isAscii = det.isAscii(buf, len);
                if (!isAscii)
                {
                    if (det.DoIt(buf, len, false))
                        break;
                }
            }
            imp.close();
            in.close();
            det.DataEnd();
            if (isAscii)
            {
                found = true;
                prob = new String[]{"ASCII"};
            } else if (found)
            {
                prob = new String[]{result};
            } else
            {
                prob = det.getProbableCharsets();
            }
    		
		} catch (Exception e) {
			
		} finally {
			try {
				
				if(imp != null) {
					imp.close();
				}
				if(in != null) {
					in.close();
				}
				
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
    	
        
        return prob;
    }

    public String[] detectAllCharset(InputStream in) throws IOException
    {
        try
        {
            lang = nsPSMDetector.ALL;
            return detectChineseCharset(in);
        } catch (IOException e)
        {
            throw e;
        }
    }
    
    public static void main(String[] args) throws IOException {
    	CharsetDetector charDect = new CharsetDetector();
    	String filename = "D:\\Work Source\\test\\ips.txt";
		File file = new File(filename);
		InputStream inputStream = new FileInputStream(file);
		String[] probableSet = charDect.detectAllCharset(inputStream);
        for (String charset : probableSet)
        {
            System.out.println(charset);
        }
	}
    
}
