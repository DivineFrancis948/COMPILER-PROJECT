package com.compiler.student.serviceImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.Response;
import com.compiler.student.dto.ServiceResponse;
import com.compiler.student.dto.StudentMcqDto;
import com.compiler.student.dto.StudentPrgDto;
import com.compiler.student.dto.StudentTimerDTO;
import com.compiler.student.entity.McqQuestionEntity;
import com.compiler.student.entity.PrgQuestionEntity;
import com.compiler.student.entity.QuestionEntity;
import com.compiler.student.entity.RegistrationEntity;
import com.compiler.student.entity.SelectedQuestionEntity;
import com.compiler.student.entity.StudentMcqEmbedded;
import com.compiler.student.entity.StudentMcqEntity;
import com.compiler.student.entity.StudentPrgEmbeddedId;
import com.compiler.student.entity.StudentPrgEntity;
import com.compiler.student.repository.McqQuestionRepository;
import com.compiler.student.repository.PrgQuestionRepository;
import com.compiler.student.repository.QuestionRepository;
import com.compiler.student.repository.RegistrationRepository;
import com.compiler.student.repository.SelectedQuestionRepository;
import com.compiler.student.repository.StudentMcqRepository;
import com.compiler.student.repository.StudentPrgRepository;
import com.compiler.student.service.StudentService;
import com.compiler.student.util.Constants;

@Service
public class StudentServiceImp implements StudentService{
	
	private static Logger logger = LogManager.getLogger(StudentServiceImp.class);
	  private final List<JSONObject> summary = new ArrayList<>(); 
	  
	@Autowired
	StudentPrgRepository studentPrgRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	PrgQuestionRepository prgquestion;
	
	@Autowired
	StudentMcqRepository studentMcqRepository;
	
	@Autowired
	McqQuestionRepository mcqQuestionRepository;
	
	@Autowired
	SelectedQuestionRepository selectedQuestionRepository;
	
	@Autowired
	RegistrationRepository registrationRepo;
	

	JSONObject result = new JSONObject();
	
