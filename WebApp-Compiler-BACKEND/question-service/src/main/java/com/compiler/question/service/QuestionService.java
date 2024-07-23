package com.compiler.question.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.json.simple.JSONObject;
import org.springframework.util.MultiValueMap;

import com.compiler.question.dto.McqQuestionDTO;
import com.compiler.question.dto.PrgQuestionDTO;
import com.compiler.question.dto.QuestionDTO;
import com.compiler.question.dto.ServiceResponse;

public interface QuestionService
{
	public ServiceResponse addQuestionDetails(QuestionDTO quesdto);
	public ServiceResponse addPRGQuestionDetails(PrgQuestionDTO prgquesdto);
	public ServiceResponse addMCQQuestionDetails(McqQuestionDTO mcqquesdto);
	public JSONObject searchfor();
	public JSONObject getAnyfiveQuestions(String userName);
	public QuestionDTO getMcqWithoutOption(String questionId);
	public JSONObject getRandomMcqQuestion(String studentId);
	public McqQuestionDTO getMcqFullWithOptions(String questionId);
	public JSONObject getallprgQuestions(String userName);
	public JSONObject getTestCases(String questionid);
	
	public ServiceResponse updateQuestionDetails(QuestionDTO quesdto);
	public ServiceResponse updateMCQQuestionDetails(McqQuestionDTO mcqquesdto);
	public ServiceResponse updatePRGQuestionDetails(PrgQuestionDTO prgQuestionDTO);
	public PrgQuestionDTO getPRGQuestionDetals(String questionId);
	public QuestionDTO getQusetionWithStatus(String questionId);
	public ServiceResponse verifyQuestion(String questionId);
	public ServiceResponse deleteQuestion(String questionId);
	public McqQuestionDTO getMcqFullWithOptionsAndAnswer(String questionId);

	public ServiceResponse processSheetMcq(Sheet sheet, String userName);
	public ServiceResponse processSheetPrg(Sheet sheet, String userName);

	public ServiceResponse updateMCQAttended(String userName);
	public ServiceResponse updatePRGAttended(String userName);
	public ServiceResponse updateTabSwitching(String userName);







}
