package com.compiler.question.serviceImp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.compiler.question.dto.AdminResponse;
import com.compiler.question.dto.Response;
import com.compiler.question.dto.TestCasesDto;
import com.compiler.question.exception.SourceCodeNotFoundException;
import com.compiler.question.exception.TestCaseFailException;
import com.compiler.question.exception.TestCaseNullException;
import com.compiler.question.service.AdminJavaCompilerService;
import com.compiler.question.util.FileDeletionService;

@Service
public class AdminJavaCompilerServiceImpl implements AdminJavaCompilerService  {

	
	@Value("${vulnerablityCheckFlag}")
	private boolean vulnerablityCheckFlag;
	
	private static Logger logger = LogManager.getLogger(AdminJavaCompilerService.class);
	private List<JSONObject> summary = Collections.synchronizedList(new ArrayList<>());
	private final List<JSONObject> resultList = Collections.synchronizedList(new ArrayList<>());
	private String userCode;
	public AdminResponse checkAdminJavaProgram(String javaSourceCode, TestCasesDto testCases,UUID randomUUID)
			throws TestCaseFailException, Exception,TestCaseNullException {
		
	if(javaSourceCode == null )
	{
		throw new SourceCodeNotFoundException("Source code is null");
	}
		
		Response adminResult = new Response();
		
		
		 if (testCases == null) {
 	        throw new TestCaseNullException("Test cases cannot be null");
 	    }
 	String testCase1 = testCases.getTestCase1();
 	String testCase2 = testCases.getTestCase2();
 	String testCase3 = testCases.getTestCase3();
     String testCase4 = testCases.getTestCase4();
     String testCase5 = testCases.getTestCase5();
     String testCase6 = testCases.getTestCase6();
     String testCase7 = testCases.getTestCase7();
     String testCase8 = testCases.getTestCase8();
     String testCase9 = testCases.getTestCase9();
     String testCase10 = testCases.getTestCase10();
 	
     String testCasesJoin = testCase1 + "\n" + testCase2 + "\n" + testCase3 + "\n" + testCase4 + "\n"
             + testCase5 + "\n" + testCase6 + "\n" + testCase7 + "\n" + testCase8 + "\n"
             + testCase9 + "\n" + testCase10;
     
		adminResult = executeAdminJavaProgram(javaSourceCode,testCasesJoin,randomUUID);
		AdminResponse admin = new AdminResponse();
		admin.setExtractedFunction(userCode);
		admin.setResponse(adminResult);
		
		return admin;
		
		
		
	}
	
	
	private Response executeAdminJavaProgram(String javaSourceCode, String testCases, UUID randomUUID)
			throws Exception {
		File javaFile = null;
		try {
			summary.clear();
			try
			{
			 StringBuilder code = extractJavaFunctionDeclarations(javaSourceCode);
			 userCode =  code.toString();
			 if(userCode == null ) {
					userCode= javaSourceCode; 
				 }
			}catch(Exception e)
			{
				logger.error("Failed to extract fucnction" + e.getMessage());
				userCode = javaSourceCode.toString();
			}
				String uniqueFileName = "Main.java";
				javaFile = new File(uniqueFileName);
			
			 try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
		            writer.write(javaSourceCode);
		            logger.info("Write to file successful.");
		        } catch (IOException e) {
		        	 logger.error("Write to file failed." + e.getMessage());
		        }
			boolean isCompiled = false;
			summary.clear();  //to clear if it contains any results
			isCompiled = compileJavaCode(javaFile, randomUUID);
			if (!isCompiled) {
				JSONObject err = new JSONObject();
				err.put("COMPILATION TIMEOUT ERROR", "Check Your Syntax!!!");
				summary.add(err);
				JSONObject fErr = new JSONObject();
				fErr.put("Terminal", summary);
				Response response = new Response();
				StringBuilder errResponse = new StringBuilder();
				errResponse.append(fErr);
				response.setCompileError(errResponse);
				response.setPassed("NO");
				logger.error("CompilationTimeOutError: compilation not completed within the time");
				return response;

			}
			String[] testCaseSets = testCases.split("\n");


			if (vulnerablityCheckFlag) {
				boolean isClean = scanExecutableWithDivineAV(javaFile.getName());
				if (!isClean) {
					logger.error("Executable contains threats. Aborting execution...");
					logger.info("will run code in controlled enviornment "); // need to do
					// in the future
					// Handle the case where threats are found
					Response report = new Response();
					StringBuilder scanReport = new StringBuilder();
					scanReport.append("Executable contains threats. Aborting execution...");
					scanReport.append("will run code in controlled enviornment");
					Response response = new Response();
					response.setCompileError(scanReport);
					report.setPassed("NO");
					return response;
				}
			}
			// Execute the C++ code for each set of test cases
			Response response = new Response();

			for (String testCaseSet : testCaseSets) {
				File executableFile = new File("Main"+ ".class");
				if (!executableFile.exists()) {
					return null;
				}

				ExecutionResult result;
				try {
					result = executeCompiledBinary(javaFile.getName(), testCaseSet);
				} catch (TestCaseFailException e) {
					logger.info("Time Limit Exceeded.."+e.getMessage());
					throw e;
				}
				catch (Exception e) {
					return null;
				}
				if (result != null) {
					boolean isTle = result.isTle();
					List<JSONObject> summary = result.getSummary();
					isTle = false; // need to change
					if (isTle == true) // future extend this with a switch allowing different exe and testcases
					{ // need to add the code as a response
						JSONObject tle = new JSONObject();
						tle.put("Rejected", "TLE");
						summary.clear();
						summary.add(tle);
						logger.warn("Aborting...Time Limit Exceeded");

						StringBuilder tleResponse = new StringBuilder();
						tleResponse.append(tle);
						response.setCompileError(tleResponse);

						deleteExistingExecutable(javaFile);  
						break;
					} else {

						response = new Response();
						JSONObject finalResult = new JSONObject();
						finalResult.put("Result", summary);
						response.setResult(finalResult);
						response.setPassed("YES");
					}

				} else {

					logger.fatal("Result from executeuserjava is null...fatal");
					summary.clear();
					response.setCompileError( new StringBuilder("error"));
					response.setPassed("NO");
				}
			}

			summary.clear();

			deleteExistingExecutable(javaFile);// ---------------

			return response;

		} catch (TestCaseFailException e) {
			FileDeletionService deletionService = new FileDeletionService();
            deletionService.deleteFilesWithPrefix(".", javaFile.getName());
			throw e;
		}

