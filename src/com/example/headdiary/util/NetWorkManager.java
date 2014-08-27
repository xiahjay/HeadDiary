package com.example.headdiary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkManager {

	public NetWorkManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isNetworkConnected(Context context) {
		Boolean flag=false;
		
		if (context != null) {  
	       ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	       NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	       if (mNetworkInfo != null)
	    	   flag=mNetworkInfo.isAvailable();  
		} 
		else
			System.out.println("Context is NULL!");
			
		return flag;  
	 }
	
	public boolean isWifiConnected(Context context) { 
		Boolean flag=false;
		
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	          NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	          if (mWiFiNetworkInfo != null)
	              flag=mWiFiNetworkInfo.isAvailable();  
	     }  
	     else
				System.out.println("Context is NULL!");
	     
	     return flag;  
	 }
}
