package com.compiler.student.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.compiler.student.dto.AdminResponse;
import com.compiler.student.dto.Response;
import com.compiler.student.exceptions.SourceCodeNotFoundException;
import com.compiler.student.exceptions.TestCaseFailException;
import com.compiler.student.exceptions.TestCaseNullException;

@RestControllerAdvice
public class CustomExceptionHandler {
    
	@ExceptionHandler
	public ResponseEntity<?> testCaseFailException(TestCaseFailException e) {
		Response response = new Response();
		response.setCompileError(new StringBuilder(e.getMessage()));
		response.setPassed("NO");
		
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@ExceptionHandler
	public ResponseEntity<?> sourceCodeNotFoundException(SourceCodeNotFoundException e) {
		Response response = new Response();
		response.setCompileError(new StringBuilder(e.getMessage()));
		response.setPassed("NO");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ExceptionHandler
	public ResponseEntity<?> exception(Exception e) {
		Response response = new Response();
		response.setCompileError(new StringBuilder(e.getMessage()));
		response.setPassed("NO");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@ExceptionHandler
	public ResponseEntity<?> tesCaseNullException(TestCaseNullException e) {
		AdminResponse adminResponse = new AdminResponse();
		Response response = new  Response();
		response.setCompileError(new StringBuilder(e.getMessage()));
		adminResponse.setResponse(response);
		adminResponse.setExtractedFunction("Error Occured\nAborting");
		return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
	}
}
