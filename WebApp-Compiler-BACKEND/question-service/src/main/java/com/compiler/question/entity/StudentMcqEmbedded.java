package com.compiler.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StudentMcqEmbedded {
	@Column(name="questionId")
	private String questionId;
	
	@Column(name = "userName")
    private String userName;

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

	public StudentMcqEmbedded() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	
}
	
	
	
