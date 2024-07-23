package com.compiler.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="student_program")
public class StudentPrgEntity 
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
    
    @EmbeddedId
    StudentPrgEmbeddedId embeddedid;

    
    
    @Column(name="solution")
    private String solution;
    
    @Column(name="test_case_passed")
    private int testCasePassed;
    
    @Column(name="language")
    private String language;
    
    @Column(name="total_mark")
    private int totalmark;
    
    @Column(name="program_name")
    private String programname;
     
    //ATTENDED , SOLVED, SUBMITTED
    @Column(name="status")
    private String status;
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProgramname() {
		return programname;
	}

	public void setProgramname(String programname) {
		this.programname = programname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public StudentPrgEmbeddedId getEmbeddedid() {
		return embeddedid;
	}

	public void setEmbeddedid(StudentPrgEmbeddedId embeddedid) {
		this.embeddedid = embeddedid;
	}

	public int getTestCasePassed() {
		return testCasePassed;
	}

	public void setTestCasePassed(int testCasePassed) {
		this.testCasePassed = testCasePassed;
	}

	public int getTotalmark() {
		return totalmark;
	}

	public void setTotalmark(int totalmark) {
		this.totalmark = totalmark;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
    
	
    
    
    

}
