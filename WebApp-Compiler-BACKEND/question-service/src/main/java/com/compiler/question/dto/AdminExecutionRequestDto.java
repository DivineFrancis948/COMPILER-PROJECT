package com.compiler.question.dto;

public class AdminExecutionRequestDto {

	
	private String sourceCode;
	private TestCasesDto testCases;
	private String language;
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public TestCasesDto getTestCases() {
		return testCases;
	}
	public void setTestCases(TestCasesDto testCases) {
		this.testCases = testCases;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	

	
	
}