		catch (IOException | InterruptedException e) {
			logger.error("Exception: "+ e.getMessage());
			throw e;
		}
	
		catch(Exception e ){
			logger.error("Exception"+ e.getMessage());
			throw e;
		}
		finally {
			deleteExistingExecutable(javaFile);
		}

	}
	
	private static final int TIMEOUT_SECONDS = 4; // Set your desired timeout in seconds

	private boolean compileJavaCode(File javaFile, UUID randomUUID) throws IOException, InterruptedException {
	    try {
	        ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-d", ".", javaFile.getPath());
	        compileProcessBuilder.redirectErrorStream(true);

	        Process compileProcess = compileProcessBuilder.start();
	        boolean compilationSuccessful;

	        // Start a timer
	        long startTime = System.currentTimeMillis();

	        int compileExitCode = 1;
	        try {
	            if (System.currentTimeMillis() - startTime > TIMEOUT_SECONDS * 1000) {
	                // Timeout reached, destroy the process
	                compileProcess.destroy();
	                logger.error("Compilation timed out. Aborting...");
	                return false;
	            }

	            compileProcess.waitFor();
	            compileExitCode = compileProcess.exitValue();
	            compileProcess.destroy();
	            compileProcessBuilder.directory();
	        } catch (Exception e) {
	            logger.error("Compilation Failed " + e.getMessage());
	            return false;
	        } finally {
	            // Ensure the process is fully terminated
	            compilationSuccessful = (compileExitCode == 0);
	        }

	        if (compilationSuccessful) {
	            logger.info("Compilation successful");
	            return true;
	        } else {
	            logger.error("Compilation Failed. Check Syntax");
	            printCompilationError(compileProcess.getInputStream(), "Compilation", "");
	            return false;
	        }

	    } catch (IOException e) {
	        logger.error("IOException compilation failed " +  e.getMessage(), e);
	        return false;
	    } catch (Exception e) {
	        logger.error("Exception occurred. Compilation Failed");
	        return false;
	    }
	}
	
	private void printCompilationError(InputStream inputStream, String type, String testCase) throws IOException {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			JSONObject compilationError = new JSONObject();

			while ((line = reader.readLine()) != null) {
//				 System.out.println(line);
				compilationError.put(line, "Compilation Error");
			}
			summary.clear();
			summary.add(compilationError);
		} catch (Exception e) {
			logger.error("Printing CompilationError Failed " + e.getMessage(),e);
			e.printStackTrace();
		}

	}

