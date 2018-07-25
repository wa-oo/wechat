package com.example.demo;

import com.example.demo.domain.AccessToken;
import com.example.demo.util.WeixinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.example.demo.util.WeixinUtil.addMaterialEver;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void getAccessToken() throws IOException {
		AccessToken accessToken = WeixinUtil.getAccessToken();
		System.out.println("access_token = "+accessToken.getToken());
		System.out.println("expires_in = "+accessToken.getExpiresIn());

		String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		int result = WeixinUtil.createMenu(accessToken.getToken(),menu);
		if (result == 0){
			System.out.println("创建菜单成功");
		}else {
			System.out.println("错误码:" + result);
		}
	}

	@Test
	public void uploadFodderController() throws IOException {
		WeixinUtil weixinUtil = new WeixinUtil();
		AccessToken token = WeixinUtil.getAccessToken();
		String path = "D:/image.jpg";
		//上传图文消息缩略图
		JSONObject object = addMaterialEver(path,"thumb",token.getToken());

		//查找素材表 从0开始,10个
		JSONObject json1=new JSONObject();
		json1.put("type","image");
		json1.put("offset","0");
		json1.put("count","10");
		JSONObject jsonImageText1 = weixinUtil.doPostStr("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+token.getToken(), json1);

		//上传图文消息素材
		JSONObject json=new JSONObject();
		JSONArray jsonlist=new JSONArray();
		JSONObject js=new JSONObject();
		js.put("author","Allen");
		js.put("content","sssssssssssssssssssss");
		js.put("content_source_url","http://www.baidu.com");
		js.put("digest","Allen");
		js.put("show_cover_pic","1");
		//缩略图id
		js.put("thumb_media_id","4H7Dn04swNqzmUcNwYmhhOufHYq_I2EE_jqgAgzPnONcjnkILYl8znJ9FyCHgiGu");
		js.put("title","11111");
		jsonlist.add(js);
		json.put("articles",jsonlist);

		JSONObject jsonImageText = weixinUtil.doPostStr("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token="+token.getToken(), json);

		//获取openID
        List list =  weixinUtil.getUserOpenId(token.getToken());

        //推送图文消息
		JSONObject jsonImage = new JSONObject();
		JSONArray jsonToUser = new JSONArray();
		JSONObject jsonNews = new JSONObject();
		jsonToUser.add(list);
		//media_id通过素材表返回的json查找
		jsonNews.put("media_id","0qVmFWIIJS9w2FGgC-1MEU0VmGu2s1qzw5K-b1GSsDtxKlS6WgbqVBYcXB2WgLgB");
		jsonImage.put("touser",list);
		jsonImage.put("mpnews",jsonNews);
		jsonImage.put("msgtype","mpnews");
		jsonImage.put("send_ignore_reprint",0);
		JSONObject jsonObject = weixinUtil.doPostStr("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="+token.getToken(), jsonImage);
	}




}
