package com.compiler.auth.dto;

public class JwtResponseDTO 
{
	private String token;
    private String username;
    private String role;
    private String ismcq;
    private int timer;
    private String isSubmitted;
    
    
	public String getIsSubmitted() {
		return isSubmitted;
	}
	public void setIsSubmitted(String isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public 	String getIsmcq() {
		return ismcq;
	}
	public void setIsmcq(String ismcq) {
		this.ismcq = ismcq;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    

}