private boolean scanExecutableWithDivineAV(String fileName) {
    try {
        String currentDirectory = System.getProperty("user.dir");
        String absoluteFilePath = Paths.get(currentDirectory, fileName).toAbsolutePath().toString();
//        System.out.println("Absolute file path: " + absoluteFilePath);

        // Use a different path inside the container (e.g., /app1/temp.exe)
        String containerPath = "/app1/" + fileName;

        String outputFileName = Paths.get(currentDirectory, "clamav_output.log").toAbsolutePath().toString();
//        System.out.println("Output file path: " + outputFileName);

        ProcessBuilder dockerProcessBuilder = new ProcessBuilder(
                "docker", "run", "--rm", "-v", absoluteFilePath + ":" + containerPath, "divine-av", "clamscan", "--remove", "--recursive", "--infected", "--no-summary", containerPath);

        File outputFile = new File(outputFileName);
        dockerProcessBuilder.redirectOutput(outputFile);
        dockerProcessBuilder.redirectErrorStream(true);

//        System.out.println("Docker command: " + String.join(" ", dockerProcessBuilder.command()));

        Process dockerProcess = dockerProcessBuilder.start();
        int dockerExitCode = dockerProcess.waitFor();

        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(dockerProcess.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                logger.error(errorLine);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        }

        return dockerExitCode == 0;

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        return false;
    }
}



	private void deleteExistingExecutable(File javaFile) {

		try {
			File existingExecutable = new File(javaFile.getAbsolutePath() + ".exe");
			logger.info("File exists: {}", existingExecutable.exists());
			logger.info("File path: {}", existingExecutable.getAbsolutePath());

			if (existingExecutable.exists()) {
				boolean deleted = existingExecutable.delete();
				logger.info("Executable Deletion result : " + deleted);
			}

			if (javaFile.exists()) {
				boolean deleted = javaFile.delete();
				logger.info("File Deletion result: " + deleted);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			logger.error("Error while deleting files: {}", e.getMessage());
			return;
		} catch (NullPointerException e) {
			logger.error("NullPointerException occurred: {}", e.getMessage());
			return;
		} catch (Exception e) {
			logger.error("Exception occurred: {}", e.getMessage());
			return;
		}
	}
	
	
	private ExecutionResult executeCompiledBinary(String binaryPath, String testCases)
			throws IOException, InterruptedException, TestCaseFailException {

		try {
			// Split the test cases by newline to handle each set independently
			String[] testCaseLines = testCases.split("\n");

			// Use an array to make isTle effectively final
			boolean[] isTle = { false };

			// Keep references to the threads
			List<CompletableFuture<Boolean>> threads = new ArrayList<>();

			// Execute the compiled binary with each test case line as input
			for (String testCaseLine : testCaseLines) {
				// Check if isTle is true, break out of the loop
				if (isTle[0]) {
					logger.error("Execution discontinued due to TLE in a previous thread.");
					break;
				}
				threads.add(executePgm(testCaseLine, binaryPath, isTle));

			}

			CompletableFuture.allOf(toCfArray(threads));
			return new ExecutionResult(isTle[0], new ArrayList<>(summary));
		} catch (TestCaseFailException e) {
			throw e;
		}

		catch (Exception e) {
			logger.error("Failed to execute binary " , e.getMessage(),e);
			return null;
		}
	}
	
	
	public class ExecutionResult {
		private boolean isTle;
		private List<JSONObject> summary;

		public ExecutionResult(boolean isTle, List<JSONObject> summary) {
			this.isTle = isTle;
			this.summary = summary;
		}

		public boolean isTle() {
			return isTle;
		}

		public List<JSONObject> getSummary() {
			return summary;
		}
	}

	public class CustomExecutionResult {
		private boolean isTle;
		private StringBuilder summary;

		public CustomExecutionResult(boolean isTle, StringBuilder summary) {
			this.isTle = isTle;
			this.summary = summary;
		}

		public boolean isTle() {
			return isTle;
		}

		public StringBuilder getSummary() {
			return summary;
		}
	}

	private CompletableFuture<Boolean>[] toCfArray(List<CompletableFuture<Boolean>> threads) {
		try {
			CompletableFuture<Boolean>[] res = new CompletableFuture[threads.size()];
			for (int i = 0; i < threads.size(); i++) {
				res[i] = threads.get(i);
			}
			return res;
		} catch (Exception e) {
			logger.error("Error in conversion to CfArray" , e.getMessage(),e);
			return null;
		}
	}
	
	private CompletableFuture<Boolean> executePgm(String testCaseLine, String binaryPath, 
			boolean[] isTle) throws TestCaseFailException {
		try {
			boolean res = executeTestCase(testCaseLine, binaryPath, isTle);

			return CompletableFuture.completedFuture(res); // will create new threads on call
		} catch (TestCaseFailException e) {
			logger.error("TestCaseFailedException:"+ e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Execution and Thread Creation Failed"+ e.getMessage());
		}
		return null;
	}
	
	
	private boolean executeTestCase(String testCaseLine, String javaClassName,  boolean[] isTle)
			throws TestCaseFailException {
		// Set a timeout value in seconds
		int timeoutSeconds = 5;

		try {
			// Create a new ProcessBuilder for each thread
			ProcessBuilder executeProcessBuilder = new ProcessBuilder("java",javaClassName);

			executeProcessBuilder.redirectErrorStream(true);

			Process executeProcess = executeProcessBuilder.start();

			// Pass the current test case line as input to the C++ program with newline
			executeProcess.getOutputStream().write((testCaseLine + "\n").getBytes());
			executeProcess.getOutputStream().flush();

			boolean processCompleted = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

			if (processCompleted) {
				int executeExitCode = executeProcess.exitValue(); // if not 0 program fails

					 summary =printProcessOutput(executeProcess.getInputStream(), "Execution",testCaseLine);

				
			} else {
				logger.warn("Process execution timed out");

				// Destroy the external process explicitly
				executeProcess.destroy();

				// Wait for the process to terminate
				boolean processTerminated = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

				if (!processTerminated) {
					// System.out.println("Process not terminated after destroy. Forcefully killing
					// the process.");
					executeProcess.destroyForcibly();
				}
				throw new TestCaseFailException("Time limit exceeded");
				// Set isTle to true to discontinue with the execution of other threads
//				isTle[0] = true;

				// Handle other actions or go to a function that returns TLE {time limit
				// exceeded}
			}
			return true;
		} catch (TestCaseFailException e) {
			logger.error("starting tle" + e.getMessage());
			
			throw e;
		}

		catch (Exception e) {
			logger.error("Execution Failed "+e.getMessage());
		}
		return false;
	}
	 private static StringBuilder extractJavaFunctionDeclarations(String code) {
	        try {
	            // Assuming the function declaration starts with the access modifier and ends with "{"
	            Pattern functionPattern = Pattern.compile("\\b(\\w+)\\s+\\w+\\([^)]*\\)\\s*\\{");
	            Matcher functionMatcher = functionPattern.matcher(code);
	            StringBuilder extractedDeclarations = new StringBuilder();

	            while (functionMatcher.find()) {
	                // Extract only the function declaration (up to the opening curly brace)
	                int openingBraceIndex = code.indexOf('{', functionMatcher.start());
	                String extractedDeclaration = code.substring(functionMatcher.start(), openingBraceIndex + 1);

	                extractedDeclarations.append(extractedDeclaration).append("\n}");
	            }

	            if (extractedDeclarations.length() > 0) {
	                return extractedDeclarations;
	            } else {
	                logger.warn("Error: Functions not found");
	                return null; // Or handle the error as needed
	            }
	        } catch (Exception e) {
	            throw e;
	        }
	    }
	

	
	  Integer testCaseId = 1;
	 private List<JSONObject> printProcessOutput(InputStream inputStream, String type, String testCase) throws IOException {
	        // Print the output of the process
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	        StringBuilder output = new StringBuilder();
	        String line;
	        JSONObject adminResultObject = new JSONObject();

	        while ((line = reader.readLine()) != null) {
//	            System.out.println(line);
	              output.append(line);
	        }

	        
	        adminResultObject.put("TestCase", testCase);
	        adminResultObject.put("Output", output);

	        resultList.add(adminResultObject);
	        return resultList;
	    }


}