	//SAVE THE USER CODE STUDENT PROGRAM CODE*************************************************************
	public ServiceResponse addPRGSolution(StudentPrgDto studentPrgDto)
	{
		Optional<QuestionEntity> num=questionRepository.findById(studentPrgDto.getQuestionid());
		try
		{
//			 && questionRepository.existsByUsername(studentPrgDto.getUsername())
			if(num.isPresent())
			{   
//				System.out.println(studentPrgDto.getQuestionid());
//			    System.out.println(studentPrgDto.getUsername());
				StudentPrgEntity studentPrgEntity=new StudentPrgEntity();
				StudentPrgEmbeddedId id=new StudentPrgEmbeddedId();
				id.setQuestionid(studentPrgDto.getQuestionid());
				id.setUsername(studentPrgDto.getUsername());
		    	Optional<StudentPrgEntity> stuprgentity = studentPrgRepository.findById(id);
		    	if(stuprgentity.isPresent())
		    	{
		    		StudentPrgEntity studentPrgEntity1=stuprgentity.get();
		    		studentPrgEntity1.setSolution(studentPrgDto.getSolution());
		    		studentPrgEntity1.setProgramname(studentPrgDto.getProgramname());
		    		studentPrgEntity1.setLanguage(studentPrgDto.getprogrammingLanguage());
		    		studentPrgEntity1.setStatus("Solve Question");
		    		studentPrgRepository.save(studentPrgEntity1);
					return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.PRG_UPDATED_SUCCESSFULLY,null);


		    	}
		    	else
		    	{
					studentPrgEntity.setEmbeddedid(id);
					studentPrgEntity.setSolution(studentPrgDto.getSolution());
					studentPrgEntity.setTestCasePassed(studentPrgDto.getTestCasePassed());
					studentPrgEntity.setLanguage(studentPrgDto.getprogrammingLanguage());
					studentPrgEntity.setTotalmark(studentPrgDto.getTotalmark());
					studentPrgEntity.setProgramname(studentPrgDto.getProgramname());
					studentPrgEntity.setStatus("Solve Question");
					studentPrgRepository.save(studentPrgEntity);
					return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.USERLOG.added_success,null);
		    	}

			}
			else
			{
				return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.USERLOG.USER_CODE_NOT_FOUND,null);

			} //IF ELSE ENDS HERE

		} //TRY ENDS HERE
		catch(Exception e)
		{
//			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.USERLOG.USER_CODE_NOT,null);

		} //TRY CARCH ENDS HERE
	}
	
	
	public Response runTestCase(EmbeddedidDTO id) throws IOException, InterruptedException
	{
		List<JSONObject> resultList = new ArrayList<>();
    	Response response=new Response();
		StudentPrgEmbeddedId entityid=new StudentPrgEmbeddedId();
		entityid.setQuestionid(id.getQuestionid());
		entityid.setUsername(id.getUsername());
		Optional<StudentPrgEntity> entity = studentPrgRepository.findById(entityid);
		StudentPrgEntity entity1=entity.get();
        // Save the Java code to a temporary file
        String fileName = entity1.getProgramname()+".java";
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) 
        {
            writer.println(entity1.getSolution());
        }
         // Compile the Java code using the external process
        Process compileProcess = new ProcessBuilder("javac", fileName).start();
        int compileExitCode = compileProcess.waitFor();
        int inputnum =1;
        if (compileExitCode == 0) 
        {
        	String error="error";
        	JSONObject results = new JSONObject();
        	String[] testcases=findallTestCases(id.getQuestionid());
        	JSONObject testcasesoutput=findallTestCaseOutputs(id.getQuestionid());

        	for (String testCase : testcases) 
        	{        		
        		String input = runJavaCode(entity1.getProgramname(), testCase);
        		System.out.println(input);
        		if(input.equals(error))
        		{
        			response.setOutput(0);
//                	response.setResult("UnSuccesssfull Executed");
        			return response;
        		}
        		results.put("input"+inputnum,input);
        		resultList.add(results);
        		inputnum++;
        		

        	}
        	String classfile=entity1.getProgramname()+".class";
        	deleteFile(classfile);
        	deleteFile(fileName);
        	List<JSONObject> num =compareJsonObjects(results,testcasesoutput,entityid);
        	JSONObject finalresults = new JSONObject();
        	finalresults.put("testcaseresult", num);
        	response.setOutput(10);
        	response.setResult(finalresults);
        	response.setPassed("yes");
        	return response;
        	
        } else 
        {
            // Compilation failed
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder compileError = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) 
            {
                compileError.append(line).append("\n");
            }
//            return "Compilation failed:\n" + compileError.toString();
            response.setCompileError(compileError);
            response.setOutput(0);
