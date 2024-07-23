package com.compiler.student.service;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.Response;
import com.compiler.student.dto.ServiceResponse;
import com.compiler.student.dto.StudentMcqDto;
import com.compiler.student.dto.StudentPrgDto;
import com.compiler.student.dto.StudentTimerDTO;

public interface StudentService {
	
	public ServiceResponse addPRGSolution(StudentPrgDto studentPrgDto);
	public Response runTestCase(EmbeddedidDTO id) throws IOException, InterruptedException;
	public Response runProgram(CustomTestDTO id) throws IOException, InterruptedException;
	
	public ServiceResponse createAnswers(StudentMcqDto dto);
	public StudentMcqDto getSelectedMcqAnswer(String userName, String questionId);
	public ServiceResponse updateMark(String userName);
	public Response runJavaProgram(CustomTestDTO id) throws IOException, InterruptedException;
	public Response runCProgram(CustomTestDTO id) throws IOException, InterruptedException;
	public Response runCppProgram(CustomTestDTO id) throws IOException, InterruptedException;
	public Response runNoProgram() throws IOException, InterruptedException;
	public ServiceResponse submitPrgQuestions(EmbeddedidDTO id);
	public ServiceResponse getUserSavedAnswer(EmbeddedidDTO id);
	public JSONObject getMcqStatus(String userName);
	
	public ServiceResponse attendedPrgQuestionStatus(String userName);
	public ServiceResponse updateUserTimer(StudentTimerDTO  timerdto);









}
