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
import java.util.Date;
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


    /**
     * 拼接字符
     * @return
     */
    public static String initText(String toUserName,String fromUserName,String content){
        TextMassage text = new TextMassage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return textMessageToXml(text);
    }

    /**
     * 主菜单
     * @return
     */
    public static String menuText(){
    StringBuffer sb = new StringBuffer();
    sb.append("欢迎你的关注,请按照菜单提示进行操作:\n\n");
    sb.append("1.Allen介绍\n");
    sb.append("2.公众号介绍\n\n");
    sb.append("回复?调出此菜单");
    return sb.toString();
    }

    public static String firstMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("Allen是王涛的English name,王涛呢,是个很有趣的人");
        return sb.toString();
    }

    public static String secondMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("这个公众号主要是用来逗比的");
        return sb.toString();
    }

}