//            response.setResult("COMPILATION ERROR");
            return response;
        }


	}
	
	
	
	private String runJavaCode(String programname,String input) throws IOException, InterruptedException
	{
        Process runProcess = new ProcessBuilder("java", "-cp", ".", programname).start();
        OutputStream outputStream = runProcess.getOutputStream();
        try (PrintWriter inputWriter = new PrintWriter(outputStream)) 
        {
            inputWriter.println(input);
        }
        int runExitCode = runProcess.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) 
        {
            output.append(line);
        }

        if (runExitCode == 0) 
        {
//        	System.out.println(output.toString());
            return output.toString();
        } else 
        {
//        	System.out.println(output.toString());
            return "error"; 
        }

	}
	
	private String[] findallTestCases(String questionid) {
	    Optional<PrgQuestionEntity> num = prgquestion.findById(questionid);
	    PrgQuestionEntity request=num.get();

	    String[] testCasesArray = new String[]{
	        request.getTestcase1(),
	        request.getTestcase2(),
	        request.getTestcase3(),
	        request.getTestcase4(),
	        request.getTestcase5(),
	        request.getTestcase6(),
	        request.getTestcase7()
	    };


	    return testCasesArray;
	}
	
	private JSONObject findallTestCaseOutputs(String questionid) {
	    Optional<PrgQuestionEntity> num = prgquestion.findById(questionid);
	    PrgQuestionEntity request = num.get();

	    JSONObject jsonObject = new JSONObject();

	    jsonObject.put("output1", request.getTestcase1A());
	    jsonObject.put("output2", request.getTestcase2A());
	    jsonObject.put("output3", request.getTestcase3A());
	    jsonObject.put("output4", request.getTestcase4A());
	    jsonObject.put("output5", request.getTestcase5A());
	    jsonObject.put("output6", request.getTestcase6A());
	    jsonObject.put("output7", request.getTestcase7A());

	    return jsonObject;
	}
	
	
	private void deleteFile(String fileName) 
	{
		File file = new File(fileName);
        if (file != null && file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete file: " + file.getAbsolutePath());
            }
        }
    }
	
    private List<JSONObject> compareJsonObjects(JSONObject json1, JSONObject json2,StudentPrgEmbeddedId entityid) 
    {
    	int testCasePassed=0;
    	Optional<StudentPrgEntity> entity = studentPrgRepository.findById(entityid);
		StudentPrgEntity entity1=entity.get();
//		System.out.println(entity1.getLanguage());
//		System.out.println(entity1.getTotalmark());
//		System.out.println(entity1.getSolution());
		List<JSONObject> resultList = new ArrayList<>();
    	for(int i=1;i<8;i++)
    	{
        	JSONObject results = new JSONObject();
    		if(json1.get("input"+i).equals(json2.get("output"+i)))
    		{
        		results.put("TestCase"+i,"Passed");
        		testCasePassed++;

    		}
    		else
    		{
        		results.put("TestCase"+i,"Failed");
//        		resultList.add(results);

    		}
    		resultList.add(results);
//    		System.out.println(results);
    	}
    	
//    	System.out.println("TestCae Passed Result");
//		System.out.println(entity1.getTestCasePassed());
//		System.out.println(testCasePassed);
		if(entity1.getTestCasePassed()<testCasePassed)
		{
    		entity1.setTestCasePassed(testCasePassed);
    		entity1.setTotalmark(testCasePassed*10);
    		studentPrgRepository.save(entity1);
		}
    	return resultList;
    }


	public Response runJavaProgram(CustomTestDTO id) throws IOException, InterruptedException 
	{
    	Response response=new Response();
    	String fileName = id.getProgramname()+".java";
    	File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) 
        {
            writer.println(id.getProgram());
        }
         // Compile the Java code using the external process
        Process compileProcess = new ProcessBuilder("javac", fileName).start();
        int compileExitCode = compileProcess.waitFor();
        int inputnum =1;
        if (compileExitCode == 0) 
        {
        	String error="error";
        	JSONObject results = new JSONObject();
        	String testcases=id.getInput(); 		
        	String input = runJavaCode(id.getProgramname(), testcases);
//            System.out.println(input);
        	results.put("Result",input);
        	String classfile=id.getProgramname()+".class";
        	deleteFile(classfile);
        	deleteFile(fileName);
        	response.setPassed("YES");
        	response.setResult(results);
        	return response;
        	
        } 
        else 
        {
            // Compilation failed
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder compileError = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) 
            {
                compileError.append(line).append("\n");
            }
            response.setPassed("no");
            response.setCompileError(compileError);
            response.setOutput(0);
            response.setResult(null);
            return response;
        }
	}


