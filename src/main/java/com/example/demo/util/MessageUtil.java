package com.example.demo.util;

import com.example.demo.domain.TextMassage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
//    public static final String MESSAGE_NEWS = "news";
//    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
//    public static final String MESSAGE_SCANCODE = "scancode_push";

    /**
     * xml转为map集合
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String,String> xmlTiMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String,String> map = new HashMap<String,String>();
        SAXReader reader = new SAXReader();

        //从request中获取输入流
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);

        Element root = doc.getRootElement();

        List<Element> list = root.elements();

        for (Element e : list){
            map.put(e.getName(),e.getText());
        }
        ins.close();
        return map;
    }

    /**
     * 将文本消息对象转换为xml
     * @param textMassage
     * @return
     */
    public static String textMessageToXml(TextMassage textMassage){
        XStream xStream = new XStream();
        //将消息头设置为xml
        xStream.alias("xml",textMassage.getClass());
        return xStream.toXML(textMassage);
    }
}
