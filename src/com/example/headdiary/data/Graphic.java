package com.example.headdiary.data;

public class Graphic {
	private Integer degree;
	private String date;
	
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
	
	public Graphic(Integer degree, String date) {
		super();
		this.date = date;
		this.degree = degree;
	}

}