@Override
	public ServiceResponse createAnswers(StudentMcqDto dto) {
		try {
			
			StudentMcqEmbedded studentMcqEmbedded = new StudentMcqEmbedded();
			studentMcqEmbedded.setQuestionId(dto.getQuestionId());
			studentMcqEmbedded.setUserName(dto.getUserName());
			
			Optional<McqQuestionEntity> optionalQuestionEntity = mcqQuestionRepository.findById(dto.getQuestionId());
			
			McqQuestionEntity mcqQuestionEntity = optionalQuestionEntity.get();
				StudentMcqEntity studentMcqEntity = new StudentMcqEntity();
				studentMcqEntity.setStudentMcqEmbedded(studentMcqEmbedded);
				studentMcqEntity.setSelected(dto.getSelected());
				studentMcqEntity.setMcqStatus(dto.getMcqStatus());
				
				if (String.valueOf(dto.getQuestionId().charAt(0)).toUpperCase().equals("M")) {
					int mark = 0;
					if (dto.getSelected() !=null && dto.getSelected().equalsIgnoreCase(mcqQuestionEntity.getAnswer())) {
					
							mark = 5;
							studentMcqEntity.setMarks(Integer.toString(mark));
						
						
					} else {
						studentMcqEntity.setMarks(Integer.toString(mark));
					}
				}
				studentMcqRepository.save(studentMcqEntity);
				
				
				
				return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.answer_Added,null);
			
		}catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.QUESTION.not_Added,null);
		}
	}
	@Override
	public StudentMcqDto getSelectedMcqAnswer(String userName, String questionId) {

		StudentMcqDto studentMcqDto = new StudentMcqDto();
		
		
		try {
			StudentMcqEmbedded studentMcqEmbedded = new StudentMcqEmbedded();
			StudentMcqEntity studentMcqEntity = new StudentMcqEntity();
			
			studentMcqEmbedded.setUserName(userName);
			studentMcqEmbedded.setQuestionId(questionId);
			
			Optional<StudentMcqEntity> entity = studentMcqRepository.findById(studentMcqEmbedded);
			
			if(entity.isPresent()) {
				studentMcqEntity = entity.get();

				studentMcqDto.setQuestionId(questionId);
				studentMcqDto.setUserName(userName);
				studentMcqDto.setSelected(studentMcqEntity.getSelected());
			}
			
		}catch(Exception e) {
			
		}
		
		return studentMcqDto;
	}


	@Override
	public ServiceResponse updateMark(String userName) {

		try {
			Optional<SelectedQuestionEntity> studentEntityOptional = selectedQuestionRepository.findById(userName);

			if (studentEntityOptional.isEmpty()) {
				throw new Exception("Student not attended exam");
			} else {
				SelectedQuestionEntity studentEntity = new SelectedQuestionEntity();
				studentEntity = studentEntityOptional.get();
				int mark = 0;

				List<StudentMcqEntity> answers = studentMcqRepository.findByStudentMcqEmbeddedUserName(userName);

				for (StudentMcqEntity i : answers) {

					if (i.getMarks() != null && !i.getMarks().isEmpty()) {
						mark += Integer.parseInt(i.getMarks());
					} else {
						mark += 0;
					}

				}
				studentEntity.setMcqMark(Integer.toString(mark));

				selectedQuestionRepository.save(studentEntity);
			}
			return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.not_Added,null);
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.QUESTION.not_Added,null);
		}

	}


	
	public Response runCProgram(CustomTestDTO id) throws IOException, InterruptedException {
		return null;
		
		
		
	}
	
	

