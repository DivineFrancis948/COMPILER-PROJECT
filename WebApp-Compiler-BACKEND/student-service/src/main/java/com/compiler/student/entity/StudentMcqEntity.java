package com.compiler.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Student_mcq")
public class StudentMcqEntity
{
	@EmbeddedId
    private StudentMcqEmbedded studentMcqEmbedded;
	
	@Column(name="selected")
	private String selected;
	
	@Column(name="marks")
	private String marks;
	
	@Column(name="mcq_status")
    private String mcqStatus;
	
	public StudentMcqEmbedded getStudentMcqEmbedded() {
		return studentMcqEmbedded;
	}
	public void setStudentMcqEmbedded(StudentMcqEmbedded studentMcqEmbedded) {
		this.studentMcqEmbedded = studentMcqEmbedded;
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
	public StudentMcqEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}