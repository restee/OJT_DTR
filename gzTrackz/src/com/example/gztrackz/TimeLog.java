package com.example.gztrackz;

public class TimeLog {
	private String email;
	private String timeIn;
	private String timeOut;
	
	public TimeLog(){}
	
	public TimeLog(String email, String timeIn, String timeOut) {
		super();
		this.email = email;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	
	
}
