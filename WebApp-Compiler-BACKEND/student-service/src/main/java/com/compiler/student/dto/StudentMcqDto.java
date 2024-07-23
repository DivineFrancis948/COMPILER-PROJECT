package com.compiler.student.dto;


public class StudentMcqDto {
	
	private String questionId;
	
    private String userName;
	
	private String selected;
	
	private String marks;
	
	private String mcqStatus;
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	
	
	public String getMcqStatus() {
		return mcqStatus;
	}
	public void setMcqStatus(String mcqStatus) {
		this.mcqStatus = mcqStatus;
	}
	public StudentMcqDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}




