package com.compiler.student.service;

import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.util.MultiValueMap;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.Response;
import com.compiler.student.exceptions.RecordNotFoundException;
import com.compiler.student.exceptions.TestCaseFailException;

public interface CppCompilerService {
	
	
//	public List<JSONObject> checkUserCppProgram(String cppSourceCode, String id, String role);
	public Response checkUserCppProgram(EmbeddedidDTO id,UUID randomUUID) throws TestCaseFailException, Exception;
	Response checkCustomUserCppProgram(CustomTestDTO dto,UUID randomUUID) throws TestCaseFailException;
}
