package com.compiler.student.service;

import java.util.UUID;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.Response;
import com.compiler.student.exceptions.TestCaseFailException;

public interface JavaCompilerService {
	
	public Response checkUserJavaProgram(EmbeddedidDTO id,UUID randomUUID) throws TestCaseFailException, Exception;
	Response checkCustomUserJavaProgram(CustomTestDTO dto,UUID randomUUID) throws TestCaseFailException;
	
}
