package com.example.demo;

import com.example.demo.domain.AccessToken;
import com.example.demo.util.WeixinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public void getAccessToken() throws IOException {
		AccessToken accessToken = WeixinUtil.getAccessToken();
		System.out.println("access_token = "+accessToken.getToken());
		System.out.println("expires_in = "+accessToken.getExpiresIn());
	}

}
