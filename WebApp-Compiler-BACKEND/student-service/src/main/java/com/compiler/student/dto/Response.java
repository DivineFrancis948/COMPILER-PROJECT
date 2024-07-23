package com.compiler.student.dto;

import java.util.List;

import org.json.simple.JSONObject;

public class Response 
{
	private int output;
	private JSONObject result;
	private String passed;
	StringBuilder compileError;
	

	public StringBuilder getCompileError() {
		return compileError;
	}

	public void setCompileError(StringBuilder compileError) {
		this.compileError = compileError;
	}

	

	

	public String getPassed() {
		return passed;
	}

	public void setPassed(String passed) {
		this.passed = passed;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

	
	

}
