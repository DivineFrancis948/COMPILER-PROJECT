package com.compiler.jdoodle.dto;

public class StudentPrgDto {

	
	private String questionid;
	
	private String solution;
	
	private String username;
	
	private int testCasePassed;
	
	private String language;
	
	private int totalmark;

	private String input;
	
	

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTestCasePassed() {
		return testCasePassed;
	}

	public void setTestCasePassed(int testCasePassed) {
		this.testCasePassed = testCasePassed;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getTotalmark() {
		return totalmark;
	}

	public void setTotalmark(int totalmark) {
		this.totalmark = totalmark;
	}
	
	
	
	
}
