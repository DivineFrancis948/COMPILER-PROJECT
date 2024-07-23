package com.compiler.question.service;

import java.util.UUID;

import com.compiler.question.dto.AdminResponse;
import com.compiler.question.dto.TestCasesDto;
import com.compiler.question.exception.TestCaseFailException;
import com.compiler.question.exception.TestCaseNullException;


public interface AdminJavaCompilerService {

	AdminResponse checkAdminJavaProgram(String sourceCode, TestCasesDto testCases, UUID randomUUID)
			throws TestCaseFailException, Exception, TestCaseNullException;

}
