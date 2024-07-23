package com.compiler.auth.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ADMIN_SETTINGS")
public class AdminEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use GenerationType.IDENTITY for auto-incremented IDs
    private Long id;
    
	@Column(name="timer")
	private String timer;
	
    @Column(name="languages")
    private List<String> programmingLanguages;
    
    @Column(name="question_categories")
    private List<String> questionCategories;

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public List<String> getProgrammingLanguages() {
		return programmingLanguages;
	}

	public void setProgrammingLanguages(List<String> programmingLanguages) {
		this.programmingLanguages = programmingLanguages;
	}

	public List<String> getQuestionCategories() {
		return questionCategories;
	}

	public void setQuestionCategories(List<String> questionCategories) {
		this.questionCategories = questionCategories;
	}
    
    

}
