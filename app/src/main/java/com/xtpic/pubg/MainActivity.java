package com.xtpic.pubg;

import com.xtpic.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.*;
import java.util.concurrent.locks.*;
import android.support.v4.widget.*;
import org.json.*;

public class MainActivity extends Activity 
{

	private SwipeRefreshLayout swipe;
	static String timeStamp;
	static String title;
	static String Banben = "0.2";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
		swipe.setColorSchemeResources(android.R.color.holo_blue_light);
		//获取当前时间戳
		long longtime = System.currentTimeMillis() / 1000;
		timeStamp = String.valueOf(longtime);
		//弹出下拉刷新转圈圈
		swipe.setRefreshing(true);
		//获取画质代码
		getPicCode(timeStamp);

		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					// TODO: Implement this method
					getPicCode(timeStamp);
				}
			});
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
				long longtime = System.currentTimeMillis() / 1000;
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
					String result = PostService.getHtml(url, timeStamps);
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{
					Log.e("连接服务器失败", e.toString());
					Toast.makeText(MainActivity.this, "连接服务器失败", 0).show();
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
					String result = PostService.getHtml(url, timeStamps);
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{
					Log.e("连接服务器失败", e.toString());
					Toast.makeText(MainActivity.this, "连接服务器失败", 0).show();
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
			String pd = String.valueOf(result.indexOf("请求失败"));
			if (pd.equals("0"))
			{
				Toast.makeText(MainActivity.this, result, 0).show();
				return;
			}
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			try
			{
				JSONObject ajax = new JSONObject(result);
				String code = String.valueOf(ajax.getInt("Code"));
				if (code.equals("1"))
				{
					String Edition = ajax.getString("Edition");
					String Force = String.valueOf(ajax.getInt("Force"));
					if (Edition.equals(Banben))
					{
						Toast.makeText(MainActivity.this, "已经是最新版本", 0).show();
						return;
					}
					else if (Force.equals("1"))
					{
						dialog.setTitle("强制更新");
						dialog.setCancelable(false);
						dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
									MainActivity.this.finish();
								}
							});
					}
					else
					{
						dialog.setTitle("正常更新");
						dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
								}
							});
						dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
								}
							});
					}
					JSONArray array = ajax.getJSONArray("Body");
					title = "更新日期：" + ajax.getString("Date") + "\n";
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject item = array.getJSONObject(i);
						title = title + item.getString("Title") + "\n";
					}
					String titles = title.trim();
					dialog.setMessage(titles);
					dialog.show();
					return;
				}
				else if (code.equals("0"))
				{
					//关闭下拉刷新转圈圈
					swipe.setRefreshing(false);
					return;
				}
			}
			catch (JSONException e)
			{

			}
		}

	};
}
