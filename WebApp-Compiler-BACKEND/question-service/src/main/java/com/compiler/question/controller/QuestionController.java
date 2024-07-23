package com.compiler.question.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compiler.question.dto.FileUploadDto;
import com.compiler.question.dto.McqQuestionDTO;
import com.compiler.question.dto.PrgQuestionDTO;
import com.compiler.question.dto.QuestionDTO;
import com.compiler.question.dto.ServiceResponse;
import com.compiler.question.service.QuestionService;
import com.compiler.question.util.Constants;
import com.compiler.question.util.FeignServiceUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("question")
public class QuestionController 
{
	@Autowired
	FeignServiceUtil util1;
	
	@Autowired
	QuestionService queservice;
	
	private static Logger logger = LogManager.getLogger(QuestionController.class);
	
	@PostMapping("/add")
	ResponseEntity<?> addQuestionDetails(@Valid @RequestBody QuestionDTO quesdto)
	{
		return new ResponseEntity(queservice.addQuestionDetails(quesdto),HttpStatus.OK);
	}
	
	@PostMapping("/add/mcq")
	ResponseEntity<?> addMCQQuestionDetails(@Valid @RequestBody McqQuestionDTO mcqquesdto)
	{
		return new ResponseEntity(queservice.addMCQQuestionDetails(mcqquesdto),HttpStatus.OK);
	}
		
	@PostMapping("/add/prg")
	ResponseEntity<?> addPRGQuestionDetails(@Valid @RequestBody PrgQuestionDTO prgquesdto)
	{
		return new ResponseEntity(queservice.addPRGQuestionDetails(prgquesdto),HttpStatus.OK);
	}
	
	@GetMapping("/getfivequestions/{userName}")
	ResponseEntity<?> getfiveallDetails(@PathVariable("userName") String userName)
	{
		return new ResponseEntity(queservice.getAnyfiveQuestions(userName),HttpStatus.OK);
	}
	
	@GetMapping("/getallquestions/{userName}")
	ResponseEntity<?> getallquestions(@PathVariable("userName") String userName)
	{
		return new ResponseEntity(queservice.getallprgQuestions(userName),HttpStatus.OK);
	}
	
	@GetMapping("/demo")
	String demo()
	{
		return util1.demofeignservice();
	}
	
	@GetMapping("/getMcqWithoutOption/{questionId}")
	ResponseEntity<?> getMcqWithoutOption(@PathVariable("questionId") String questionId)
	{
		return new ResponseEntity(queservice.getMcqWithoutOption(questionId),HttpStatus.OK);
	}
	
	//Make this studentid to username
	@GetMapping("/getRandomMcqQuestion/{studentId}")
	ResponseEntity<?> getRandomMcqQuestion(@PathVariable("studentId") String studentId)
	{
//		System.out.println("llllllllllllllll");
		return new ResponseEntity(queservice.getRandomMcqQuestion(studentId),HttpStatus.OK);
	}
	
	@GetMapping("/getMcqFullWithOptions/{questionId}")
	ResponseEntity<?> getMcqFullWithOptions(@PathVariable("questionId") String questionId)
	{
		return new ResponseEntity(queservice.getMcqFullWithOptions(questionId),HttpStatus.OK);
	}
	
	@GetMapping("/gettestcases/{questionId}")
	ResponseEntity<?> getTesCases(@PathVariable("questionId") String questionId)
	{
		return new ResponseEntity(queservice.getTestCases(questionId),HttpStatus.OK);
	}
	
	//updating and verifying mcq question and delete
	
		//getting question with status
		@GetMapping("/getQusetionWithStatus/{questionId}")
		ResponseEntity<?> getQusetionWithStatus(@PathVariable("questionId") String questionId)
		{
			return new ResponseEntity(queservice.getQusetionWithStatus(questionId),HttpStatus.OK);
		}
		
		
		//update Question(mcq & programming)
		@PostMapping("/update/question")
		ResponseEntity<?> updateQuestionDetails(@RequestBody QuestionDTO quesdto)
		{
			return new ResponseEntity(queservice.updateQuestionDetails(quesdto),HttpStatus.OK);
		}
		
