package com.example.demo.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;

//推送消息
public class WeiXinApi {
    /**
     * 获取token接口
     */
    private String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    /**
     * 拉微信用户信息接口
     */
    private String getUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}";
    /**
     * 主动推送信息接口
     */
    private String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/send?access_token={0}";
    private HttpClient webClient;

    private Log log = LogFactory.getLog(getClass());
    public void initWebClient(String proxyHost, int proxyPort){
        this.initWebClient();
        if(webClient != null && !StringUtils.isEmpty(proxyHost)){
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            webClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
    }

    /**
     * @desc 初始化创建 WebClient
     */
    public void initWebClient() {
        log.info("initWebClient start....");
        try {
            PoolingClientConnectionManager tcm = new PoolingClientConnectionManager();
            tcm.setMaxTotal(10);
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new X509TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, ssf);
            tcm.getSchemeRegistry().register(sch);
            webClient = new DefaultHttpClient(tcm);
        } catch (Exception ex) {
            log.error("initWebClient exception", ex);
        } finally {
            log.info("initWebClient end....");
        }
    }

    /**
     * @desc 推送信息
     * @param token
     * @param msg
     * @return
     */
    public String sendMessage(String token,String msg){
        try{
            log.info("sendMessage start.token:"+token+",msg:"+msg);
            String url = MessageFormat.format(this.sendMsgUrl, token);
            HttpPost post = new HttpPost(url);
            ResponseHandler<?> responseHandler = new BasicResponseHandler();
            StringEntity entity = new StringEntity(msg);
            post.setEntity(entity);
            String response = (String) this.webClient.execute(post, responseHandler);
            log.info("return response=====start======");
            log.info(response);
            log.info("return response=====end======");
            return response;

        }catch (Exception e) {
            log.error("get user info exception", e);
            return null;
        }
    }
    /**
     * @desc 拉取用户信息
     * @param token
     * @param openid
     * @return
     */
    // public WeixinOpenUser getUserInfo(String token, String openid) {
    //     try {
    //         log.info("getUserInfo start.{token:" + token + ",openid:" + openid + "}");
    //         String url = MessageFormat.format(this.getUserInfoUrl, token, openid);
    //         String response = executeHttpGet(url);
    //         JsonNode json = JsonUtils.read(response);
    //         if (json.get("openid") != null) {
    //             WeixinOpenUser user = new WeixinOpenUser();
    //             user.setOpenUserId(json.get("openid").asText());
    //             user.setState(json.get("subscribe").asText());
    //             if ("1".equals(user.getState())) {
    //                 user.setUsername(json.get("nickname").asText());
    //                 user.setSex(json.get("sex").asText());
    //                 user.setCity(json.get("city").asText());
    //                 user.setLanguage(json.get("language").asText());
    //             }
    //             return user;
    //         }
    //     } catch (Exception e) {
    //         log.error("get user info exception", e);
    //     }
    //     return null;
    // }
    /**
     * @desc 发起HTTP GET请求返回数据
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    private String executeHttpGet(String url) throws IOException, ClientProtocolException {
        ResponseHandler<?> responseHandler = new BasicResponseHandler();
        String response = (String) this.webClient.execute(new HttpGet(url), responseHandler);
        log.info("return response=====start======");
        log.info(response);
        log.info("return response=====end======");
        return response;
    }
}
