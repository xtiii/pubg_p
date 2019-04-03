package com.xtpic.pubg;

import com.xtpic.*;
import java.net.*;
import java.io.*;

public class GetService
{

	public static String getHtml(String url) throws Exception
	{
		URL urls = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)urls.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(3000);
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
		return null;
	}

}
