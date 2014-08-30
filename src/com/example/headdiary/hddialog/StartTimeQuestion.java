package com.example.headdiary.hddialog;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.headdiary.ActivityManager;
import com.example.headdiary.HomeActivity;
import com.example.headdiary.R;
import com.example.headdiary.UnfinishedDiaryActivity;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.util.TimeManager;
import com.speedven.pickview.widget.NumericWheelAdapter;
import com.speedven.pickview.widget.OnWheelScrollListener;
import com.speedven.pickview.widget.WheelView;

public class StartTimeQuestion extends Activity {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	long nowTimeinMs=0;	
	private Button btnCancel;
	private Button btnConfirm;
	private UnfinishedDiaryActivity mUnfinishedDiaryActivity;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_time_dialog);		
		btnCancel=(Button)findViewById(R.id.start_btn_cancel);
		btnCancel.setText("放弃记录");
		btnConfirm=(Button)findViewById(R.id.start_btn_confirm);
		btnConfirm.setText("下一步");
		
		initDateTime();
		
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){  
	        //捕捉返回键
	    	finish();
	    	Intent intent = new Intent();  
			intent.setClass(StartTimeQuestion.this, HomeActivity.class);  
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
			startActivity(intent);  
			}
            return true;
	    
	    } 
	
	private void initDateTime(){
		Calendar c;
		if (headacheDiary.getStartTime()==null)
			c= Calendar.getInstance();
		else {
			c=TimeManager.parseStrDateTime(headacheDiary.getStartTime());
		}
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		int curDate = c.get(Calendar.DATE);
		int curHour=c.get(Calendar.HOUR_OF_DAY);
		int curMin=c.get(Calendar.MINUTE);
		
		year = (WheelView) findViewById(R.id.start_wheel_year);
		year.setAdapter(new NumericWheelAdapter(2000, 2099));
		year.setLabel(this.getString(R.string.title_year));
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		
		month = (WheelView) findViewById(R.id.start_wheel_month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel(this.getString(R.string.title_month));
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		
		day = (WheelView) findViewById(R.id.start_wheel_day);
		initDay(curYear,curMonth);
		day.setLabel(this.getString(R.string.title_date));
		day.setCyclic(true);

		year.setCurrentItem(curYear);
		month.setCurrentItem(curMonth);
		day.setCurrentItem(curDate - 1);
		
		hour = (WheelView) findViewById(R.id.start_wheel_hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel(this.getString(R.string.title_hour));
		hour.setCyclic(true);
		mins = (WheelView) findViewById(R.id.start_wheel_mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel(this.getString(R.string.title_min));
		mins.setCyclic(true);
		
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMin);
		
		Calendar initTime=Calendar.getInstance();
		initTime.set(year.getCurrentItem()+2000, month.getCurrentItem(), day.getCurrentItem()+1, hour.getCurrentItem(), mins.getCurrentItem(),0);
		nowTimeinMs= Calendar.getInstance().getTimeInMillis();
		
	}

	
	public void onClickCancel(View v){
		finish();	
		Intent intent = new Intent();  
		intent.setClass(StartTimeQuestion.this, HomeActivity.class);  
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
		startActivity(intent);  
		//ActivityManager.getInstance().exit();
		//mUnfinishedDiaryActivity.finish();
	}
	
	public void onClickConfirm(View v){
		long endTimeinMs;
		Calendar startTime=Calendar.getInstance();
		startTime.set(year.getCurrentItem()+2000, month.getCurrentItem(), day.getCurrentItem()+1, hour.getCurrentItem(), mins.getCurrentItem(),0);
		if (startTime.getTimeInMillis()<nowTimeinMs+60000){  //允许�?��钟以内的误差
			headacheDiary.setStartTime(TimeManager.getStrDateTime(startTime));
			
			Calendar endTime=TimeManager.parseStrDateTime(headacheDiary.getEndTime());
			if (endTime!=null){
				endTimeinMs=endTime.getTimeInMillis();
				if (endTimeinMs<startTime.getTimeInMillis()+60000){	//要求至少有一分钟的时�?
					Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_end_time_early), Toast.LENGTH_SHORT).show();	
					headacheDiary.setEndTime(null);
				}
			}
		}
		else
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_start_time), Toast.LENGTH_SHORT).show();	
		
		Intent intent = new Intent (StartTimeQuestion.this,AchePositionQuestion.class);	
		startActivity(intent);
		
		finish();
	}
	
	/**
	 * 滑动日期的响�?
	 */
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 2000;
			int n_month = month.getCurrentItem();
			initDay(n_year,n_month);

		}
	};

	/**
	 * 已知年�?月，获取该月有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {// 璁＄畻鏄惁鏄棸骞�?
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month+1) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 * 给控件设置该月的天数
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

}
