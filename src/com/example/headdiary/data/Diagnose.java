package com.example.headdiary.data;

public class Diagnose {
	private String diagnose, suggestion, guidelines;
	
	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	
	public String getGuidelines() {
		return guidelines;
	}

	public void setGuidelines(String guidelines) {
		this.guidelines = guidelines;
	}
	public Diagnose(String diagnose, String suggestion,String guidelines) {
		super();
		this.diagnose = diagnose;
		this.suggestion = suggestion;
		this.guidelines = guidelines;
	}
	

}
