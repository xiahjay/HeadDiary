package com.example.headdiary.data;

import android.R.integer;

public class Suggestion implements Cloneable{
	
	
	private int UserId, RecId;
	private String Suggestion, SuggestionTime;
	private int IfNew;
	
	public Suggestion() {
		// TODO Auto-generated constructor stub
		UserId=0;
		RecId=0;
		Suggestion="";
		SuggestionTime="";
		
		
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId() {
		UserId = UserDAO.getInstance().getUser().getUserId();
	}

	public int getRecId() {
		return RecId;
	}

	public void setRecId(int recId) {
		RecId = recId;
	}

	public String getSuggestion() {
		return Suggestion;
	}

	public void setSuggestion(String suggestion) {
		Suggestion = suggestion;
	}

	public String getSuggestionTime() {
		return SuggestionTime;
	}

	public void setSuggestionTime(String suggestionTime) {
		SuggestionTime = suggestionTime;
	}

	public int getIfNew() {
		return IfNew;
	}

	public void setIfNew(int ifNew) {
		IfNew = ifNew;
	}
	
	

	
	
}
