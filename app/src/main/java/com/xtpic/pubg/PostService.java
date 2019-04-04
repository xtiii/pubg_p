package com.xtpic.pubg;

import com.xtpic.*;
import java.net.*;
import java.io.*;

public class PostService
{

	public static String getHtml(String url, String par) throws Exception
	{
		byte[] pars = par.toString().getBytes();
		URL urls = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setDefaultUseCaches(false);
		conn.setConnectTimeout(3000);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(pars.length));
		OutputStream outputStream = conn.getOutputStream();
		outputStream.write(pars);

		int code = conn.getResponseCode();
		if (code == 200)
		{
			InputStream inStream = conn.getInputStream();//通过输入流获取html数据
			byte[] data = StreamTool.readInputStream(inStream);//得到html的二进制数据
			String html = new String(data, "utf-8");
			conn.disconnect();//关闭链接
			inStream.close();
			return html;
		}
		else
		{
			String codes = String.valueOf(code);
			return "请求失败，错误代码：" + codes;
		}
	}
	
}
