package com.compiler.student.dto;

public class StudentPrgDto 
{

	
	private String questionid;
	
	private String solution;
	
	private String username;
	
	private int testCasePassed;
	
	private String programmingLanguage;
	
	private int totalmark;
	
	private String programname;
	 

	public String getProgramname() {
		return programname;
	}

	public void setProgramname(String programname) {
		this.programname = programname;
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

	public String getprogrammingLanguage() {
		return programmingLanguage;
	}

	public void setprogrammingLanguage(String programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}

	public int getTotalmark() {
		return totalmark;
	}

	public void setTotalmark(int totalmark) {
		this.totalmark = totalmark;
	}
	
}
