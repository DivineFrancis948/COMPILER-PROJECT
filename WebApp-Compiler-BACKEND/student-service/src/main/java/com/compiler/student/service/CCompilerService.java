package com.compiler.student.service;

import java.util.UUID;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.Response;
import com.compiler.student.exceptions.TestCaseFailException;

public interface CCompilerService {
	
//	public Response checkUserCProgram(EmbeddedidDTO id);
//	
//	public Response customUserCProgram(CustomTestDTO dto);
	
	public Response checkUserCProgram(EmbeddedidDTO id,UUID randomUUID) throws TestCaseFailException, Exception;
	Response checkCustomUserCProgram(CustomTestDTO dto,UUID randomUUID) throws TestCaseFailException;

}
