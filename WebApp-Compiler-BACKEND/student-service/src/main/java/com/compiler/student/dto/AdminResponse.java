package com.compiler.student.dto;

public class AdminResponse {
	
	
	private String extractedFunction;
	private Response response;
	public Response getResponse() {
		return response;
	}
	public String getExtractedFunction() {
		return extractedFunction;
	}
	public void setExtractedFunction(String string) {
		this.extractedFunction = string;
	}
	public void setResponse(Response response) {
		this.response = response;
	}


	

}
