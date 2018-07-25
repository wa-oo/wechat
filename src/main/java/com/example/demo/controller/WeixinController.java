package com.example.demo.controller;

import com.example.demo.util.Decript;
import com.example.demo.util.MessageUtil;
import com.example.demo.util.WeixinUtil;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class WeixinController {

    private final String token = "wangtao";
    // 上传多媒体文件
    public static final String UPLOAD_IMAGE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
    public static final String UPLOAD_FODDER_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";


    /**
     * 信息校验
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/h")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("开始签名校验");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);

        //排序
        String sortString = sort(token, timestamp, nonce);
        //加密
        String mytoken = Decript.SHA1(sortString);
        //校验签名
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("签名校验通过。");
            response.getWriter().println(echostr); //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
        } else {
            System.out.println("签名校验失败。");
        }
    }

    /**
     * 接受公众号传来的信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/h", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置字符串格式
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Map<String,String> map = MessageUtil.xmlTiMap(request);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            String message = null;

            if (MessageUtil.MESSAGE_TEXT.equals(msgType)){

                //根据用户输入进行判断
                if ("1".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.firstMenu());
                } else if ("2".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.secondMenu());
                } else if ("?".equals(content) || "？".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                }

            } else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {

                //获取事件
                String eventType = map.get("Event");

                //关注后的逻辑处理
                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                } else if (MessageUtil.MESSAGE_CLICK.equals(eventType)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                } else if (MessageUtil.MESSAGE_VIEW.equals(eventType)) {
                    String url = map.get("EventKey");
                    System.out.println(url);
//                    message = MessageUtil.initText(toUserName,fromUserName,url);
                } else if (MessageUtil.MESSAGE_SCANCODE.equals(eventType)) {
                    String key = map.get("EventKey");
                    System.out.println(key);
//                    message = MessageUtil.initText(toUserName,fromUserName,key);
                }
            } else if (MessageUtil.MESSAGE_LOCATION.equals(msgType)) {
                String label = map.get("Label");
                message = MessageUtil.initText(toUserName,fromUserName,label);
            }
            out.print(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public void uploadFodderController() throws IOException {
        String acctessToken = WeixinUtil.getAccessToken().getToken();
        System.out.println(acctessToken);
    }

    //排序方法
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
    }

}
