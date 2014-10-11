package com.example.headdiary.data;

public class Graphic {
	private Integer degree;
	private String date;
	private long interval;
	
	public String getDate() {
		return date;
	}

	public void setDate(String userDate) {
		this.date = userDate;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	
	public Graphic(Integer degree, String date,long interval) {
		super();
		this.date = date;
		this.degree = degree;
		this.interval=interval;
	}
	
	public long getInterval(){
		return interval;
	}
	
	public void setInterval(long interval){
		this.interval=interval;
	}

}
