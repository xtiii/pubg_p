package com.xtpic.pubg;

import com.xtpic.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.*;
import java.util.concurrent.locks.*;

public class MainActivity extends Activity 
{

	static String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//获取当前时间戳
		long longtime = System.currentTimeMillis()/1000;
		timeStamp = String.valueOf(longtime);
		//获取画质代码
		getPicCode(timeStamp);
    }

	//右上角菜单
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.main_bar, menu);
		return true;
	}

	//右上角菜单列表点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.Check_update:
				//获取当前时间戳
				long longtime = System.currentTimeMillis()/1000;
				timeStamp = String.valueOf(longtime);
				//检查更新类
				getCode(timeStamp);
				break;
			case R.id.About_us:
				Intent Abouts = new Intent();
				Abouts.setClass(MainActivity.this, About_us.class);
				startActivity(Abouts);
				break;
		}
		return true;
	}

	private void getPicCode(String timeStamp)
	{
		// TODO: Implement this method
		final String url="http://hkss.yes1.cn/ajax.php";
		final String timeStamps = "act=subject" + "&utime=" + timeStamp;
		new Thread(){

			@Override
			public void run()
			{
				try
				{
					String result = PostService.getHtml(url,timeStamps);
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{

				}
			}

		}.start();
	}

	private void getCode(String timeStamp)
	{
		// TODO: Implement this method
		final String url="http://hkss.yes1.cn/ajax.php";
		final String timeStamps = "act=upapp" + "&utime=" + timeStamp;
		new Thread(){
			
			@Override
			public void run()
			{
				try
				{
					String result = PostService.getHtml(url,timeStamps);
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{

				}
			}

		}.start();
	}

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			String result = (String)msg.obj;
			Toast.makeText(MainActivity.this,result,0).show();
		}

	};
}
