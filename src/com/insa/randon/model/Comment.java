package com.insa.randon.model;

public class Comment {
	String title;
	String content;
	String date; 

	public Comment(String title, String content, String date){
		this.title=title;
		this.content = content;
		this.date=date;	
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getDate() {
		return this.date;
	}
}
