package com.compiler.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class QuestionDTO 
{
	@NotEmpty
    private String username;
	
	@NotNull
	@NotEmpty
    private String questiontype;
	
	@NotNull
	@NotEmpty
    private String question;

    private String questionid;
	
    private String questionHeading;
	
    private String status;
    

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuestionHeading() {
		return questionHeading;
	}

	public void setQuestionHeading(String questionHeading) {
		this.questionHeading = questionHeading;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
    
    
    

}
