package com.compiler.question.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compiler.question.dto.AdminExecutionRequestDto;
import com.compiler.question.dto.AdminResponse;
import com.compiler.question.exception.SourceCodeNotFoundException;
import com.compiler.question.exception.TestCaseFailException;
import com.compiler.question.exception.TestCaseNullException;
import com.compiler.question.service.AdminCCompilerService;
import com.compiler.question.service.AdminCppCompilerService;
import com.compiler.question.service.AdminJavaCompilerService;

@RestController
@RequestMapping("admin")
public class AdminController {
	
	
	@Autowired
	AdminCppCompilerService adminCpp;
	@Autowired
	AdminCCompilerService adminC;
	@Autowired
	AdminJavaCompilerService adminJava;
	
    @PostMapping("/execute")
    public AdminResponse executeAdminCppProgram(@RequestBody AdminExecutionRequestDto request) throws TestCaseFailException, Exception {
    	try {
    		if (request.getTestCases() == null) {
			    throw new TestCaseNullException("Test cases cannot be null in controller");
			}
    		if (request.getLanguage() == null) {
			    throw new NullPointerException("Language cannot be null ");
			}
    		if(request.getSourceCode() == null) {
    			throw new SourceCodeNotFoundException("Source Code is null");
    		}
    		
    		switch(request.getLanguage())
    		{
    			
    			case "cpp" : {
    				
//    				System.out.println("->" + request.getTestCases().getTestCase1());
    				UUID randomUUID = UUID.randomUUID();  // for future debugging
    				return adminCpp.checkAdminCppProgram( request.getSourceCode(),
    													  request.getTestCases(),randomUUID
    													);
    			}

    			case "c" : {
    				
//    				System.out.println("->" + request.getTestCases().getTestCase1());
    				UUID randomUUID = UUID.randomUUID();  // for future debugging
    				return adminC.checkAdminCProgram( request.getSourceCode(),
    													  request.getTestCases(),randomUUID
    													);
    			}

    			case "java" : {
    				
//    				System.out.println("->" + request.getTestCases().getTestCase1());
    				UUID randomUUID = UUID.randomUUID();  // for future debugging
    				return adminJava.checkAdminJavaProgram( request.getSourceCode(),
    													  request.getTestCases(),randomUUID
    													);
    			}
    			
    		}
    		
    	}catch (TestCaseNullException e) {
			throw e;
		} catch (TestCaseFailException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return null;
    }


}
