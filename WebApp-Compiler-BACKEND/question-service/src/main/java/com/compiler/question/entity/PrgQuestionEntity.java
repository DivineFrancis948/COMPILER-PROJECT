package com.compiler.question.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="PROGRAM_QUESTIONS")
public class PrgQuestionEntity 
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Id
    @Column(name = "question_id")
    private String questionId;

    @Column(name = "solution")
    private String solution;
    
    @Column(name = "basic_structure")
    private String basic;	
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "no_of_inputs")
    private String inputs;
    
    @Column(name = "test_case_1")
    private String testcase1;
    
    @Column(name = "test_case_1A")
    private String testcase1A;
    
    @Column(name = "test_case_2")
    private String testcase2;
    
    @Column(name = "test_case_2A")
    private String testcase2A;
    
    @Column(name = "test_case_3")
    private String testcase3;
    
    @Column(name = "test_case_3A")
    private String testcase3A;
    
    @Column(name = "test_case_4")
    private String testcase4;
    
    @Column(name = "test_case_4A")
    private String testcase4A;
    
    @Column(name = "test_case_5")
    private String testcase5;
    
    @Column(name = "test_case_5A")
    private String testcase5A;
    
    @Column(name = "test_case_6")
    private String testcase6;
    
    @Column(name = "test_case_6A")
    private String testcase6A;
    
    @Column(name = "test_case_7")
    private String testcase7;
    
    @Column(name = "test_case_7A")
    private String testcase7A;
    
    @Column(name = "test_case_8")
    private String testcase8;
    
    @Column(name = "test_case_8A")
    private String testcase8A;
    
    @Column(name = "test_case_9")
    private String testcase9;
    
    @Column(name = "test_case_9A")
    private String testcase9A;
    
    @Column(name = "test_case_10")
    private String testcase10;
    
    @Column(name = "test_case_10A")
    private String testcase10A;
    
    

	public String getTestcase9() {
		return testcase9;
	}

	public void setTestcase9(String testcase9) {
		this.testcase9 = testcase9;
	}

	public String getTestcase9A() {
		return testcase9A;
	}

	public void setTestcase9A(String testcase9a) {
		testcase9A = testcase9a;
	}

	public String getTestcase10() {
		return testcase10;
	}

	public void setTestcase10(String testcase10) {
		this.testcase10 = testcase10;
	}

	public String getTestcase10A() {
		return testcase10A;
	}

	public void setTestcase10A(String testcase10a) {
		testcase10A = testcase10a;
	}

	public String getTestcase8() {
		return testcase8;
	}

	public void setTestcase8(String testcase8) {
		this.testcase8 = testcase8;
	}

	public String getTestcase8A() {
		return testcase8A;
	}

	public void setTestcase8A(String testcase8a) {
		testcase8A = testcase8a;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getInputs() {
		return inputs;
	}

	public void setInputs(String inputs) {
		this.inputs = inputs;
	}

	public String getTestcase1() {
		return testcase1;
	}

	public void setTestcase1(String testcase1) {
		this.testcase1 = testcase1;
	}

	public String getTestcase1A() {
		return testcase1A;
	}

	public void setTestcase1A(String testcase1a) {
		testcase1A = testcase1a;
	}

	public String getTestcase2() {
		return testcase2;
	}

	public void setTestcase2(String testcase2) {
		this.testcase2 = testcase2;
	}

	public String getTestcase2A() {
		return testcase2A;
	}

	public void setTestcase2A(String testcase2a) {
		testcase2A = testcase2a;
	}

	public String getTestcase3() {
		return testcase3;
	}

	public void setTestcase3(String testcase3) {
		this.testcase3 = testcase3;
	}

	public String getTestcase3A() {
		return testcase3A;
	}

	public void setTestcase3A(String testcase3a) {
		testcase3A = testcase3a;
	}

	public String getTestcase4() {
		return testcase4;
	}

	public void setTestcase4(String testcase4) {
		this.testcase4 = testcase4;
	}

	public String getTestcase4A() {
		return testcase4A;
	}

	public void setTestcase4A(String testcase4a) {
		testcase4A = testcase4a;
	}

	public String getTestcase5() {
		return testcase5;
	}

	public void setTestcase5(String testcase5) {
		this.testcase5 = testcase5;
	}

	public String getTestcase5A() {
		return testcase5A;
	}

	public void setTestcase5A(String testcase5a) {
		testcase5A = testcase5a;
	}

	public String getTestcase6() {
		return testcase6;
	}

	public void setTestcase6(String testcase6) {
		this.testcase6 = testcase6;
	}

	public String getTestcase6A() {
		return testcase6A;
	}

	public void setTestcase6A(String testcase6a) {
		testcase6A = testcase6a;
	}

	public String getTestcase7() {
		return testcase7;
	}

	public void setTestcase7(String testcase7) {
		this.testcase7 = testcase7;
	}

	public String getTestcase7A() {
		return testcase7A;
	}

	public void setTestcase7A(String testcase7a) {
		testcase7A = testcase7a;
	}
    
    
	
}