boolean isCompileError = true;
	
	public Response runCppProgram(CustomTestDTO id) throws IOException, InterruptedException {
	
		JSONObject df = new JSONObject();
		df = executeCppProgram(id.getProgram(),"admin",id.getInput());
//		System.out.println("testing result"+ df);
		Response response = new Response();
		response.setResult(df);
		
		if(isCompileError) 
		{
		StringBuilder error = new  StringBuilder();
		error.append(compilationError);
//		System.out.println(error + "error");
		response.setCompileError(error);
		JSONObject er = new JSONObject();
	    response.setPassed("failed");	}
		return response;
	}
	  Integer testCaseId = 1;
	  private final List<JSONObject> resultList = new ArrayList<>();
	 public JSONObject executeCppProgram(String cppSourceCode,String role, String testCases
	            )   
	    {
	    	
	        try {
	        	   if (testCases == null) {
	        	        // Handle the case where testCases is null, perhaps by returning an appropriate response or throwing an exception.
	        	        // For example:
	        	        throw new IllegalArgumentException("Test cases cannot be null");
	        	    }
	        	
	        
	            File cppFile = new File("temp.cpp");
	            FileWriter fileWriter = new FileWriter(cppFile);
	            fileWriter.write(cppSourceCode);
	            fileWriter.close();

	              isCompileError = compileCppCode(cppFile);

	            String[] testCaseSets = testCases.split("\n");
	            String id ="";
//	             AtomicInteger totalMarks = new AtomicInteger(0);  //need to chane to atomic variable to make it more atomic
	           Integer totalMarks = 0;//may need to change
	           resultList.clear();
	           if (new File("temp.exe").exists()) {
	        	    try {
	        	        for (String testCaseSet : testCaseSets) {
	        	            executeCompiledBinary("temp.exe", testCaseSet, role, id);
	        	            testCaseId++;
	        	        }
	        	    } catch (IOException e) {
	        	        e.printStackTrace();
	        	    }
	        	} else {
	        	    // Handle the case when "temp.exe" does not exist
//	        	    System.out.println("Error: temp.exe not found");
	        	    // You might want to throw an exception or return from the method, depending on your requirements.
	        	    // For example, you could throw a FileNotFoundException like this:
	        	    // throw new FileNotFoundException("temp.exe not found");
	        	}

	            deleteExistingExecutable();
//	            System.out.println(getResultList());
	            return getResultList();

	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	 
	 public JSONObject getResultList() {
	    	
	    	result.put("customTestCasesResponse", resultList);
	        return result;
	    }
	 private boolean executeCompiledBinary(String binaryPath, String testCases, String role, String id) throws IOException, InterruptedException {
	        // Split the test cases by newline to handle each set independently
	        String[] testCaseLines = testCases.split("\n");

	        // Use an array to make isTle effectively final
	        boolean[] isTle = {false};

	        // Set a timeout value in seconds
	        int timeoutSeconds = 5;

	        // Keep references to the threads
	        List<Thread> threads = new ArrayList<>();

	        // Execute the compiled binary with each test case line as input
	        for (String testCaseLine : testCaseLines) {
	            // Check if isTle is true, break out of the loop
	        	if (isTle[0]) {
//	                System.out.println("Execution discontinued due to TLE in a previous thread.");
	                return true;
//	                break;
	            }
	        	
	            ProcessBuilder executeProcessBuilder = new ProcessBuilder(binaryPath);
	            executeProcessBuilder.redirectErrorStream(true);

	            Process executeProcess = executeProcessBuilder.start();

	            // Pass the current test case line as input to the C++ program with newline
	            executeProcess.getOutputStream().write((testCaseLine + "\n").getBytes());
	            executeProcess.getOutputStream().flush();

	            // Start a separate thread to wait for the process to complete with a timeout
	            Thread waitForProcessThread = new Thread(() -> {
	                try {
	                    boolean processCompleted = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

	                    if (processCompleted) {
	                        int executeExitCode = executeProcess.exitValue(); // if not 0 program fails
//	                        System.out.println("Output of Testcase " + testCaseId);

	                        if (role.equals("admin") || role.equals("superAdmin"))
	                        {
	                            printProcessOutput(executeProcess.getInputStream(), "Execution", testCaseLine);
	                        }
	                        else if(role.equals("user")){
	                        	
//	                            printUserProcessOutput(executeProcess.getInputStream(), "Execution", testCaseLine, id);
	                        }
	                        else
	                        {
//	                        	System.out.println("printing else ---------------------->");
	                        }
	                    } else {
//	                        System.out.println("Process execution timed out");

	                        // Destroy the external process explicitly
	                        executeProcess.destroy();

	                        // Wait for the process to terminate
	                        boolean processTerminated = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

	                        if (!processTerminated) {
	                            System.out.println("Process not terminated after destroy. Forcefully killing the process.");
	                            executeProcess.destroyForcibly();
	                        }

	                        // Set isTle to true to discontinue with the execution of other threads
	                        isTle[0] = true;

	                        // Interrupt other running threads
	                        for (Thread thread : threads) {
	                            thread.interrupt();
	                        }

	                        // Handle other actions or go to a function that returns TLE {time limit exceeded}
	                    }
	                } catch (InterruptedException e) {
	                    // Handle InterruptedException (e.g., log or rethrow if needed)
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });

	            // Keep a reference to the thread
	            threads.add(waitForProcessThread);

	            waitForProcessThread.start();
	            waitForProcessThread.join(); // Wait for the thread to finish
	        }
	        
	        return isTle[0];
	    }

	 private void printProcessOutput(InputStream inputStream, String type, String testCase) throws IOException {
	        // Print the output of the process
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	        StringBuilder output = new StringBuilder();
	        String line;
	        JSONObject adminResultObject = new JSONObject();

	        while ((line = reader.readLine()) != null) {
//	            System.out.println(line);
//	            output.append(line).append("\n");
	              output.append(line);
	        }

	        // Create a JsonObject to store the result
	        
//	        adminResultObject.put("type", type);
//	        adminResultObject.put("testCase", testCase);
	        adminResultObject.put("output", output);
	        adminResultObject.put("testCaseId", testCaseId);

//	        System.out.println(adminResultObject);
//	        testCaseId = (testCaseId >= 10) ? 0 : testCaseId;
	        // Add the result to the resultList
	        resultList.add(adminResultObject);
//	        System.out.println(resultList);
	    }
	 
	  private void deleteExistingExecutable() {
	        try {
	            // Delete any existing temp.exe file
	            File existingExecutable = new File("temp.exe");
//	            System.out.println("File exists: " + existingExecutable.exists());
//	            System.out.println("File path: " + existingExecutable.getAbsolutePath());

	            if (existingExecutable.exists()) {
	                boolean deleted = existingExecutable.delete();
//	                System.out.println("Deletion result: " + deleted);
	            }
	        } catch (SecurityException e) {
	            e.printStackTrace();
	            // Log or handle the exception as needed
	        }
	    }
	
	 private static final int TIMEOUT_SECONDS = 4; // Set
	 private boolean compileCppCode(File cppFile) throws IOException, InterruptedException {
	        try {
				ProcessBuilder compileProcessBuilder = new ProcessBuilder("g++", "-o", "temp.exe", cppFile.getPath());
				compileProcessBuilder.redirectErrorStream(true);

				Process compileProcess = compileProcessBuilder.start();
				boolean compilationSuccessful;

				// Start a timer
				long startTime = System.currentTimeMillis();

				try {
				    int compileExitCode;
				    // Wait for the process to finish or timeout
				    while (true) {
				        try {
				            compileExitCode = compileProcess.exitValue();
				            break; // The process has exited
				        } catch (IllegalThreadStateException e) {
				            // The process is still running
				        }

				        if (System.currentTimeMillis() - startTime > TIMEOUT_SECONDS * 1000) {
				            compileProcess.destroy();
//				            System.out.println("Compilation timed out");
				            return false;
				        }

				        Thread.sleep(100);
				    }

				    compilationSuccessful = (compileExitCode == 0);
				} finally {
				    compileProcess.waitFor(); // Ensure the process is fully terminated
				}

				if (compilationSuccessful) {
//				    System.out.println("Compilation successful");
				} else {
//				    System.out.println("Compilation Failed");
				    printCompilationError(compileProcess.getInputStream(), "Compilation", "");
				}
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return isCompileError;
	    }
	 
	 JSONObject compilationError = new JSONObject();

	  private void printCompilationError(InputStream inputStream, String type, String testCase) throws IOException {
	    	
	    	
	    	 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	         StringBuilder output = new StringBuilder();
	         String line;

	         while ((line = reader.readLine()) != null) {
//	             System.out.println(line);
	             compilationError.put( "Compilation Error:",line);
//	             output.append(line).append("\n");
//	               output.append(line);
	         }
	         
	          
//	         summary.clear();
//	         summary.add(compilationError);
	         
	    
	    }
	@Override
	public Response runNoProgram() throws IOException, InterruptedException {
        StringBuilder compileError = new StringBuilder();
        compileError.append("No Language Selected");
    	Response response=new Response();
        response.setPassed("no");
        response.setCompileError(compileError);
        response.setOutput(0);
        response.setResult(null);
        return response;
	}


	@Override
	public Response runProgram(CustomTestDTO id) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ServiceResponse submitPrgQuestions(EmbeddedidDTO id) {
	    try {
	        Long totalValue = studentPrgRepository.findTotalmarkByUsername(id.getUsername());
//	        System.out.println(totalValue);
	        Optional<SelectedQuestionEntity> studentidentity = selectedQuestionRepository.findById(id.getUsername());

	        if (studentidentity.isPresent()) {
	            SelectedQuestionEntity studentrowEntity = studentidentity.get();
	            studentrowEntity.setPrgMark(totalValue.toString());
	            selectedQuestionRepository.save(studentrowEntity);

	            String mcqMarkAsString = studentrowEntity.getMcqMark();
//		        System.out.println(mcqMarkAsString);

	            int mcqMarkAsInt = Integer.parseInt(mcqMarkAsString);
	            Long totalMark = mcqMarkAsInt + totalValue;

	            Optional<RegistrationEntity> registraionEntity = registrationRepo.findById(id.getUsername());

	            if (registraionEntity.isPresent()) {
	                RegistrationEntity studentDeatils = registraionEntity.get();
	                studentDeatils.setTotalMarks(totalMark.toString());
	                registrationRepo.save(studentDeatils);
	                return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, "Updated Total Mark", null);
	            } else {
	                return new ServiceResponse(Constants.MESSAGE_STATUS.FAIL, Constants.USERLOG.USER_NOT_FOUND, null);
	            }
	        } else {
	            return new ServiceResponse(Constants.MESSAGE_STATUS.FAIL, Constants.USERLOG.USER_NOT_FOUND, null);
	        }
	    } catch (Exception e) {
	        // Handle the exception appropriately, log it, or take any necessary action
			logger.error("Unexpected error: " + e.getMessage());
	        return new ServiceResponse(Constants.MESSAGE_STATUS.FAIL, "An error occurred", null);
	    }
	}



	@Override
	public ServiceResponse getUserSavedAnswer(EmbeddedidDTO id) 
	{
		StudentPrgEmbeddedId embeddedid=new StudentPrgEmbeddedId();
		embeddedid.setQuestionid(id.getQuestionid());
		embeddedid.setUsername(id.getUsername());
    	Optional<StudentPrgEntity> stuprgentity = studentPrgRepository.findById(embeddedid);
    	if(stuprgentity.isPresent())
    	{
    		StudentPrgEntity studentPrgEntity=stuprgentity.get();
    		if(studentPrgEntity.getStatus() == null)
    		{
    			studentPrgEntity.setStatus("Attended");
    			studentPrgRepository.save(studentPrgEntity);
    		}
			StudentPrgEntity studententity=stuprgentity.get();
//    		System.out.println("Sved code");
//    		System.out.println(id.getQuestionid());
//    		System.out.println(id.getUsername());
//    		System.out.println(studententity.getLanguage());
//    		System.out.println(studententity.getSolution());
			return new ServiceResponse(studententity.getLanguage(),studententity.getSolution(),null);

    	}		
    	else
    	{
            String javaCode = "import java.util.Scanner;\n" +
                    "public class Main {\n" +
                    "    public static void main(String[] args) {\n" +
                    "}";
			return new ServiceResponse("java",javaCode,null);
    	}
	}


	

@Override
	public JSONObject getMcqStatus(String userName) {
		
		JSONObject mcqStatus = new JSONObject();
		
		try {
			
			SelectedQuestionEntity selectedQuestionEntity = selectedQuestionRepository.findById(userName).get();
			
			List<String> selectedQuestionList =selectedQuestionEntity.getSelectedMcqQuestionId();
			
			List<StudentMcqEntity> optionalEntity = studentMcqRepository.findByStudentMcqEmbeddedUserName(userName);
			
//			System.out.println("Before status array");
			String [] statusArray = new String[selectedQuestionList.size()];
//			System.out.println(selectedQuestionList.size());
			if(optionalEntity.isEmpty()) {
				mcqStatus.put("message", "failed");
			}else {
				int countU = 0;
				int countA = 0;
				int countS = 0;
				for(StudentMcqEntity i : optionalEntity) {
					
					statusArray[selectedQuestionList.indexOf(i.getStudentMcqEmbedded().getQuestionId())]= i.getMcqStatus();
//					System.out.println(statusArray[selectedQuestionList.indexOf(i.getStudentMcqEmbedded().getQuestionId())]);
//					System.out.println(statusArray);
					if(i.getMcqStatus().equalsIgnoreCase("U")) {
						countU++;

					}else if(i.getMcqStatus().equalsIgnoreCase("A")) {
						countA++;
//						System.out.println(countA);
					}else if(i.getMcqStatus().equalsIgnoreCase("S")) {
						countS++;
					}
				}
				mcqStatus.put("unanswered", countU);
				mcqStatus.put("answered", countA);
				mcqStatus.put("saved", countS);
				mcqStatus.put("message", "success");
				mcqStatus.put("array", statusArray);
			}
			
		}catch(Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		}
		return mcqStatus;
	}








	
	public ServiceResponse attendedPrgQuestionStatus(String userName)
	{
		if(studentPrgRepository.existsByEmbeddedidUsername(userName))
		{
			try {
			    List<StudentPrgEntity> studentrowlist = studentPrgRepository.findByEmbeddedidUsername(userName);
			    List<JSONObject> responselist = new ArrayList<>();
			    
			    for (StudentPrgEntity userRow : studentrowlist) {
			        JSONObject eachUser = new JSONObject();
			        Optional<QuestionEntity> qheading=questionRepository.findById(userRow.getEmbeddedid().getQuestionid());
			        QuestionEntity qestionHeading=qheading.get();
			        eachUser.put("questionid", qestionHeading.getQuestionHeading());
			        eachUser.put("testCasePassed", userRow.getTestCasePassed());
			        responselist.add(eachUser);
			    }

			    return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, Constants.MESSAGE_STATUS.success, responselist);
			} 
			catch (Exception e) 
			{
				logger.error("Unexpected error: " + e.getMessage());
			    return new ServiceResponse(Constants.MESSAGE_STATUS.error, "An error occurred", null);
			}
		}
		else
		{
		    return new ServiceResponse(Constants.MESSAGE_STATUS.error,Constants.USERLOG.USER_NOT_FOUND, null);
		}
	
		
	}
	
	
	public ServiceResponse updateUserTimer(StudentTimerDTO  timerdto)
	{
		try {
		    Optional<SelectedQuestionEntity> userquestion = selectedQuestionRepository.findById(timerdto.getUsername());

		    if (userquestion.isPresent()) {
		        SelectedQuestionEntity timerupdate = userquestion.get();
		        timerupdate.setTimer(timerdto.getTimer());
		        selectedQuestionRepository.save(timerupdate);
		        return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.Timer_Updated, null);
		    } else 
		    {
		        // Handle the case when the entity with the given ID is not found
		        // You may throw a custom exception, log the error, or return an appropriate response based on your requirements
		        return new ServiceResponse(Constants.MESSAGE_STATUS.error, Constants.USERLOG.USER_NOT_FOUND, null);
		    }
		} catch (Exception e) {
		    // Handle the exception appropriately, log it, or take any necessary action
			logger.error("Unexpected error: " + e.getMessage());
		    return new ServiceResponse(Constants.MESSAGE_STATUS.error, "Error updating timer", null);
		}


	}

	


	
	
		
	

}