		//update option and answer of mcq
		@PostMapping("/update/mcq")
		ResponseEntity<?> updateMCQQuestionDetails(@RequestBody McqQuestionDTO mcqquesdto)
		{
			return new ResponseEntity(queservice.updateMCQQuestionDetails(mcqquesdto),HttpStatus.OK);
		}
		
		//getting PRG details to update by admin
		@GetMapping("/getPRGQuestionDetals/{questionId}")
		ResponseEntity<?> getPRGQuestionDetals(@PathVariable("questionId") String questionId)
		{
			return new ResponseEntity(queservice.getPRGQuestionDetals(questionId),HttpStatus.OK);
		}
		
		//update option and answer of programming
			@PostMapping("/update/prg")
			ResponseEntity<?> updatePRGQuestionDetails(@RequestBody PrgQuestionDTO prgQuestionDTO)
			{
				return new ResponseEntity(queservice.updatePRGQuestionDetails(prgQuestionDTO),HttpStatus.OK);
			}
		
		//verifying question
			@GetMapping("/verifyQuestion/{questionId}")
			ResponseEntity<?> verifyQuestion(@PathVariable("questionId") String questionId)
			{
				return new ResponseEntity(queservice.verifyQuestion(questionId),HttpStatus.OK);
			}
		
		//delete the question
			@GetMapping("/deleteQuestion/{questionId}")
			ResponseEntity<?> deleteQuestion(@PathVariable("questionId") String questionId)
			{
				return new ResponseEntity(queservice.deleteQuestion(questionId),HttpStatus.OK);
			}
			
			@GetMapping("/getMcqFullWithOptionsAndAnswer/{questionId}")
			ResponseEntity<?> getMcqFullWithOptionsAndAnswer(@PathVariable("questionId") String questionId)
			{
				return new ResponseEntity(queservice.getMcqFullWithOptionsAndAnswer(questionId),HttpStatus.OK);
			}
			

			
			//add mcq questions using excel
			@PostMapping(value = "/createExcelMcq", consumes = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<ServiceResponse> saveExcelMcq(@RequestBody FileUploadDto fileUploadDto) throws IOException {
				try {
					InputStream inputStream = new ByteArrayInputStream(fileUploadDto.getFileData());
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					ServiceResponse response = queservice.processSheetMcq(sheet,fileUploadDto.getUserName());
					return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
				}catch(Exception e) {
					logger.error("Error:" + e.getMessage(), e);
					return new ResponseEntity<>(new ServiceResponse("FAILED", "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			//add prg questions using excel
			@PostMapping(value = "/createExcelPrg", consumes = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<ServiceResponse> saveExcelPrg(@RequestBody FileUploadDto fileUploadDto) throws IOException {
				try {
					InputStream inputStream = new ByteArrayInputStream(fileUploadDto.getFileData());
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					ServiceResponse response = queservice.processSheetPrg(sheet,fileUploadDto.getUserName());
					return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
				}catch(Exception e) {
					logger.error("Error:" + e.getMessage(), e);
					return new ResponseEntity<>(new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, Constants.USERLOG.INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			//UPDATE MCQ ATTENDE STATUS
			@GetMapping("/updatemcqattendedstatus/{userName}")
			public ResponseEntity<?> updateMCQAttendedtatus(@PathVariable("userName") String userName ) {
				return new ResponseEntity<>(queservice.updateMCQAttended(userName),new HttpHeaders(),HttpStatus.OK);
			}
			
			//UPDATE PRG ATTENDED STATUS
			@GetMapping("/updateprgattendedstatus/{userName}")
			public ResponseEntity<?> updatePRGAttendedtatus(@PathVariable("userName") String userName ) {
				return new ResponseEntity<>(queservice.updatePRGAttended(userName),new HttpHeaders(),HttpStatus.OK);
			}
			
			//UPDATE TAB SWITCHING STATUS
			@GetMapping("/updateTabSwitchingStatus/{userName}")
			public ResponseEntity<?> updateTabSwitching(@PathVariable("userName") String userName ) {
				return new ResponseEntity<>(queservice.updateTabSwitching(userName),new HttpHeaders(),HttpStatus.OK);

			}
	
	
}
