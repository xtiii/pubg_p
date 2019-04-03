package com.xtpic.pubg;

import java.io.*;

/**
 * 从输入流中获取数据
 * @param inStream 输入流
 * @return
 * @throws Exception
 */
public class StreamTool
{

	public static byte[] readInputStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		inStream.close();//关闭流
		return outStream.toByteArray();
	}
}
