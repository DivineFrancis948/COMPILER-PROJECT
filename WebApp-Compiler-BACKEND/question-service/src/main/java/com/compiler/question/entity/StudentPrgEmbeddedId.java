package com.compiler.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StudentPrgEmbeddedId 
{
    @Column(name="user_name",unique = true)
    private String username;
    
    @Column(name="question_id")
    private String questionid;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}
    
    

}
