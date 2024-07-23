package com.compiler.student.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "compiler")
public class ExecutionRequest {
	
	
		@Id
		@Column(name="id")
		private String id;
		
		@Column(name="role")
		private String role;
		
		@Column(name="code")
		private String cppSourceCode;
		
		@Column(name="type")
	    private String type;
		
		@Column(name = "structure")
		private String structure;
		
		
		@Column(name = "testcase1")
		private String testCase1;

	    @Column(name = "testcase2")
	    private String testCase2;

	    @Column(name = "testcase3")
	    private String testCase3;

	    @Column(name = "testcase4")
	    private String testCase4;

	    @Column(name = "testcase5")
	    private String testCase5;

	    @Column(name = "testcase6")
	    private String testCase6;

	    @Column(name = "testcase7")
	    private String testCase7;

	    @Column(name = "testcase8")
	    private String testCase8;

	    @Column(name = "testcase9")
	    private String testCase9;

	    @Column(name = "testcase10")
	    private String testCase10;
	    
	    @Column(name = "output1")
	    private String output1;

	    @Column(name = "output2")
	    private String output2;

	    @Column(name = "output3")
	    private String output3;

	    @Column(name = "output4")
	    private String output4;

	    @Column(name = "output5")
	    private String output5;

	    @Column(name = "output6")
	    private String output6;

	    @Column(name = "output7")
	    private String output7;

	    @Column(name = "output8")
	    private String output8;

	    @Column(name = "output9")
	    private String output9;

	    @Column(name = "output10")
	    private String output10;
	    public String getTestCase1() {
			return testCase1;
		}
		public void setTestCase1(String testCase1) {
			this.testCase1 = testCase1;
		}
		public String getTestCase2() {
			return testCase2;
		}
		public void setTestCase2(String testCase2) {
			this.testCase2 = testCase2;
		}
		public String getTestCase3() {
			return testCase3;
		}
		public void setTestCase3(String testCase3) {
			this.testCase3 = testCase3;
		}
		public String getTestCase4() {
			return testCase4;
		}
		public void setTestCase4(String testCase4) {
			this.testCase4 = testCase4;
		}
		public String getTestCase5() {
			return testCase5;
		}
		public void setTestCase5(String testCase5) {
			this.testCase5 = testCase5;
		}
		public String getTestCase6() {
			return testCase6;
		}
		public void setTestCase6(String testCase6) {
			this.testCase6 = testCase6;
		}
		public String getTestCase7() {
			return testCase7;
		}
		public void setTestCase7(String testCase7) {
			this.testCase7 = testCase7;
		}
		public String getTestCase8() {
			return testCase8;
		}
		public void setTestCase8(String testCase8) {
			this.testCase8 = testCase8;
		}
		public String getTestCase9() {
			return testCase9;
		}
		public void setTestCase9(String testCase9) {
			this.testCase9 = testCase9;
		}
		public String getTestCase10() {
			return testCase10;
		}
		public void setTestCase10(String testCase10) {
			this.testCase10 = testCase10;
		}
		public String getOutput1() {
			return output1;
		}
		public void setOutput1(String output1) {
			this.output1 = output1;
		}
		public String getOutput2() {
			return output2;
		}
		public void setOutput2(String output2) {
			this.output2 = output2;
		}
		public String getOutput3() {
			return output3;
		}
		public void setOutput3(String output3) {
			this.output3 = output3;
		}
		public String getOutput4() {
			return output4;
		}
		public void setOutput4(String output4) {
			this.output4 = output4;
		}
		public String getOutput5() {
			return output5;
		}
		public void setOutput5(String output5) {
			this.output5 = output5;
		}
		public String getOutput6() {
			return output6;
		}
		public void setOutput6(String output6) {
			this.output6 = output6;
		}
		public String getOutput7() {
			return output7;
		}
		public void setOutput7(String output7) {
			this.output7 = output7;
		}
		public String getOutput8() {
			return output8;
		}
		public void setOutput8(String output8) {
			this.output8 = output8;
		}
		public String getOutput9() {
			return output9;
		}
		public void setOutput9(String output9) {
			this.output9 = output9;
		}
		public String getOutput10() {
			return output10;
		}
		public void setOutput10(String output10) {
			this.output10 = output10;
		}
		
    
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCppSourceCode() {
			return cppSourceCode;
		}
		public void setCppSourceCode(String cppSourceCode) {
			this.cppSourceCode = cppSourceCode;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getStructure() {
			return structure;
		}
		public void setStructure(String structure) {
			this.structure = structure;
		}
		
		
	
		
	
}
