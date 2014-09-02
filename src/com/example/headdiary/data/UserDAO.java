package com.example.headdiary.data;

import java.util.Calendar;
import java.util.Date;

import javax.security.auth.PrivateCredentialPermission;

import android.R.string;

public class UserDAO {
	private static UserDAO mInstance;
	private int timePeriod,documentStyle,analysisStyle,graphicStyle,language,listStyle,monthStyle;
	private User user,loginUser,registerUser;
	private Boolean loginFromWeb;
	private String selectMonth;
	
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

	
	
}
