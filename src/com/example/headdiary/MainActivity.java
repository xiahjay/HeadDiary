package com.example.headdiary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import com.example.headdiary.data.StrConfig;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.data.Config.DBConfig;
import com.example.headdiary.util.DBManager;
import com.igexin.sdk.PushManager;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final String TAG="MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
		
	    //deleteDB(DBConfig.DB_FULLNAME);
		importDB(DBConfig.DB_FULLNAME);

		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent;
				
				if (DBManager.getLastUser()){
					intent=new Intent(MainActivity.this,HomeActivity.class);
					startActivity(intent);
				}
				else{
					intent=new Intent(MainActivity.this,LoginActivity.class);
					startActivity(intent);
				}
				setLanguage();
				MainActivity.this.finish();
			}
		}, 1000);//1000
		
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setLanguage(){
		int lan=UserDAO.getInstance().getLanguage();
		Resources resources =getResources();//获得res资源对象  
		Configuration config = resources.getConfiguration();//获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
		switch(lan){
		case 0:
			config.locale=Locale.getDefault();
			break;
		case 1:
			config.locale=Locale.SIMPLIFIED_CHINESE;
			break;
		case 2:
			config.locale=Locale.ENGLISH;
			break;
		}
		resources.updateConfiguration(config, dm);
		StrConfig.initStrConfig();
	}
	
	public void deleteDB(String dbfile) {
		File f=new File(dbfile);
		if(f.exists()){
			f.delete();
			Log.i(TAG, "Delete DB sucessfully!");
		}
	}
	
	public void importDB(String dbfile) {
		int BUFFER_SIZE = 400000;
		
        try {
            if (!(new File(dbfile).exists())) {
            	//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            	Log.i(TAG, "try to import DB!");
                InputStream is = getResources().openRawResource(R.raw.headdiary); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                Log.i(TAG,"Import DB sucessfully!");
            }
            else{
            	Log.i(TAG, "DB exists!");
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            db.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG+"-Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG+"-Database", "IO exception");
            e.printStackTrace();
        }
	}
	


}
