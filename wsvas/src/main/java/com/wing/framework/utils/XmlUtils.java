package com.wing.framework.utils;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * xml解析工具类
 *
 * @author: panwb
 *
 * Date: 13-4-11
 * Time: 下午2:43
 */
public class XmlUtils {

    /**
     *
     * 解析XML字符串，到Document
     *
     * @param xml
     * @return
     */
    public static Document parse(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            Document document = db.parse(stream);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 解析Document对象到Xml字符串
     *
     * @param document
     * @return
     */
    public static String parse(Document document) {
        try {
            TransformerFactory dbf = TransformerFactory.newInstance();
            Transformer t = dbf.newTransformer();
            t.setOutputProperty("encoding", "utf-8");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            t.transform(new DOMSource(document), new StreamResult(bos));
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
