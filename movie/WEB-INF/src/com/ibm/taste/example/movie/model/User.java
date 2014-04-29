package com.ibm.taste.example.movie.model;

public class User {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	
	private int id;
	private String name;
	private String email;
	public User(){
		
	}
	public User(int id, String name, String email){
		this.id = id;
		this.name = name;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String toJSON(){
		StringBuilder sb = new StringBuilder();
		sb.append("{'" + ID + "':'" + id + "', ");
		sb.append("'" + NAME + "':'" + name + "', ");
		sb.append("'" + EMAIL + "':'" + email + "'}");
		return sb.toString();
	}
}
