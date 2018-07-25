package com.example.demo;

import com.example.demo.controller.WeiXinApi;
import com.example.demo.domain.AccessToken;
import com.example.demo.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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

		AccessToken token = WeixinUtil.getAccessToken();
		// String path = "D:/image.jpg";
		// JSONObject object = addMaterialEver(path,"image",token.getToken());
		// System.out.println(object.toString());

		System.out.println("---");

		WeiXinApi weiXinApi = new WeiXinApi();
		// weiXinApi.initWebClient();
		weiXinApi.sendMessage(token.getToken(),"ssssssssssss");
	}




}
