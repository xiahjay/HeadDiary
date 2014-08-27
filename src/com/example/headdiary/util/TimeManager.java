package com.example.headdiary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;

public class TimeManager {
	private static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private static Calendar c;
	
	public TimeManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getStrDate(){
		c=Calendar.getInstance();
		String strDate=DateTimeFormat.format(c.getTime());
		strDate=strDate.substring(0, 10);
		return strDate;
	}
	
	public static String getStrDate(Calendar time){
		if(time==null)
			return "";
		String strDate=DateTimeFormat.format(time.getTime());
		strDate=strDate.substring(0, 10);
		return strDate;
	}
	
	public static String getStrTime(){
		c=Calendar.getInstance();
		String strDate=DateTimeFormat.format(c.getTime());
		strDate=strDate.substring(11,16);
		return strDate;
	}
	
	public static String getStrTime(Calendar time){
		if(time==null)
			return "";
		String strDate=DateTimeFormat.format(time.getTime());
		strDate=strDate.substring(11,16);
		return strDate;
	}
	
	public static String getStrDateTime(){
		c=Calendar.getInstance();
		String strDate=DateTimeFormat.format(c.getTime());
		return strDate;
	}
	
	public static String getStrDateTime(Calendar time){
		if(time==null)
			return "";
		String strDate=DateTimeFormat.format(time.getTime());
		return strDate;
	}
	
	public static Calendar parseStrDateTime(String strDate){
		if (strDate==null)
			return null;
		Date date = null;
		Calendar time =Calendar.getInstance();
		try {
			date = DateTimeFormat.parse(strDate);
			time.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return time;
	}
	
	public static int getMonthInterval(Calendar start, Calendar end){
		long interval=end.getTimeInMillis()-start.getTimeInMillis();
		int month=(int) (interval/30/24/3600/1000);
		return month;
	}

	public static String getStrDayHourMin(long min) {
		// TODO Auto-generated method stub
		String strTime="";
		int hour=0,day=0;
        if (min>60){
        	hour=(int) (min/60);
        	min=min%60;
        	if (hour>24){
        		day=hour/24;
        		hour=hour%24;
        	}
        }

        if (day>0)
        	strTime+=Integer.toString(day)+"天";
        if (hour>0)
        	strTime+=Integer.toString(hour)+"小时";
        if (min>0)
        	strTime+=Integer.toString((int)min)+"分钟";
        return strTime;
	}


}
