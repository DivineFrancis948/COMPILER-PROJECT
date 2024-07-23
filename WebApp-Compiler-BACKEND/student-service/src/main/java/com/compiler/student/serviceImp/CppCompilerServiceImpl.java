package com.compiler.student.serviceImp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.compiler.student.dto.CustomTestDTO;
import com.compiler.student.dto.EmbeddedidDTO;
import com.compiler.student.dto.ExecutionRequest;
import com.compiler.student.dto.Response;
import com.compiler.student.entity.PrgQuestionEntity;
import com.compiler.student.entity.StudentPrgEmbeddedId;
import com.compiler.student.entity.StudentPrgEntity;
import com.compiler.student.exceptions.RecordNotFoundException;
import com.compiler.student.exceptions.SourceCodeNotFoundException;
import com.compiler.student.exceptions.TestCaseFailException;
import com.compiler.student.repository.PrgQuestionRepository;
import com.compiler.student.repository.StudentPrgRepository;
import com.compiler.student.service.CppCompilerService;
import com.compiler.student.util.FileDeletionService;

@Service
@RequestScope
public class CppCompilerServiceImpl implements CppCompilerService {

	@Autowired
	PrgQuestionRepository prgquestion;

	@Autowired
	StudentPrgRepository studentRepo;

	@Value("${vulnerablityCheckFlag}")
	private boolean vulnerablityCheckFlag;

//	private final ExecutorService threadPool = Executors.newFixedThreadPool(1); // Adjust the number of threads as
	private volatile Integer totalMark = 0;
	private final List<JSONObject> summary = Collections.synchronizedList(new ArrayList<>());
	private static Logger logger = LogManager.getLogger(CppCompilerServiceImpl.class);
	Integer testCaseId = 1;

