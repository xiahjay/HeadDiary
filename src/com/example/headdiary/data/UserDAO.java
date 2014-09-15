package com.example.headdiary.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import javax.security.auth.PrivateCredentialPermission;

import com.example.headdiary.util.DBManager;
import com.example.headdiary.util.DocumentAdapter;
import com.example.headdiary.util.MessageAdapter;
import com.example.headdiary.util.TimeManager;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;

public class UserDAO {	
	private static UserDAO mInstance;
	private int timePeriod,documentStyle,analysisStyle,graphicStyle,language,listStyle,monthStyle;
	private User user,loginUser,registerUser;
	private Boolean loginFromWeb;
	private String selectMonth;
	private ArrayList<Suggestion> suggestionList;
	private int unreadSuggestion;
	private String pushClientId;
	private String payload;
	private Calendar selectCalendar;
		
	
	
	public UserDAO(){
		timePeriod=3;
		listStyle=documentStyle=analysisStyle=language=0;
		monthStyle=7;
		graphicStyle=1;
		loginFromWeb=false;
		selectMonth="2014-09";
		
	}
	
	public static UserDAO getInstance(){
		if (mInstance==null){
			mInstance=new UserDAO();
		}
		return mInstance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		HeadacheDiaryDAO.newInstance();		//将原来存储的头痛数据清空。否则切换用户之后仍有可能打开的是上次登录的用户的信息。
	}

	public int getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public int getMonth() {
		return monthStyle;
	}

	public void setMonth(int monthStyle) {
		this.monthStyle = monthStyle;
	}

	public String getSelectMonth() {
		return selectMonth;
	}

	public void setSelectMonth(String selectMonth) {
		this.selectMonth = selectMonth;
	}
	
	public int getListStyle() {
		return listStyle;
	}

	public void setListStyle(int listStyle) {
		this.listStyle = listStyle;
	}
	
	public int getDocumentStyle() {
		return documentStyle;
	}
	
	public int getGraphicStyle() {
		return graphicStyle;
	}

	public void setDocumentStyle(int documentStyle) {
		this.documentStyle = documentStyle;
	}

	public int getAnalysisStyle() {
		return analysisStyle;
	}

	public void setAnalysisStyle(int analysisStyle) {
		this.analysisStyle = analysisStyle;
	}

	public User getLoginUser() {
		if (loginUser==null)
			loginUser=new User();
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public User getRegisterUser() {
		if (registerUser==null)
			registerUser=new User();
		return registerUser;
	}

	public void setRegisterUser(User registerUser) {
		this.registerUser = registerUser;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public Boolean getLoginFromWeb() {
		return loginFromWeb;
	}

	public void setLoginFromWeb(Boolean loginFromWeb) {
		this.loginFromWeb = loginFromWeb;		
	}
	
	
	
   //新添方法
	public ArrayList<Suggestion> getSuggestionList() {
		//if (suggestionList==null)
			DBManager.getSuggestionlistFromDB();
		return suggestionList;
	}
	
	public void setSuggestionList(ArrayList<Suggestion> suggestionList) {
		this.suggestionList = suggestionList;
	}
	
	 public ArrayList<HashMap<String, Object>> getSuggestionListForDisplay(ArrayList<Suggestion> suggestionList){
		ArrayList<HashMap<String, Object>> resultList=new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		String content, suggestionTime;
		
		for(Suggestion suggestion:suggestionList){
			content= suggestion.getSuggestion();
			suggestionTime= suggestion.getSuggestionTime();			
			//String getDate=null;
			//if(suggestionTime.equals(TimeManager.getStrDateTime())){
			 // getDate="今天"+suggestionTime.substring(11, 16);
			//}
			//else{
			String getDate=StringPattern(suggestionTime.substring(0, 16), "yyyy-MM-dd HH:mm", "yyyy年MM月dd日 HH:mm");
			//}
			map=new HashMap<String, Object>();			
			map.put(MessageAdapter.ArrayKey_FirstLine, getDate);
			map.put(MessageAdapter.ArrayKey_SecondLine, content);
			resultList.add(map);			
		    }
				
		return resultList;
	}
	
	public int getUnreadSuggestion() {
		DBManager.getUnreadCountFromDB();
		return unreadSuggestion;
	}

	public void setUnreadSuggestion(int unreadSuggestion) {
		this.unreadSuggestion = unreadSuggestion;
	}
	
	public final String StringPattern(String date, String oldPattern, String newPattern) {   
        if (date == null || oldPattern == null || newPattern == null)   
            return "";   
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象    
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;        // 实例化模板对象    
        Date d = null ;    
        try{    
            d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来    
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理    
            e.printStackTrace() ;       // 打印异常信息    
        }    
        return sdf2.format(d);  
    }

	public void setSuggestionRead(int RecId) {
		// TODO Auto-generated method stub
		DBManager.setSelectedSuggestionRead(RecId);
	}

	public String getPushClientId() {
		
		return pushClientId;
	}

	public void setPushClientId(String pushClientId) {
		this.pushClientId = pushClientId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setSelectCalendar(Calendar startTime) {
		// TODO Auto-generated method stub
		this.selectCalendar = startTime;
	}   
	
	public Calendar getSelectCalendar(){
		return selectCalendar;
	}
	
}
