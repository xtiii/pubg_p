package com.xtpic.pubg;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class MainActivity extends Activity 
{

	private SwipeRefreshLayout swipe;
	private ListView lv1;
	private String[] Id;
	private String[] Date;
	static String[] Title;
	static String timeStamp;
	static String title;
	static String Banben = "0.5_beta";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
		lv1 = (ListView) findViewById(R.id.listview);
		//设置下拉转圈圈颜色
		swipe.setColorSchemeResources(android.R.color.holo_blue_light);
		//获取当前时间戳
		long Unlongtime = System.currentTimeMillis() / 1000;
		String UntimeStamp = String.valueOf(Unlongtime);
		//自动检测更新
		UpgetCode(UntimeStamp);
		//弹出下拉刷新转圈圈
		swipe.setRefreshing(true);
		//获取画质代码
		getPicCode();

		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					// TODO: Implement this method
					lv1.setAdapter(null);
					getPicCode();
				}
			});
    }

	//自动检测更新
	private void UpgetCode(String timeStamp)
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
					uphandler.sendMessage(msg);
				}
				catch (Exception e)
				{
					Log.e("连接服务器失败", e.toString());
					Toast.makeText(MainActivity.this, "连接服务器失败", 0).show();
				}
			}

		}.start();
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

	//获取画质代码
	private void getPicCode()
	{
		// TODO: Implement this method
		final String url="http://hkss.yes1.cn/ajax.php";
		final String timeStamps = "act=subject" + "&utime=hello";
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

	//自动检测更新的Handler
	Handler uphandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
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
				String Edition = ajax.getString("Edition");
				String code = String.valueOf(ajax.getInt("Code"));
				if (Edition.equals(Banben))
				{
					Log.e("Pubg 画质助手","已经是最新版本");
					return;
				}
				else if  (code.equals("1"))
				{
					String Force = String.valueOf(ajax.getInt("Force"));
					if (Force.equals("1"))
					{
						dialog.setTitle("重大更新");
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
						dialog.setTitle("微量更新");
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
			}
			catch (JSONException e)
			{
				Log.e("Pubg 画质助手", e.toString());
			}
		}
	};

	//手动检测更新还有获取画质代码的Handler
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
						dialog.setTitle("重大更新");
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
						dialog.setTitle("微量更新");
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
					JSONArray array = ajax.getJSONArray("Body");
					ArrayList<String> id=new ArrayList<String>();
					ArrayList<String> date=new ArrayList<String>();
					ArrayList<String> title=new ArrayList<String>();
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject item = array.getJSONObject(i);
						title.add(item.getString("Title"));
						id.add(item.getString("Id"));
						date.add(item.getString("Date"));
					}
					Title = title.toArray(new String[title.size()]);
					Id = id.toArray(new String[id.size()]);
					Date = date.toArray(new String[date.size()]);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
					for (int i = 0; i < Id.length;i++)
					{
						adapter.add(Title[i]);
					}
					lv1.setAdapter(adapter);
					lv1.setOnItemClickListener(new OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
							{
								// TODO: Implement this method
								String listid = String.valueOf(Id[p3]);
								Intent Picture = new Intent();
								Picture.setClass(MainActivity.this, Picture.class);
								Bundle bundle=new Bundle();
								bundle.putString("ListId", listid);
								Picture.putExtras(bundle);
								startActivity(Picture);
							}
						});
					//关闭下拉刷新转圈圈
					swipe.setRefreshing(false);
					return;
				}
				else
				{
					Toast.makeText(MainActivity.this, "错误代码：" + code, 0).show();
				}
			}
			catch (JSONException e)
			{
				Log.e("Pubg 画质助手", e.toString());
			}
		}

	};

}
