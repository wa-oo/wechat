package com.example.demo.util;

import com.example.demo.domain.AccessToken;
import com.example.demo.domain.UserInfo;
import com.example.demo.domain.menu.Button;
import com.example.demo.domain.menu.ClickButton;
import com.example.demo.domain.menu.Menu;
import com.example.demo.domain.menu.ViewButton;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeixinUtil {
    public final static String APPID = "wx139f89139d0670e9";

    public final static String APPSECRET = "f51af4f64f433bf606e3ec493074a65d";
    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // 创建菜单
    public final static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 存放：1.token，2：获取token的时间,3.过期时间
    public final static Map<String,Object> accessTokenMap = new HashMap<String,Object>();

    /**
     * GET请求
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetStr(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        //接受变量
        JSONObject jsonObject = null;
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null){
            String result = EntityUtils.toString(httpEntity,"UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        return jsonObject;
    }

    /**
     * POST请求
     * @param url
     * @param outStr
     * @return
     * @throws IOException
     */
    public static JSONObject doPostStr(String url,JSONObject outStr) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpPost.setEntity(new StringEntity(String.valueOf(outStr),"UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        jsonObject = JSONObject.fromObject(result);
        return jsonObject;
    }

    /**
     * 获取accessToken
     * @return
     * @throws IOException
     */
    public static AccessToken getAccessToken() throws IOException {
        AccessToken accessToken = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null) {
            accessToken.setToken(jsonObject.getString("access_token"));
            accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        return accessToken;
    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMenu(){
        Menu menu = new Menu();

        ClickButton button11 = new ClickButton();
        button11.setName("Click菜单");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("View菜单");
        button21.setType("view");
        button21.setUrl("https://github.com/wangtao-Allen");

        ClickButton button31 = new ClickButton();
        button31.setName("扫码");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("地理位置");
        button32.setType("location_select");
        button32.setKey("32");

        Button button3 = new Button();
        button3.setName("功能菜单");
        button3.setSub_button(new Button[]{button31,button32});

        menu.setButton(new Button[]{button11,button21,button3});

        return menu;
    }

    /**
     * 创建菜单
     * @param accessToken
     * @param menu
     * @return
     * @throws IOException
     */
    public static int createMenu(String accessToken,String menu) throws IOException {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN",accessToken);
        JSONObject jsonObject = doPostStr(url, JSONObject.fromObject(menu));
        if (jsonObject != null) {
            result = jsonObject.getInt("errcode");
        }
        return result;
    }

    /**
     * 上传永久图片素材
     * @param fileurl
     * @param type
     * @param token
     * @return
     */
    public static JSONObject addMaterialEver(String fileurl, String type, String token) {
        try {
            File file = new File(fileurl);
            //上传素材
            String path = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + token + "&type=" + type;
            String result = connectHttpsByPost(path, null, file);
            result = result.replaceAll("[\\\\]", "");
            System.out.println("result:" + result);
            JSONObject resultJSON = JSONObject.fromObject(result);
            if (resultJSON != null) {
                if (resultJSON.get("media_id") != null) {
                    System.out.println("上传" + type + "永久素材成功");
                    return resultJSON;
                } else {
                    System.out.println("上传" + type + "永久素材失败");
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  String connectHttpsByPost(String path, String KK, File file) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL urlObj = new URL(path);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        String result = null;
        con.setDoInput(true);

        con.setDoOutput(true);

        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary="
                        + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""

                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);

        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        System.out.println("result---" + result);
        return result;

    }

    /**
     * 获取公众号关注的用户openid
     * @return
     */
    public List<String> getUserOpenId(String access_token)//, String nextOpenid
    {
        // String path = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        String path = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
        path = path.replace("ACCESS_TOKEN", access_token);//.replace("NEXT_OPENID", nextOpenid)
        System.out.println("path:" + path);

        List<String> result = null;
        try
        {
            JSON strResp = WeixinUtil.doGetStr(path);
            System.out.println(strResp);

            JSONObject  jasonObject = JSONObject.fromObject(strResp);
            Map map = (Map)jasonObject;
            Map tmapMap = (Map) map.get("data");

            result = (List<String>) tmapMap.get("openid");

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 通过用户openid 获取用户信息
     * @param userOpenids
     * @return
     */
    public List<UserInfo> getUserInfo(List<String> userOpenids) throws IOException {
        // 1、获取access_token
        // 使用测试 wx9015ccbcccf8d2f5 02e3a6877fa5fdeadd78d0f6f3048245
        AccessToken token = WeixinUtil.getAccessToken();
        String tAccess_Token = token.getToken();
        // 2、封装请求数据
        List user_list = new ArrayList<Map>();
        for (int i = 0; i < userOpenids.size(); i++)
        {
            String openid = userOpenids.get(i);
            Map tUserMap = new HashMap<String, String>();
            tUserMap.put("openid", openid);
            tUserMap.put("lang", "zh_CN");
            user_list.add(tUserMap);
        }
        System.out.println(user_list.toString());
        Map requestMap = new HashMap<String, List>();
        requestMap.put("user_list", user_list);
        String tUserJSON = JSONObject.fromObject(requestMap).toString();

        // 3、请求调用
        JSON result = getUserInfobyHttps(tAccess_Token, tUserJSON);

        JSONObject  jasonObject = JSONObject.fromObject(result);
        String tMapData = jasonObject.toString();

        System.out.println(result);

        // 4、解析返回将结果
        return parseUserInfo(tMapData);
    }

    /**
     * 解析返回用户信息数据
     * @param userInfoJSON
     * @return
     */
    private List<UserInfo> parseUserInfo(String userInfoJSON)
    {
        List user_info_list = new ArrayList<UserInfo>();

        JSONObject  jasonObject = JSONObject.fromObject(userInfoJSON);
        Map tMapData = (Map)jasonObject;

        List<Map> tUserMaps = (List<Map>) tMapData.get("user_info_list");

        for (int i = 0; i < tUserMaps.size(); i++)
        {
            UserInfo tUserInfo = new UserInfo();
            tUserInfo.setSubscribe((Integer) tUserMaps.get(i).get("subscribe"));
            tUserInfo.setSex((Integer) tUserMaps.get(i).get("sex"));
            tUserInfo.setOpenId((String) tUserMaps.get(i).get("openid"));
            tUserInfo.setNickname((String) tUserMaps.get(i).get("nickname"));
            tUserInfo.setLanguage((String) tUserMaps.get(i).get("language"));
            tUserInfo.setCity((String) tUserMaps.get(i).get("city"));
            tUserInfo.setProvince((String) tUserMaps.get(i).get("province"));
            tUserInfo.setCountry((String) tUserMaps.get(i).get("country"));
            tUserInfo.setHeadimgurl((String) tUserMaps.get(i).get("headimgurl"));
            tUserInfo.setSubscribetime((Integer) tUserMaps.get(i).get("subscribe_time"));
            tUserInfo.setRemark((String) tUserMaps.get(i).get("remark"));
            tUserInfo.setGroupid((Integer) tUserMaps.get(i).get("groupid"));
            user_info_list.add(tUserInfo);
        }

        return user_info_list;
    }

    /**
     * 调用HTTPS接口，获取用户详细信息
     * @param access_token
     * @param requestData
     * @return
     */
    private JSON getUserInfobyHttps(String access_token, String requestData)
    {
        // 返回报文
        JSON strResp = null;
        String path = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
        path = path.replace("ACCESS_TOKEN", access_token);

        try
        {
            strResp = WeixinUtil.doPostStr(path, JSONObject.fromObject(requestData));
            JSONObject  jasonObject = JSONObject.fromObject(strResp);
            System.out.println("strResp---"+strResp.toString());

        } catch (IOException e)
        {
            // 发生网络异常
            System.out.println(e);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally
        {}
        return strResp;
    }

}
