package com.xtpic.pubg;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import java.io.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class Picture extends Activity
{
	private EditText ed1;
	private Button bt1;
	private Switch sw1;
	private String cmd;
	private String listid;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_allocation);
		listid = getIntent().getStringExtra("ListId");
		ed1 = (EditText) findViewById(R.id.ed1);
		bt1 = (Button) findViewById(R.id.bt1);
		sw1 = (Switch) findViewById(R.id.sw1);

		//获取画质配置代码
		getPicCode(listid);

		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		String name = pref.getString("switch", "0");
		if (name.equals("1"))
		{
			sw1.setChecked(true);
		}
		else
		{
			sw1.setChecked(false);
		}

		sw1.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				private String[] command;

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					// TODO: Implement this method
					if (p2)
					{
						cmd = "chmod 444 /data/media/0/Android/data/com.tencent.tmgp.pubgmhd/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/UserCustom.ini";
						String fan = Pubg_pic_lock(cmd);
						SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
						editor.putString("switch", "1");
						editor.commit();
						Toast.makeText(Picture.this, fan, 0).show();
					}
					else
					{
						cmd = "chmod 664 /data/media/0/Android/data/com.tencent.tmgp.pubgmhd/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/UserCustom.ini";
						String fan = Pubg_pic_lock(cmd);
						SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
						editor.putString("switch", "0");
						editor.commit();
						Toast.makeText(Picture.this, fan, 0).show();
					}
				}
			});

		bt1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					String PicCodes = ed1.getText().toString();
					File file = new File("/storage/emulated/0/Android/data/com.tencent.tmgp.pubgmhd/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/", "UserCustom.ini");
					String UserCode = readUserComtom(file);
					if (UserCode == "false"){
						Toast.makeText(Picture.this,"画质配置不存在，请先运行一遍刺激战场",0).show();
						return;
					}
					String picCode = UserCode.substring(0, UserCode.indexOf("[UserCustom DeviceProfile]"));
					String PicCode = picCode + PicCodes;
					writeUserComtom(PicCode, file);
				}
			});
	}

	private static String Pubg_pic_lock(String cmd)
	{

		DataOutputStream os = null;
		Process process = null;
        //adb push core code
        try
		{
            process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			return "true";
        }
		catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.close();
				}
				process.destroy();

			}
			catch (IOException e)
			{
				Log.e("Pubg 画质助手",e.toString());
			}
		}
	}

	private void getPicCode(String ListId)
	{
		// TODO: Implement this method
		final String url="http://hkss.yes1.cn/" + ListId + ".html";
		new Thread(){

			@Override
			public void run()
			{
				try
				{
					String results = GetService.getHtml(url);
					Message msg = new Message();
					msg.obj = results;
					handler.sendMessage(msg);
				}
				catch (Exception e)
				{
					Log.e("连接服务器失败", e.toString());
					Toast.makeText(Picture.this, "连接服务器失败", 0).show();
				}
			}

		}.start();
	}

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			String result = (String)msg.obj;
			ed1.setText(result);
		}
	};

	//读取画质代码的备份段
	private String readUserComtom(File file)
	{
		// TODO: Implement this method
		String str = null;
		try
		{
			InputStream is = new FileInputStream(file);
			InputStreamReader input = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(input);
			StringBuilder sb = new StringBuilder("");
			while ((str = reader.readLine()) != null)
			{
				sb.append(str);
				sb.append("\n");
			}
			return sb.toString();
		}
		catch (FileNotFoundException e)
		{
			Log.e("Pubg 画质助手", e.toString());
			return "false";
		}
		catch (IOException e)
		{
			Log.e("Pubg 画质助手", e.toString());
			return "false";
		}
	}

	//写入画质代码的主段
	private void writeUserComtom(String PicCode, File file)
	{
		// TODO: Implement this method
		try
		{
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(PicCode.getBytes());
			outStream.close();
			Toast.makeText(Picture.this, "修改成功", 0).show();
		}
		catch (IOException e)
		{
			Log.e("Pubg 画质助手", e.toString());
			Toast.makeText(Picture.this, "修改失败，请先解除画质锁定", 0).show();
		}
	}
}
