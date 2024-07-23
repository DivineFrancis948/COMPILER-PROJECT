package com.compiler.student.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="SELECTED_QUESTIONS")
public class SelectedQuestionEntity {
	
	@Id
    @Column(name="user_name",unique = true)
    private String userName;
	
//    @ElementCollection
    @Column(name="ques_list")
    private List<String> selectedQuestionId;
    
    @Column(name="mcq_ques_list")
    private List<String> selectedMcqQuestionId;
    
    @Column(name="mcq_total_mark")
    private String mcqMark;
    
    @Column(name="prg_total_mark")
    private String prgMark;
    
    @Column(name="tab_changed")
    private int tabChanged;
    
    @Column(name="timer")
    private int timer;
    

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getTabChanged() {
		return tabChanged;
	}

	public void setTabChanged(int tabChanged) {
		this.tabChanged = tabChanged;
	}

	public String getPrgMark() {
		return prgMark;
	}

	public void setPrgMark(String prgMark) {
		this.prgMark = prgMark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getSelectedQuestionId() {
		return selectedQuestionId;
	}

	public void setSelectedQuestionId(List<String> selectedQuestionId) {
		this.selectedQuestionId = selectedQuestionId;
	}

	public List<String> getSelectedMcqQuestionId() {
		return selectedMcqQuestionId;
	}

	public void setSelectedMcqQuestionId(List<String> selectedMcqQuestionId) {
		this.selectedMcqQuestionId = selectedMcqQuestionId;
	}

	public String getMcqMark() {
		return mcqMark;
	}

	public void setMcqMark(String mcqMark) {
		this.mcqMark = mcqMark;
	}
	
	
	
	
}
