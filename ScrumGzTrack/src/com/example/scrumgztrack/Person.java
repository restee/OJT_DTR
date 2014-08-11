package com.example.scrumgztrack;

public class Person {
	private String firstName;
	private String lastName;
	private String email;
	private String teamName;
	
	
	public Person() {
		super();
	}
	public Person(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	public Person(String firstName, String lastName, String email,String teamName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.teamName = teamName;
	}
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
