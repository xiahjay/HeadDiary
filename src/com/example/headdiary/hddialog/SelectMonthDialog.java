package com.example.headdiary.hddialog;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headdiary.HomeActivity;
import com.example.headdiary.R;
import com.example.headdiary.R.id;
import com.example.headdiary.R.layout;
import com.example.headdiary.R.menu;
import com.example.headdiary.R.string;
import com.example.headdiary.UnfinishedDiaryActivity;
import com.example.headdiary.data.HeadacheDiary;
import com.example.headdiary.data.HeadacheDiaryDAO;
import com.example.headdiary.data.UserDAO;
import com.example.headdiary.util.TimeManager;
import com.speedven.pickview.widget.NumericWheelAdapter;
import com.speedven.pickview.widget.OnWheelScrollListener;
import com.speedven.pickview.widget.WheelView;

public class SelectMonthDialog extends Activity {
	private WheelView year;
	private WheelView month;	
	long nowTimeinMs=0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_month);
		
		initDateTime();
		
	}
	
	private void initDateTime(){
		Calendar c;
		//if ( UserDAO.getInstance().getSelectMonth()==null)
			//c= Calendar.getInstance();
		//else {
			c=TimeManager.parseStrDateTime(UserDAO.getInstance().getSelectMonth());
		//}
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		
		year = (WheelView) findViewById(R.id.select_wheel_year);
		year.setAdapter(new NumericWheelAdapter(2000, 2099));
		year.setLabel(this.getString(R.string.title_year));
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		
		month = (WheelView) findViewById(R.id.select_wheel_month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel(this.getString(R.string.title_month));
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
				
		year.setCurrentItem(curYear);
		month.setCurrentItem(curMonth);		
		
		Calendar initTime=Calendar.getInstance();
		initTime.set(year.getCurrentItem()+2000, month.getCurrentItem(), 0,0, 0,0);
		nowTimeinMs= Calendar.getInstance().getTimeInMillis();
		
	}

	
	public void onClickCancel(View v){
		finish();		
	}
	
	public void onClickConfirm(View v){
		long endTimeinMs;
		Calendar startTime=Calendar.getInstance();
		startTime.set(year.getCurrentItem()+2000, month.getCurrentItem()+1, 0, 0, 0,0);
		String month=TimeManager.getStrDateTime(startTime);
		//month = month.substring(0,7);
			UserDAO.getInstance().setSelectMonth(month);
			UserDAO.getInstance().setSelectCalendar(startTime);
			
					
		
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
			
		}
	};

	
}
