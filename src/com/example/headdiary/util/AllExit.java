package com.example.headdiary.util;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class AllExit extends Application{

	public AllExit() {
		// TODO Auto-generated constructor stub
	}

	private List<Activity> activityList = new LinkedList<Activity>(); 
	private static AllExit instance;

 	
    public static AllExit getInstance(){
        if(null == instance){
        	instance = new AllExit();
        }
        return instance;             
    }
    
    //添加Activity到容器中
    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    
    //遍历所有Activity并finish
    public void exit(){
    	for(Activity activity:activityList){
    		activity.finish();
    	}
      // System.exit(0);
    }         
}
