package com.compiler.student.controller;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.ServiceResponse;
import com.compiler.student.dto.StudentMcqDto;
import com.compiler.student.dto.StudentPrgDto;
import com.compiler.student.dto.StudentTimerDTO;
import com.compiler.student.exceptions.SourceCodeNotFoundException;
import com.compiler.student.exceptions.TestCaseFailException;
import com.compiler.student.service.CCompilerService;
import com.compiler.student.service.CppCompilerService;
import com.compiler.student.service.JavaCompilerService;
import com.compiler.student.service.StudentService;
import com.compiler.student.util.Constants;


@RestController
@RequestMapping("student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	JavaCompilerService java;
	
	@Autowired
	CppCompilerService cpp;
	
	@Autowired
	CCompilerService c;
	
	private static Logger logger = LogManager.getLogger(StudentController.class);

	
	@PostMapping("/add/prgSol")
	ResponseEntity<?> addPRGSolution(@RequestBody StudentPrgDto studentPrgDto)
	
	{
//		System.out.println(studentPrgDto.getQuestionid());
		return new ResponseEntity(studentService.addPRGSolution(studentPrgDto),HttpStatus.OK);
	}
	
	@PostMapping("/runtestcase")
	ResponseEntity<?> runTestCase(@RequestBody EmbeddedidDTO id) throws Exception
	{
		
		
			try {
				UUID randomUUID = UUID.randomUUID();
				
				if (id.getQuestionid()==null || id.getUsername()==null || id.getProgrammingLanguage() == null || id.getProgrammingLanguage().isBlank()||id.getQuestionid().isBlank()|| id.getUsername().isBlank()) {
					throw new NullPointerException(Constants.EXCEPTIONS.RUNMISSING);
					}
switch (id.getProgrammingLanguage())
{
				case "java":
				    return new ResponseEntity(java.checkUserJavaProgram(id,randomUUID), HttpStatus.OK);
				case "cpp":
				    return new ResponseEntity(cpp.checkUserCppProgram(id,randomUUID), HttpStatus.OK);
				case "c":
				    return new ResponseEntity(c.checkUserCProgram(id,randomUUID), HttpStatus.OK);
				default:
				    return new ResponseEntity(studentService.runNoProgram(), HttpStatus.OK);
}
			} catch (TestCaseFailException e) {
				logger.error(Constants.EXCEPTIONS.TLE_OCCURED,e.getMessage());
				throw e;
			} catch (IOException e) {
				logger.error(Constants.EXCEPTIONS.IOEXCEPTION,e.getMessage());
				throw e;
			} catch (InterruptedException e) {
				logger.error(Constants.EXCEPTIONS.INTERUPPT,e.getMessage());
				throw e;
			}catch (SourceCodeNotFoundException e) {
				logger.error("SCNF "+e.getMessage());
				throw e;
			}
			catch (Exception e) {
				logger.error(Constants.EXCEPTIONS.EXCEPTION,e.getMessage());
				throw e;
			}
	}
	
	@PostMapping("/run")
	ResponseEntity<?> runProgram(@RequestBody CustomTestDTO id) throws IOException, InterruptedException, TestCaseFailException
	{
		try {
			
				if (id.getLanguage()==null || id.getInput()==null || id.getInput().isBlank()) {
					throw new NullPointerException(Constants.EXCEPTIONS.RUNMISSING);
					}
					else if( id.getLanguage().isBlank() || id.getProgram().isBlank()
							|| id.getProgramname().isBlank()) {
						throw new NullPointerException(Constants.EXCEPTIONS.RUNMISSING);
//						return new ResponseEntity(studentService.runNoProgram(),HttpStatus.OK);
					}
				
			if(id.getLanguage().equals("c"))
			{
				UUID randomUUID = UUID.randomUUID();
				//System.out.println("run controller triggered");
				return new ResponseEntity(c.checkCustomUserCProgram(id,randomUUID),HttpStatus.OK);
			}
			if(id.getLanguage().equals("java"))
			{
				UUID randomUUID = UUID.randomUUID();
				return new ResponseEntity(java.checkCustomUserJavaProgram(id,randomUUID),HttpStatus.OK);
			}
			if(id.getLanguage().equals("cpp"))
			{
				UUID randomUUID = UUID.randomUUID();
				return new ResponseEntity(cpp.checkCustomUserCppProgram(id,randomUUID),HttpStatus.OK);
			}
			else
			{	
				logger.info("Unexpected entry");//call admin report and logout immediately
				return new ResponseEntity(studentService.runNoProgram(),HttpStatus.OK);
			}
			
		}catch (NullPointerException e) {
			logger.error("NPE: "+e.getMessage());
			throw e;
		}
		catch (TestCaseFailException e) {
			logger.error(Constants.EXCEPTIONS.TLE_OCCURED,e.getMessage());
			throw e;
		} catch (IOException e) {
			logger.error(Constants.EXCEPTIONS.IOEXCEPTION,e.getMessage());
			throw e;
		} catch (InterruptedException e) {
			logger.error(Constants.EXCEPTIONS.INTERUPPT,e.getMessage());
			throw e;
		}catch (Exception e) {
			logger.error(Constants.EXCEPTIONS.EXCEPTION,e.getMessage(),e);
			throw e;
		}
	}
	
	
	
	@PostMapping("/submitprgquestions")
	public ResponseEntity<?> submitProgramQuestions(@RequestBody EmbeddedidDTO id) {
		return new ResponseEntity<>(studentService.submitPrgQuestions(id),new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/getsavedanswer")
	public ResponseEntity<?> getSavedAnswer(@RequestBody EmbeddedidDTO id) {
		return new ResponseEntity<>(studentService.getUserSavedAnswer(id),new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/answerCreation")
	public ResponseEntity<?> createAnswers(@RequestBody StudentMcqDto dto) {
		return new ResponseEntity<>(studentService.createAnswers(dto),new HttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/getSelectedMcqAnswer/{userName}/{questionId}")
	public ResponseEntity<?> getSelected(@PathVariable("userName") String userName , @PathVariable("questionId") String questionId) {
		return new ResponseEntity<>(studentService.getSelectedMcqAnswer(userName,questionId),new HttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/updatemark/{userName}")
	public ResponseEntity<ServiceResponse> updateMark(@PathVariable("userName") String userName) {
		return new ResponseEntity<>(studentService.updateMark(userName),new HttpHeaders(),HttpStatus.OK);
	}

	@GetMapping("/getMcqStatus/{userName}")
	public ResponseEntity<?> getMcqStatus(@PathVariable("userName") String userName ) {
		System.err.println("In to get stattus");
		return new ResponseEntity<>(studentService.getMcqStatus(userName),new HttpHeaders(),HttpStatus.OK);
	}
	@GetMapping("/getprgquestionattended/{userName}")
	public ResponseEntity<?> getPrgQuestionAttended(@PathVariable("userName") String userName ) {
		return new ResponseEntity<>(studentService.attendedPrgQuestionStatus(userName),new HttpHeaders(),HttpStatus.OK);
	}
	
	@PutMapping("/updatetimer")
	public ResponseEntity<?> updateTimer(@RequestBody StudentTimerDTO  timerdto) {
		return new ResponseEntity<>(studentService.updateUserTimer(timerdto),new HttpHeaders(),HttpStatus.OK);
	}

}