	public Response checkUserCppProgram(EmbeddedidDTO id, UUID randomUUID) throws Exception {
		try {
//			FileDeletionService deletionService = new FileDeletionService();
//            deletionService.deleteFilesWithPrefix(".", "temp");
			StudentPrgEmbeddedId entityid = new StudentPrgEmbeddedId();
			entityid.setQuestionid(id.getQuestionid());
			entityid.setUsername(id.getUsername());
			Optional<StudentPrgEntity> entity = studentRepo.findById(entityid);
			StudentPrgEntity entity1 = new StudentPrgEntity();
			try {
				if (entity != null) {
					entity1 = entity.get();
				} else {
					entity1 = null;
				}
			} catch (NoSuchElementException e) {
				logger.error("No such element present{entity is empty !!} " + e.getMessage());
			}

			try {

				StudentPrgEmbeddedId pId = new StudentPrgEmbeddedId();
				pId.setQuestionid(id.getQuestionid());
				pId.setUsername(id.getUsername());

				Optional<StudentPrgEntity> studentPrg = studentRepo.findById(pId); // exception
				if (studentPrg.isPresent()) {
					StudentPrgEntity prg = studentPrg.get();
					String cppSourceCode = prg.getSolution();

					Response userResults = new Response();
					
					if(cppSourceCode.isBlank()  || cppSourceCode.isEmpty())
					{
						logger.error("Missing source code. Aborting...");
						throw new SourceCodeNotFoundException("Missing source code.Please Save before Submitting. \n\t Aborting...");
					}

//				userResults = executeUserCppProgramUsingThreadPool(cppSourceCode, id.getQuestionid(), "user",
//						randomUUID).get();
					userResults = executeUserCppProgram(cppSourceCode, id.getQuestionid(), "user",
							randomUUID);

//	        userResults = executeUserCppProgram(cppSourceCode, id.getQuestionid(), "user");
//				userResults = null;

					if (userResults == null) {
						userResults = checkUserCppProgramWithRetry(cppSourceCode, id.getQuestionid(),
								"user", randomUUID);
						logger.warn("Entering pipeline...Output of previous execution is null. Retrying...");
						return userResults;
					}
					entity1.setTestCasePassed(totalMark);
					entity1.setTotalmark(totalMark * 10);
					studentRepo.save(entity1);
					totalMark = 0;
					
					return userResults;
				} else {
					// System.out.println("No StudentPrgEntity found for the given ID");
					
					Response errorResponse = new Response();
					errorResponse.setPassed("NO");
					JSONObject err = new JSONObject();
					err.put(entityid, "No StudentPrgEntity found for the given ID");
					errorResponse.setResult(err);
					
					return errorResponse;
				}

			} catch (TestCaseFailException e) {
				logger.error("TLE"+ e.getMessage());
				throw e;
			}
			 catch (SourceCodeNotFoundException e) {
					logger.error("TLE"+ e.getMessage());
					throw e;
				}
		} catch (Exception e) {
			logger.error("Exception Occured"+ e.getMessage());
			throw e;
		}
		
//		return null;
	}

//	private CompletableFuture<Response> executeUserCppProgramUsingThreadPool(String cppSourceCode, String id,
//			String role, UUID randomUUID) {
//		try {
//			return CompletableFuture.supplyAsync(() -> executeUserCppProgram(cppSourceCode, id, role, randomUUID),
//					threadPool);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}

//	 private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//	    private Response checkUserCppProgramWithRetry(String cppSourceCode, String id, String role, UUID randomUUID) {
//	        int maxRetries = 2;
//	        int retryCount = 0;
//	        Response userResults = null;
//
//	        CompletableFuture<Response> future = executeUserCppProgramUsingThreadPool(cppSourceCode, id, role, randomUUID);
//
//	        while (retryCount < maxRetries) {
//	            try {
//	                userResults = future.get(150, TimeUnit.SECONDS);
//	                 
//	            } catch (InterruptedException | ExecutionException | TimeoutException e) {
//	                // Handle exceptions if needed
//	                e.printStackTrace();
//	            }
//
//	            if (userResults != null) {
//	                break;
//	            }
//
//	            retryCount++;
//
//	            if (retryCount < maxRetries) {
//	                // Create a delayed CompletableFuture for the retry
//	                CompletableFuture<Response> delayedFuture = new CompletableFuture<>();
//	                scheduler.schedule(() -> delayedFuture.complete(null), 150, TimeUnit.SECONDS);
//
//	                // Combine the original CompletableFuture and the delayed one
//	                future = future.applyToEither(delayedFuture, result -> result);
//	                
//	            }
//	        }
//
//	        if (userResults == null) {
//	            //System.out.println("All retry attempts failed. Unable to get user results. API Id:" + randomUUID);
//	            // Handle the failure accordingly
//	        }
//
//	        return userResults;
//	    }
//
	private Response checkUserCppProgramWithRetry(String cppSourceCode, String id, String role, UUID randomUUID) {
		try {
			int maxRetries = 2;
			int retryCount = 0;
			Response userResults = null;

			while (retryCount < maxRetries) {
				try {
					userResults = executeUserCppProgram(cppSourceCode, id, role, randomUUID);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (userResults != null) {
					break;
				}

				retryCount++;

				
			}

			if (userResults == null) {
				logger.error("All retry attempts failed. Unable to get user results. :" + userResults);
			}

			return userResults;
		} catch (Exception e) {
			logger.error("Pipeline closed due to execption" + e.getMessage());
			return null;
		}
	}

	private void deleteExistingExecutable(File cppFile) {

		try {
			File existingExecutable = new File(cppFile.getAbsolutePath() + ".exe");
			logger.info("File exists: {}", existingExecutable.exists());
			logger.info("File path: {}", existingExecutable.getAbsolutePath());

			if (existingExecutable.exists()) {
				boolean deleted = existingExecutable.delete();
				logger.info("Executable Deletion result : " + deleted);
			}

			if (cppFile.exists()) {
				boolean deleted = cppFile.delete();
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

	private Response executeUserCppProgram(String cppSourceCode, String id, String role, UUID randomUUID)
			throws TestCaseFailException, RecordNotFoundException {
		File cppFile = null;
		try {
//			resetSharedState();
			totalMark = 0;
			summary.clear();

			String testCases;
			
				testCases = getTestCasesFromDatabase(id);
			
			String uniqueFileName = "temp_" + System.currentTimeMillis() + ".cpp";
			cppFile = new File(uniqueFileName);

//			FileWriter fileWriter = new FileWriter(cppFile);
//			fileWriter.write(cppSourceCode);
//			fileWriter.close();
			
			 try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
		            writer.write(cppSourceCode);
		            logger.info("Write to file successful.");
		        } catch (IOException e) {
		        	 logger.error("Write to file failed." + e.getMessage());
		        }
			boolean isCompiled = false;
			summary.clear();  //to clear if it contains any results
			isCompiled = compileCppCode(cppFile, randomUUID);
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
			role = "user";
			String[] testCaseSets = testCases.split("\n");


			if (vulnerablityCheckFlag) {
				boolean isClean = scanExecutableWithDivineAV(cppFile.getName());
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

			outputIndex.set(0); //setting output index
			for (String testCaseSet : testCaseSets) {
				File executableFile = new File(uniqueFileName + ".exe");
				if (!executableFile.exists()) {
					return null;
				}

				ExecutionResult result;
				try {
					result = executeCompiledBinary(uniqueFileName + ".exe", testCaseSet, role, id);
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

						deleteExistingExecutable(cppFile);  
						break;
					} else {

						response = new Response();
						JSONObject finalResult = new JSONObject();
						finalResult.put("Result", summary);
						response.setResult(finalResult);
						response.setPassed("YES");
					}

				} else {

					logger.fatal("Result from executeusercpp is null...fatal");
					summary.clear();
					response.setCompileError( new StringBuilder("error"));
					response.setPassed("NO");
				}
			}

			summary.clear();

			deleteExistingExecutable(cppFile);// ---------------

			return response;

		} catch (TestCaseFailException e) {
			FileDeletionService deletionService = new FileDeletionService();
            deletionService.deleteFilesWithPrefix(".", cppFile.getName());
			throw e;
		}

		catch (IOException | InterruptedException e) {
			logger.error("Exception: "+ e.getMessage());
			return null;
		}
		finally {
			deleteExistingExecutable(cppFile);
		}

	}

	private String getTestCasesFromDatabase(String id) throws RecordNotFoundException{

		try {
			Optional<PrgQuestionEntity> testcasesDb = prgquestion.findById(id);
			ExecutionRequest request = new ExecutionRequest();
			if (testcasesDb.isPresent()) {
				try {
					testcasesDb.get();
				} catch (Exception e) {
					logger.error("Error:" + e.getMessage(), e);
					e.printStackTrace();
				}
//		BeanUtils.copyProperties(testcasesDb, request); // not working lets see

				request.setTestCase1(testcasesDb.get().getTestcase1());
				request.setTestCase2(testcasesDb.get().getTestcase2());
				request.setTestCase3(testcasesDb.get().getTestcase3());
				request.setTestCase4(testcasesDb.get().getTestcase4());
				request.setTestCase5(testcasesDb.get().getTestcase5());
				request.setTestCase6(testcasesDb.get().getTestcase6());
				request.setTestCase7(testcasesDb.get().getTestcase7());
				request.setTestCase8(testcasesDb.get().getTestcase8());
				request.setTestCase9(testcasesDb.get().getTestcase8());
				request.setTestCase10(testcasesDb.get().getTestcase8());

				String testCases = String.join("\n",
						request.getTestCase1(),
						request.getTestCase2(),
						request.getTestCase3(),
						request.getTestCase4(),
						request.getTestCase5(),
						request.getTestCase6(),
						request.getTestCase7(),
						request.getTestCase8(),
						request.getTestCase9(),
						request.getTestCase10());

//	 //System.out.println( "in get db :"+testCases);

				return testCases;
				}
			else {
				throw new RecordNotFoundException("User not found") ;
			}
			
			
		}catch(RecordNotFoundException e) {
			logger.error("Fetching TestCases Failed" + e.getMessage());
			return null;
		}
		catch (Exception e) {
			logger.error("Fetching TestCases from database failed " + e.getMessage());
			return null;
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




	private static final int TIMEOUT_SECONDS = 4; // Set your desired timeout in seconds

	String uniqueFileName; //need to check whether its neede or not

	private boolean compileCppCode(File cppFile, UUID randomUUID) throws IOException, InterruptedException {
		try {

			ProcessBuilder compileProcessBuilder = new ProcessBuilder("g++", "-o", cppFile.getName() + ".exe",
					cppFile.getPath());
			compileProcessBuilder.redirectErrorStream(true);

			Process compileProcess = compileProcessBuilder.start();
			boolean compilationSuccessful;

			// Start a timer
			long startTime = System.currentTimeMillis();

			int compileExitCode = 1;
			try {

				if (System.currentTimeMillis() - startTime > TIMEOUT_SECONDS * 1000) {
//						// Timeout reached, destroy the process
					compileProcess.destroy();
						logger.error("Compilation timed out.Aborting...");
					return false;
				}
//
//					// Sleep for a short time before checking again
//					Thread.sleep(100);
//				}

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
				logger.error("Compilation Failed.Check Syntax");
				printCompilationError(compileProcess.getInputStream(), "Compilation", "");
				return false;
			}

		} catch (IOException e) {
			logger.error("IOException compilation failed " +  e.getMessage(),e);
			return false;
		} catch (Exception e) {
			logger.error("Exception occured. Compilation Failed");
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

	private boolean executeTestCase(String testCaseLine, String binaryPath, String role, String id, boolean[] isTle)
			throws TestCaseFailException {
		// Set a timeout value in seconds
		int timeoutSeconds = 5;

		try {
			// Create a new ProcessBuilder for each thread
			ProcessBuilder executeProcessBuilder = new ProcessBuilder(binaryPath);
			executeProcessBuilder.redirectErrorStream(true);

			Process executeProcess = executeProcessBuilder.start();

			// Pass the current test case line as input to the C++ program with newline
			executeProcess.getOutputStream().write((testCaseLine + "\n").getBytes());
			executeProcess.getOutputStream().flush();

			boolean processCompleted = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

			if (processCompleted) {
				int executeExitCode = executeProcess.exitValue(); // if not 0 program fails

				if (role.equals("admin") || role.equals("superAdmin")) {
					// printProcessOutput(executeProcess.getInputStream(), "Execution",
					// testCaseLine);
				} else if (role.equals("user")) {
					List<JSONObject> testCaseSummary = printUserProcessOutput(executeProcess.getInputStream(),
							"Execution", testCaseLine, id);

//            synchronized (summary) {     //-------------------->>>
//                summary.addAll(testCaseSummary);
//            }
					summary.addAll(testCaseSummary);
				} else {
					logger.fatal("Fatal error: neither user nor admin");
					//type code for immediate logout and warn admin about such activity.
				}
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

	private CompletableFuture<Boolean> executePgm(String testCaseLine, String binaryPath, String role, String id,
			boolean[] isTle) throws TestCaseFailException {
		try {
			boolean res = executeTestCase(testCaseLine, binaryPath, role, id, isTle);

			return CompletableFuture.completedFuture(res); // will create new threads on call
		} catch (TestCaseFailException e) {
			logger.error("TestCaseFailedException:"+ e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Execution and Thread Creation Failed"+ e.getMessage());
		}
		return null;
	}

	private ExecutionResult executeCompiledBinary(String binaryPath, String testCases, String role, String id)
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
				threads.add(executePgm(testCaseLine, binaryPath, role, id, isTle));

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
	
	

//	private void resetSharedState() {
////	        sharedStateLock.writeLock().lock();
//		try {
//			outputIndex.set(0);
//			summary.clear();
//		} finally {
////	            sharedStateLock.writeLock().unlock();
//		}
//	}

//private volatile Integer outputIndex = 0;
//	 private final ReentrantReadWriteLock sharedStateLock = new ReentrantReadWriteLock();
//	    private volatile Integer totalMarks = 0;
	private final AtomicInteger outputIndex = new AtomicInteger(0);

	private List<JSONObject> printUserProcessOutput(InputStream inputStream, String type, String testCase, String id)
			throws IOException {
		try {
//	            Object outputLock = new Object();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			Map<Integer, String> outputMap = new ConcurrentHashMap<>();
			List<JSONObject> summary = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
//	                synchronized (outputLock) {
				int currentIndex = outputIndex.incrementAndGet();
				outputMap.put(currentIndex, line);
//	                }

				List<JSONObject> threadSummary;
//	                sharedStateLock.readLock().lock();
				try {
					threadSummary = generateSummary(id, new HashMap<>(outputMap));
				} finally {
//	                    sharedStateLock.readLock().unlock();
				}

//	                sharedStateLock.writeLock().lock();
				try {
					outputMap.clear();
				} finally {
//	                    sharedStateLock.writeLock().unlock();
				}

				summary.addAll(threadSummary);
			}

//	            sharedStateLock.writeLock().lock();
			try {
				outputIndex.set((outputIndex.get() == 10) ? 0 : outputIndex.get());
//	                totalMarks = (totalMarks >= 10) ? 0 : totalMarks;
			} finally {
//	                sharedStateLock.writeLock().unlock();
			}

			return summary;
		} catch (Exception e) {
			logger.error("Error in printing User Process Output ", e.getMessage(),e);
			return null;
		}
	}

	private synchronized List<JSONObject> generateSummary(String id, HashMap<Integer, String> dbOutputMap) {
		try {
			List<JSONObject> summary = new ArrayList<>();
			Integer userTestCaseId = 1;
			boolean allTestCasesPassed = true; // 11.45

			for (Map.Entry<Integer, String> entry : dbOutputMap.entrySet()) {
				Integer currentOutputIndex = entry.getKey();
				String currentOutput = entry.getValue();

				String dbOutputSet = getOutputsFromDatabase(id, currentOutputIndex);

//	                 

				boolean testCasePassed = compareUserOutputWithExpectedOutput(currentOutput, dbOutputSet);

				JSONObject testCaseResponse = new JSONObject();

				if (testCasePassed) {
//					totalMarksThreadLocal.set(totalMarksThreadLocal.get() + 1);
//					System.out.println(totalMarksThreadLocal.get());
//					totalMark += totalMarksThreadLocal.get();
					++totalMark;
//	                	 System.out.println(totalMark); //has some issues will address later------------------>
					testCaseResponse.put("TestCase" + currentOutputIndex, "Passed");

				} else {
					testCaseResponse.put("TestCase" + currentOutputIndex, "Failed");
					allTestCasesPassed = false; // Update this flag for all test cases
				}

//	                synchronized (summary) {
				summary.add(testCaseResponse);
//	                }
			}

	            if (totalMark == 10) {
	                JSONObject acceptanceResponse = new JSONObject();
	                acceptanceResponse.put("All testcases Passed", "Accepted");
	                summary.add(acceptanceResponse);

	            }

			return summary;
		} catch (Exception e) {
			logger.error("Failed to generate summary",e.getMessage(),e);
			return null;
		}
	}

	private String getOutputsFromDatabase(String id, Integer currentOutputIndex) throws RecordNotFoundException {

		try {
			Optional<PrgQuestionEntity> testcasesDb = prgquestion.findById(id);
			if (testcasesDb.isPresent()) {
				try {
					testcasesDb.get();
				} catch (Exception e) {
					logger.error("Fetching Output Failed" , e.getMessage(), e);
				}

				switch (currentOutputIndex) {
					case 1:
						return testcasesDb.get().getTestcase1A();
					case 2:
						return testcasesDb.get().getTestcase2A();
					case 3:
						return testcasesDb.get().getTestcase3A();
					case 4:
						return testcasesDb.get().getTestcase4A();
					case 5:
						return testcasesDb.get().getTestcase5A();
					case 6:
						return testcasesDb.get().getTestcase6A();
					case 7:
						return testcasesDb.get().getTestcase7A();
					case 8:
						return testcasesDb.get().getTestcase8A();
					case 9:
						return testcasesDb.get().getTestcase8A();
					case 10:
						return testcasesDb.get().getTestcase8A();
					default:
						return null;
				}
			} else {
				
				throw new RecordNotFoundException("Output Not Found in db");
			}
		} catch (Exception e) {
			logger.error("Failed to get testcases from db ", e.getMessage(),e);
			return null;
		}
	}

	private boolean compareUserOutputWithExpectedOutput(String userOutput, String expectedOutput) {
		try {
			// Split the user and expected output into lines
			String[] userLines = userOutput.split("\\r?\\n");
			String[] expectedLines = expectedOutput.split("\\r?\\n");

			// Compare each line
			for (int i = 0; i < Math.min(userLines.length, expectedLines.length); i++) {
				if (!userLines[i].trim().equals(expectedLines[i].trim())) {
					// If any line differs, return false
					return false;
				}
			}

			// If the number of lines is different, return false
			return userLines.length == expectedLines.length;
		} catch (Exception e) {
			logger.error("Error in comparing outputs "+ e.getMessage());
			return false;
		}
	}

	public Response checkCustomUserCppProgram(CustomTestDTO dto,UUID randomUUID) throws TestCaseFailException {
		
			try {
				FileDeletionService deletionService = new FileDeletionService();
				deletionService.deleteFilesWithPrefix(".", "temp");
				String cppSourceCode = dto.getProgram();
				String testCase = dto.getInput();
				String role = "user";
				if(cppSourceCode == null || testCase == null || role == null)
				{
					logger.error("some of the required fields in custominput is missing");
					return null; //need to address later
				}
				
				Response response = executeCustomUserCppProgram(cppSourceCode,testCase,role,randomUUID);
				JSONObject a = new JSONObject();
				a = response.getResult();		
				return response;
			} catch (TestCaseFailException e) {
				throw e;
			}
			catch(Exception e)
			{
				throw e;
			}
		}
	
	
	private Response executeCustomUserCppProgram(String cppSourceCode, String testCases, String role, UUID randomUUID)
			throws TestCaseFailException {
		try {
			File cppFile;
			summary.clear();

			String uniqueFileName = "temp_" + System.currentTimeMillis() + ".cpp";
			cppFile = new File(uniqueFileName);

			 try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
		            writer.write(cppSourceCode);
		            logger.info("Write to file successful.");
		        } catch (IOException e) {
		        	 logger.error("Write to file failed." + e.getMessage());
		        }
			boolean isCompiled = false;
			customOutput = new  StringBuilder(); //to clear if it contains any results
			isCompiled = compileCppCode(cppFile, randomUUID);
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
			role = "user";
			String[] testCaseSets = testCases.split("\n");


			if (vulnerablityCheckFlag) {
				boolean isClean = scanExecutableWithDivineAV(cppFile.getName());
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

			outputIndex.set(0); //setting output index
			for (String testCaseSet : testCaseSets) {
				File executableFile = new File(uniqueFileName + ".exe");
				if (!executableFile.exists()) {
					return null;
				}

				CustomExecutionResult result;
				try {
					result = executeCustomCompiledBinary(uniqueFileName + ".exe", testCaseSet, role);
				} catch (TestCaseFailException e) {
					logger.info("Time Limit Exceeded.. "+e.getMessage());
					throw e;
				}

				catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				if (result != null) {
					boolean isTle = result.isTle();
					 customOutput = result.getSummary();
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

						deleteExistingExecutable(cppFile);  
						break;
					} else {

						response = new Response();
						JSONObject finalResult = new JSONObject();
						finalResult.put("Result", customOutput);
						response.setResult(finalResult);
						response.setPassed("YES");
					}

				} else {

					logger.fatal("Result from executeusercpp is null...fatal");
					response.setCompileError( new StringBuilder("error"));
					response.setPassed("NO");
				}
			}


			deleteExistingExecutable(cppFile);// ---------------

			return response;

		} catch (TestCaseFailException e) {
			throw e;
		}
		catch (IOException | InterruptedException e) {
			logger.error("Exception: "+ e.getMessage());
			return null;
		}
		catch(Exception e){
			logger.error("Exception Occured" + e.getMessage());
			throw e;
		}

	}
	private CustomExecutionResult executeCustomCompiledBinary(String binaryPath, String testCases, String role)
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
				threads.add(executeCustomPgm(testCaseLine, binaryPath, role, isTle));

			}

			CompletableFuture.allOf(toCfArray(threads));
			return new CustomExecutionResult(isTle[0], new StringBuilder(customOutput));
		} catch (TestCaseFailException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error("Failed to execute binary " + e.getMessage());
			throw e;
		}
	}
		

	private CompletableFuture<Boolean> executeCustomPgm(String testCaseLine, String binaryPath, String role,
			boolean[] isTle) throws TestCaseFailException {
		try {
			boolean res = executeCustomTestCase(testCaseLine, binaryPath, role,  isTle);

			return CompletableFuture.completedFuture(res); // will create new threads on call
		} catch (TestCaseFailException e) {
			logger.error("TestCaseFailedException: "+ e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Execution and Thread Creation Failed "+ e.getMessage());
		}
		return null;
	}
	
	
	StringBuilder customOutput= new  StringBuilder();
	private boolean executeCustomTestCase(String testCaseLine, String binaryPath, String role, boolean[] isTle)
			throws Exception {
		// Set a timeout value in seconds
		int timeoutSeconds = 5;

		try {
			// Create a new ProcessBuilder for each thread
			ProcessBuilder executeProcessBuilder = new ProcessBuilder(binaryPath);
			executeProcessBuilder.redirectErrorStream(true);

			Process executeProcess = executeProcessBuilder.start();

			// Pass the current test case line as input to the C++ program with newline
			executeProcess.getOutputStream().write((testCaseLine + "\n").getBytes());
			executeProcess.getOutputStream().flush();

			boolean processCompleted = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

			if (processCompleted) {
				int executeExitCode = executeProcess.exitValue(); // if not 0 program fails

			
				 if (role.equals("user")) {
					customOutput = printCustomUserProcessOutput(executeProcess.getInputStream(),
							"Execution", testCaseLine);

				} else {
					logger.fatal("Fatal error: neither user nor admin");
					//type code for immediate logout and warn admin about such an activity.
				}
			} else {
				logger.warn("Process execution timed out");

				// Destroy the external process explicitly
				executeProcess.destroy();

				// Wait for the process to terminate
				boolean processTerminated = executeProcess.waitFor(timeoutSeconds, TimeUnit.SECONDS);

				if (!processTerminated) {
					executeProcess.destroyForcibly();
				}
				throw new TestCaseFailException("Time limit exceeded");
			}
			return true;
		} catch (TestCaseFailException e) {
			logger.error("Time Limit Exceeded..Breaking Exceution:" +e.getMessage());
			throw e;
		}

		catch (Exception e) {
			logger.error("Execution Failed "+e.getMessage());
			throw e;
		}
	}
	
	private StringBuilder printCustomUserProcessOutput(InputStream inputStream, String type, String testCase)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder customOutput = new StringBuilder();
			while ((line = reader.readLine()) != null) {		
				customOutput.append(line);
			}
			return customOutput;
		} catch (Exception e) {
			logger.error("Error in printing User Process Output "+ e.getMessage());
			return null;
		}
	}

	
	
	

}
