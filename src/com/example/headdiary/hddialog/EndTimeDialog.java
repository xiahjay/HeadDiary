package com.example.headdiary.hddialog;

import java.util.Calendar;

import com.example.headdiary.HomeActivity;
import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.util.TimeManager;
import com.speedven.pickview.widget.NumericWheelAdapter;
import com.speedven.pickview.widget.OnWheelScrollListener;
import com.speedven.pickview.widget.WheelView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EndTimeDialog extends Activity {

	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;
	private HeadacheDiary headacheDiary=HeadacheDiaryDAO.getInstance().getHeadacheDiarySelected();
	long nowTimeinMs=0;
	private Button btnNotFinished;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_time_dialog);
		btnNotFinished=(Button)findViewById(R.id.end_btn_cancel);
		btnNotFinished.setText("取消");
		initDateTime();
		
	}
	
	private void initDateTime(){
		Calendar c;
		if (headacheDiary.getEndTime()==null)
			c= Calendar.getInstance();
		else {
			c=TimeManager.parseStrDateTime(headacheDiary.getEndTime());
		}
		
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		int curDate = c.get(Calendar.DATE);
		int curHour=c.get(Calendar.HOUR_OF_DAY);
		int curMin=c.get(Calendar.MINUTE);
		
		
		year = (WheelView) findViewById(R.id.end_wheel_year);
		year.setAdapter(new NumericWheelAdapter(2000, 2099));
		year.setLabel(this.getString(R.string.title_year));
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		
		month = (WheelView) findViewById(R.id.end_wheel_month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel(this.getString(R.string.title_month));
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		
		day = (WheelView) findViewById(R.id.end_wheel_day);
		initDay(curYear,curMonth);
		day.setLabel(this.getString(R.string.title_date));
		day.setCyclic(true);

		year.setCurrentItem(curYear);
		month.setCurrentItem(curMonth);
		day.setCurrentItem(curDate - 1);
		
		hour = (WheelView) findViewById(R.id.end_wheel_hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel(this.getString(R.string.title_hour));
		hour.setCyclic(true);
		mins = (WheelView) findViewById(R.id.end_wheel_mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel(this.getString(R.string.title_min));
		mins.setCyclic(true);
		
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMin);
		
		Calendar initTime=Calendar.getInstance();
		initTime.set(year.getCurrentItem()+2000, month.getCurrentItem(), day.getCurrentItem()+1, hour.getCurrentItem(), mins.getCurrentItem(),0);
		nowTimeinMs= Calendar.getInstance().getTimeInMillis();
		
	}
	
	
	public void onClickAcheNotFinished(View v){
		//headacheDiary.setEndTime(null);
		finish();		
	}
	
	public void onClickConfirm(View v){
		long startTimeinMs;
		Calendar endTime=Calendar.getInstance();
		endTime.set(year.getCurrentItem()+2000, month.getCurrentItem(), day.getCurrentItem()+1, hour.getCurrentItem(), mins.getCurrentItem(),0);

		Calendar startTime=TimeManager.parseStrDateTime(headacheDiary.getStartTime());
		if (startTime!=null)
			startTimeinMs=startTime.getTimeInMillis();
		else
			startTimeinMs=0;
		
		if (endTime.getTimeInMillis()>=nowTimeinMs+60000)  //允许�?��钟以内的误差
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_end_time_late), Toast.LENGTH_SHORT).show();	
		else if (endTime.getTimeInMillis()<startTimeinMs+60000)	//要求至少有一分钟的时�?
			Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_end_time_early), Toast.LENGTH_SHORT).show();	
		else
			{headacheDiary.setEndTime(TimeManager.getStrDateTime(endTime));		
		finish();
			}
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
