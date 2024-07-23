package com.compiler.question.serviceImp;

import java.util.Date;
import java.util.Iterator;
import java.math.BigInteger;


import java.text.SimpleDateFormat;
//import java.util.Collections;
////import java.util.List;
//import java.util.Optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.compiler.question.dto.McqQuestionDTO;
import com.compiler.question.dto.PrgQuestionDTO;
import com.compiler.question.dto.QuestionDTO;
import com.compiler.question.dto.ServiceResponse;
import com.compiler.question.entity.McqQuestionEntity;
import com.compiler.question.entity.PrgQuestionEntity;
import com.compiler.question.entity.QuestionEntity;
import com.compiler.question.entity.RegistrationEntity;
import com.compiler.question.entity.SelectedQuestionEntity;
import com.compiler.question.entity.StudentMcqEmbedded;
import com.compiler.question.entity.StudentMcqEntity;
import com.compiler.question.entity.StudentPrgEntity;
import com.compiler.question.repository.McqQuestionRepository;
import com.compiler.question.repository.PrgQuestionRepository;
import com.compiler.question.repository.QuestionRepository;
import com.compiler.question.repository.RegistrationRepository;
import com.compiler.question.repository.SelectedQuestionRepository;
import com.compiler.question.repository.StudentMcqRepository;
import com.compiler.question.repository.StudentPrgRepository;
import com.compiler.question.service.QuestionService;
import com.compiler.question.util.Constants;

@Service
public class QuestionServiceImp implements QuestionService
{
	private static Logger logger = LogManager.getLogger(QuestionServiceImp.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	RegistrationRepository regrepo;
	
	@Autowired
	QuestionRepository qesrepo;
	
	@Autowired
	PrgQuestionRepository prgrepo;
	
	@Autowired
	SelectedQuestionRepository selectedQuestionRepository;
	
	@Autowired
	McqQuestionRepository mcqQuestionRepository;
	
	@Autowired
	StudentPrgRepository studprgrepo;
	
	@Autowired
	StudentMcqRepository studentMcqRepository;
	
	public JSONObject searchfor() {
		JSONObject result = new JSONObject();
		try {
//			PageRequest pageable = PageRequest.of(start / pageSize, pageSize);
//			Specification<EntityClass> spec = SecurityUserSpec.getUserSpec(searchParam);
			List<QuestionEntity> usersList = qesrepo.findAll();
			JSONArray array = new JSONArray();
//			JSONArray countByStatus = countByStatus(spec);
			for (QuestionEntity users : usersList) {
				JSONObject obj = new JSONObject();
				obj.put("Questionid", users.getQuestionid());
				Optional<PrgQuestionEntity> num = prgrepo.findById(users.getQuestionid());
				if (num.isPresent()) {
					PrgQuestionEntity question = num.get();
					obj.put("Questionsol", question.getSolution());
					obj.put("BasicStruct", question.getBasic());
					obj.put("Testcase1", question.getTestcase1());
					obj.put("Testcase1A", question.getTestcase1A());
					obj.put("Testcase2", question.getTestcase2());
					obj.put("Testcase2A", question.getTestcase2A());
					obj.put("Testcase3", question.getTestcase3());
					obj.put("Testcase3A", question.getTestcase3A());
				}
				obj.put("QuestionType", users.getQuestiontype());
				obj.put("Question", users.getQuestion());
				array.add(obj);
			}
			result.put("aaData", array);
			result.put("iTotalDisplayRecords", regrepo.findAll().size());
			result.put("iTotalRecords", regrepo.findAll().size());
//			result.put("countByStatus", countByStatus);
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
		}
		return result;
	}
	
	public ServiceResponse addQuestionDetails(QuestionDTO quesdto) {
		try {
			Optional<RegistrationEntity> optionalEntity = regrepo.findById(quesdto.getUsername());

			if (optionalEntity.isPresent()) {
				RegistrationEntity regdetails = optionalEntity.get();

				QuestionEntity entity = new QuestionEntity();
				String id = findLargestId(quesdto.getQuestiontype());
				entity.setUsername(quesdto.getUsername());
				entity.setQuestionid(findLargestId(quesdto.getQuestiontype()));
				entity.setQuestion(quesdto.getQuestion());
				entity.setQuestiontype(quesdto.getQuestiontype());

//				entity.setCreatedDate(new Date());
				entity.setStatus("PROCESSD");

				if (quesdto.getQuestiontype().equals("MCQ")) {
					entity.setQuestionHeading(quesdto.getQuestion());

				} else {
					entity.setQuestionHeading(quesdto.getQuestionHeading());

				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = dateFormat.format(new Date());
				entity.setCreatedDate(dateString); // If using java.util.Date;

				if (regdetails.getNumberOfQuestions() == null) {
					regdetails.setNumberOfQuestions("1");
				} else {
					int noofquestions;
					noofquestions = Integer.parseInt(regdetails.getNumberOfQuestions());
					noofquestions = noofquestions + 1;
					regdetails.setNumberOfQuestions(Integer.toString(noofquestions));
				}
				qesrepo.save(entity);
				regrepo.save(regdetails);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, id, null);

			} else {
				return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);

			} // IF ELSE ENDS HERE

		} // TRY ENDS HERE
		catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);

		} // TRY CARCH ENDS HERE
	}
	
	
	public ServiceResponse addMCQQuestionDetails(McqQuestionDTO mcqquesdto) {
		try {

			McqQuestionEntity entity = new McqQuestionEntity();

			entity.setQuestionId(mcqquesdto.getQuestionId());
			entity.setOption1(mcqquesdto.getOption1());
			entity.setOption2(mcqquesdto.getOption2());
			entity.setOption3(mcqquesdto.getOption3());
			entity.setOption4(mcqquesdto.getOption4());

			entity.setAnswer(mcqquesdto.getAnswer());

			mcqQuestionRepository.save(entity);

			return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.QUESTION_ADDED, null);

		} // TRY ENDS HERE
		catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);

		} // TRY CARCH ENDS HERE
	}
	
	public ServiceResponse addPRGQuestionDetails(PrgQuestionDTO prgquesdto) {
		Optional<QuestionEntity> optionalEntity = qesrepo.findById(prgquesdto.getQuestionId());
		try {
			if (optionalEntity.isPresent()) {
				PrgQuestionEntity entity = new PrgQuestionEntity();
				entity.setQuestionId(prgquesdto.getQuestionId());
				entity.setBasic(prgquesdto.getBasic());
				entity.setLanguage(prgquesdto.getLanguage());
				entity.setSolution(prgquesdto.getSolution());
				entity.setInputs(prgquesdto.getInputs());
				entity.setTestcase1(prgquesdto.getTestcase1());
				entity.setTestcase1A(prgquesdto.getTestcase1A());
				entity.setTestcase2(prgquesdto.getTestcase2());
				entity.setTestcase2A(prgquesdto.getTestcase2A());
				entity.setTestcase3(prgquesdto.getTestcase3());
				entity.setTestcase3A(prgquesdto.getTestcase3A());
				entity.setTestcase4(prgquesdto.getTestcase4());
				entity.setTestcase4A(prgquesdto.getTestcase4A());
				entity.setTestcase5(prgquesdto.getTestcase5());
				entity.setTestcase5A(prgquesdto.getTestcase5A());
				entity.setTestcase6(prgquesdto.getTestcase6());
				entity.setTestcase6A(prgquesdto.getTestcase6A());
				entity.setTestcase7(prgquesdto.getTestcase7());
				entity.setTestcase7A(prgquesdto.getTestcase7A());
				entity.setTestcase8(prgquesdto.getTestcase8());
				entity.setTestcase8A(prgquesdto.getTestcase8A());
				entity.setTestcase9(prgquesdto.getTestcase9());
				entity.setTestcase9A(prgquesdto.getTestcase9A());
				entity.setTestcase10(prgquesdto.getTestcase10());
				entity.setTestcase10A(prgquesdto.getTestcase10A());
				prgrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.QUESTION_ADDED_TESTCASES, null);

			} else {

				return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);

			}
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);

		} // TRY CARCH ENDS HERE
	}
	
	
	public JSONObject getAnyfiveQuestions(String userName) {
		JSONObject result = new JSONObject();
		try {
			Optional<SelectedQuestionEntity> user = selectedQuestionRepository.findById(userName);
			if (user.isPresent()) {
				SelectedQuestionEntity selectedQuestionEntity = user.get();
				if (selectedQuestionEntity.getSelectedQuestionId() == null) {
					List<QuestionEntity> randomQuestions = null;
					// Fetch all questions from the database
					List<QuestionEntity> allQuestions = qesrepo.findByQuestiontype("PRG");
					// Shuffle the list to get a random order
					Collections.shuffle(allQuestions);
//					System.out.println(allQuestions);
					// Take only the first 5 questions (or fewer if there are fewer than 5 questions
					// in the database)
					randomQuestions = allQuestions.subList(0, Math.min(5, allQuestions.size()));
					ArrayList<String> selectedQuestionsList = new ArrayList<>();
//			            SelectedQuestionEntity selectedQuestionEntity = new SelectedQuestionEntity();
//			            selectedQuestionEntity.setUserName(userName);

					JSONArray array = new JSONArray();
					for (QuestionEntity question : randomQuestions) {
						JSONObject obj = new JSONObject();
						selectedQuestionsList.add(question.getQuestionid());
						obj.put("Questionid", question.getQuestionid());
						obj.put("QuestionHeading", question.getQuestionHeading());
						obj.put("QuestionType", question.getQuestiontype());
						obj.put("Question", question.getQuestion());
						obj.put("QuestionStatus", "Solve Question");
						array.add(obj);
					}
//					System.out.println(selectedQuestionsList);

					selectedQuestionEntity.setSelectedQuestionId(selectedQuestionsList);
					selectedQuestionRepository.save(selectedQuestionEntity);

					result.put("aaData", array);
					result.put("iTotalDisplayRecords", randomQuestions.size());
					result.put("iTotalRecords", randomQuestions.size());

				} else {
					List<QuestionDTO> randomQuestions = new ArrayList<>();

					SelectedQuestionEntity existingUsers = user.get();

					List<String> questionIds = existingUsers.getSelectedQuestionId();

					Optional<QuestionEntity> question;
					QuestionEntity existingQuestion;

					for (String id : questionIds) {
						QuestionDTO questionDTO = new QuestionDTO();
						question = qesrepo.findById(id);
						existingQuestion = question.get();
						questionDTO.setQuestionid(id);
						questionDTO.setQuestionHeading(existingQuestion.getQuestionHeading());
						questionDTO.setQuestion(existingQuestion.getQuestion());
						questionDTO.setQuestiontype(existingQuestion.getQuestiontype());
						randomQuestions.add(questionDTO);
					}

					JSONArray array = new JSONArray();
					for (QuestionDTO questions : randomQuestions) {
						JSONObject obj = new JSONObject();
						obj.put("Questionid", questions.getQuestionid());
						obj.put("QuestionHeading", questions.getQuestionHeading());
						obj.put("QuestionType", questions.getQuestiontype());
						obj.put("Question", questions.getQuestion());
						obj.put("QuestionStatus", questions.getStatus());
						array.add(obj);

					}

					result.put("aaData", array);
					result.put("iTotalDisplayRecords", randomQuestions.size());
					result.put("iTotalRecords", randomQuestions.size());
				}

//				

			} else {

				List<QuestionEntity> randomQuestions = null;
				// Fetch all questions from the database
				List<QuestionEntity> allQuestions = qesrepo.findByQuestiontype("PRG");

				// Shuffle the list to get a random order
				Collections.shuffle(allQuestions);
//				System.out.println(allQuestions);
				// Take only the first 5 questions (or fewer if there are fewer than 5 questions
				// in the database)
				randomQuestions = allQuestions.subList(0, Math.min(5, allQuestions.size()));

				ArrayList<String> selectedQuestionsList = new ArrayList<>();
				SelectedQuestionEntity selectedQuestionEntity = new SelectedQuestionEntity();
				selectedQuestionEntity.setUserName(userName);

				JSONArray array = new JSONArray();
				for (QuestionEntity question : randomQuestions) {
					JSONObject obj = new JSONObject();
					selectedQuestionsList.add(question.getQuestionid());
					obj.put("QuestionHeading", question.getQuestionHeading());
					obj.put("Questionid", question.getQuestionid());
					obj.put("QuestionType", question.getQuestiontype());
					obj.put("Question", question.getQuestion());
					obj.put("QuestionStatus", "Solve Question");

					array.add(obj);
				}
//				System.out.println(selectedQuestionsList);

				selectedQuestionEntity.setSelectedQuestionId(selectedQuestionsList);
				selectedQuestionEntity.setTimer(60);
				selectedQuestionEntity.setTabChanged(0);
				selectedQuestionRepository.save(selectedQuestionEntity);

				result.put("aaData", array);
				result.put("iTotalDisplayRecords", randomQuestions.size());
				result.put("iTotalRecords", randomQuestions.size());

			}

		} catch (Exception e) {
			// Handle the exception
			logger.error("Error:" + e.getMessage(), e);
		}
		return result;
	}
	
	// FUNTION TO AUTOGENERATE THE QUESTION ID
	private String findLargestId(String type) {
		try {
			Long num = qesrepo.countByQuestiontype(type) + 1;
			String stringid = type + String.valueOf(num);
			return stringid;
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return null; // or throw a custom exception or return a default value based on your
							// requirements
		}
	}

    
    //FUNCRTION TO RETURN ALL THE TEST CASES
    private String findallTestCases(String questionid)
    {
    	Optional<PrgQuestionEntity> num=prgrepo.findById(questionid);
    	PrgQuestionEntity request=new PrgQuestionEntity();
    	String testCases = String.join("\n",
                request.getTestcase1(),
                request.getTestcase2(),
                request.getTestcase3(),
                request.getTestcase4(),
                request.getTestcase5(),
                request.getTestcase6(),
                request.getTestcase7()
        );
    	return testCases;
    }

	@Override
	public QuestionDTO getMcqWithoutOption(String questionId) {

		QuestionDTO dto = new QuestionDTO();

		try {
			Optional<QuestionEntity> entity1 = qesrepo.findById(questionId);

			if (entity1.isPresent()) {
				QuestionEntity entity = entity1.get();

				dto.setQuestionid(questionId);
				dto.setQuestiontype(entity.getQuestiontype());
				dto.setQuestionHeading(entity.getQuestionHeading());
				dto.setQuestion(entity.getQuestion());
				dto.setUsername(entity.getUsername());

			}

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);

		}

		return dto;
	}

	@Override
	public JSONObject getRandomMcqQuestion(String studentId) {

		JSONObject result = new JSONObject();
		try {
//			System.out.println("hello");
			Optional<SelectedQuestionEntity> user = selectedQuestionRepository.findById(studentId);
			StudentMcqEmbedded studentMcqEmbedded = new StudentMcqEmbedded();
			StudentMcqEntity studentMcqEntity = new StudentMcqEntity();
//			System.out.println("Is Present");

			if (user.isPresent()) {
//				System.out.println("Is Present");
				SelectedQuestionEntity selectedQuestionEntity = user.get();

				if (selectedQuestionEntity.getSelectedMcqQuestionId() == null) {
					List<QuestionEntity> randomQuestions = null;
					// Fetch all questions from the database
					List<QuestionEntity> allQuestions = qesrepo.findByQuestiontype("MCQ");
					// Shuffle the list to get a random order
					Collections.shuffle(allQuestions);
//					System.out.println(allQuestions);
					// Take only the first 5 questions (or fewer if there are fewer than 5 questions
					// in the database)
					randomQuestions = allQuestions.subList(0, Math.min(15, allQuestions.size()));

					ArrayList<String> selectedQuestionsList = new ArrayList<>();
//			            selectedQuestionEntity.setUserName(studentId);

					JSONArray array = new JSONArray();
					for (QuestionEntity question : randomQuestions) {
						JSONObject obj = new JSONObject();
						selectedQuestionsList.add(question.getQuestionid());

						studentMcqEmbedded.setUserName(studentId);
			            studentMcqEmbedded.setQuestionId(question.getQuestionid());
			            studentMcqEntity.setStudentMcqEmbedded(studentMcqEmbedded);
			            studentMcqEntity.setMcqStatus("U");
			            studentMcqRepository.save(studentMcqEntity);
						
						obj.put("Questionid", question.getQuestionid());
						obj.put("QuestionType", question.getQuestiontype());
						obj.put("Question", question.getQuestion());
						array.add(obj);
					}

					selectedQuestionEntity.setSelectedMcqQuestionId(selectedQuestionsList);
					selectedQuestionRepository.save(selectedQuestionEntity);
					result.put("aaData", array);
					result.put("iTotalDisplayRecords", randomQuestions.size());
					result.put("iTotalRecords", randomQuestions.size());

				} else {

					List<QuestionDTO> randomQuestions = new ArrayList<>();

					SelectedQuestionEntity existingUsers = user.get();

					List<String> questionIds = existingUsers.getSelectedMcqQuestionId();

					Optional<QuestionEntity> question;
					QuestionEntity existingQuestion;

					for (String id : questionIds) {
						QuestionDTO questionDTO = new QuestionDTO();
						question = qesrepo.findById(id);
						existingQuestion = question.get();
						questionDTO.setQuestionid(id);
						questionDTO.setQuestion(existingQuestion.getQuestion());
						questionDTO.setQuestiontype(existingQuestion.getQuestiontype());
						randomQuestions.add(questionDTO);
					}

//					
					JSONArray array = new JSONArray();
					for (QuestionDTO questions : randomQuestions) {
						JSONObject obj = new JSONObject();

						obj.put("Questionid", questions.getQuestionid());
						obj.put("QuestionType", questions.getQuestiontype());
						obj.put("Question", questions.getQuestion());
						array.add(obj);

					}
					result.put("aaData", array);
					result.put("iTotalDisplayRecords", "MCQ");
					result.put("iTotalRecords", randomQuestions.size());

				}

			} else {

				List<QuestionEntity> randomQuestions = null;
				// Fetch all questions from the database
				List<QuestionEntity> allQuestions = qesrepo.findByQuestiontype("MCQ");
				// Shuffle the list to get a random order
				Collections.shuffle(allQuestions);
//				System.out.println(allQuestions);
				// Take only the first 5 questions (or fewer if there are fewer than 5 questions
				// in the database)
				randomQuestions = allQuestions.subList(0, Math.min(15, allQuestions.size()));

				ArrayList<String> selectedQuestionsList = new ArrayList<>();
				SelectedQuestionEntity selectedQuestionEntity = new SelectedQuestionEntity();
				selectedQuestionEntity.setUserName(studentId);

				JSONArray array = new JSONArray();
				for (QuestionEntity question : randomQuestions) {
					JSONObject obj = new JSONObject();
					selectedQuestionsList.add(question.getQuestionid());

					studentMcqEmbedded.setUserName(studentId);
		            studentMcqEmbedded.setQuestionId(question.getQuestionid());
		            studentMcqEntity.setStudentMcqEmbedded(studentMcqEmbedded);
		            studentMcqEntity.setMcqStatus("U");
		            studentMcqRepository.save(studentMcqEntity);
					
					obj.put("Questionid", question.getQuestionid());
					obj.put("QuestionType", question.getQuestiontype());
					obj.put("Question", question.getQuestion());
					array.add(obj);
				}

				selectedQuestionEntity.setSelectedMcqQuestionId(selectedQuestionsList);
				selectedQuestionEntity.setTabChanged(0);
				selectedQuestionEntity.setTimer(60);
				selectedQuestionRepository.save(selectedQuestionEntity);
				result.put("aaData", array);
				result.put("iTotalDisplayRecords", randomQuestions.size());
				result.put("iTotalRecords", randomQuestions.size());

			}

		} catch (Exception e) {
			// Handle the exception
			logger.error("Error:" + e.getMessage(), e);
		}
		return result;
	}

	@Override
	public McqQuestionDTO getMcqFullWithOptions(String questionId) {

		McqQuestionDTO dto = new McqQuestionDTO();

		try {

			Optional<McqQuestionEntity> entity1 = mcqQuestionRepository.findById(questionId);

			if (entity1.isPresent()) {
				McqQuestionEntity entity = entity1.get();

				dto.setQuestionId(questionId);
				dto.setOption1(entity.getOption1());
				dto.setOption2(entity.getOption2());
				dto.setOption3(entity.getOption3());
				dto.setOption4(entity.getOption4());

			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		}

		return dto;
	}
	
	
	public JSONObject getallprgQuestions(String userName) {
		JSONObject result = new JSONObject();
		try {
			List<PrgQuestionEntity> user = prgrepo.findAll();
			ArrayList<String> selectedQuestionsList = new ArrayList<>();

			JSONArray array = new JSONArray();
			for (PrgQuestionEntity question : user) {
				JSONObject obj = new JSONObject();
				obj.put("Questionid", question.getQuestionId());
				Optional<QuestionEntity> questionentity = qesrepo.findById(question.getQuestionId());
				QuestionEntity questiondetails = questionentity.get();
				obj.put("QuestionHeading", questiondetails.getQuestionHeading());
				obj.put("QuestionType", questiondetails.getQuestiontype());
				obj.put("Question", questiondetails.getQuestion());
				array.add(obj);
			}
//			System.out.println(selectedQuestionsList);

			result.put("aaData", array);

		} catch (Exception e) {
			// Handle the exception
			logger.error("Error:" + e.getMessage(), e);
		}
		return result;
	}
	
	public JSONObject getTestCases(String questionid) {
		JSONObject obj = new JSONObject();
		try {
			Optional<PrgQuestionEntity> user = prgrepo.findById(questionid);
			PrgQuestionEntity details = user.get();
			obj.put("Testcase01", details.getTestcase1());
			obj.put("Testcase02", details.getTestcase2());
			obj.put("Testcase03", details.getTestcase3());
			obj.put("Testcase04", details.getTestcase4());
			obj.put("Testcase05", details.getTestcase5());
			obj.put("Testcase06", details.getTestcase6());
			obj.put("Testcase07", details.getTestcase7());
			obj.put("Testcase08", details.getTestcase8());
			obj.put("Testcase09", details.getTestcase9());
			obj.put("Testcase10", details.getTestcase10());
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
		}
		return obj;
	}

	
	public ServiceResponse updateQuestionDetails(QuestionDTO quesdto) {
		try {
			Optional<QuestionEntity> optionalEntity = qesrepo.findById(quesdto.getQuestionid());
			if (optionalEntity.isPresent()) {
				QuestionEntity entity = new QuestionEntity();

				entity = optionalEntity.get();

				entity.setQuestion(quesdto.getQuestion());
				qesrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.QUESTION_ADDED, null);
			} else {
				return new ServiceResponse(Constants.MESSAGE_STATUS.fail,  Constants.QUESTION.QUESTION_NOT_FOUND, null);
			} // IF ELSE ENDS HERE
		} // TRY ENDS HERE
		catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);
		}

	}
	@Override
	public ServiceResponse updateMCQQuestionDetails(McqQuestionDTO mcqquesdto) {
		
		Optional<McqQuestionEntity> optionalEntity = mcqQuestionRepository.findById(mcqquesdto.getQuestionId());
		
		try
		{	
			if(optionalEntity.isPresent()) {
				McqQuestionEntity entity = new McqQuestionEntity();
				
				entity.setQuestionId(mcqquesdto.getQuestionId());
				entity.setOption1(mcqquesdto.getOption1());
				entity.setOption2(mcqquesdto.getOption2());
				entity.setOption3(mcqquesdto.getOption3());
				entity.setOption4(mcqquesdto.getOption4());
				entity.setAnswer(mcqquesdto.getAnswer());
				mcqQuestionRepository.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.QUESTION_OPTIONS,null);
				
			}else {
				return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.QUESTION.QUESTION_OPTIONS_NOT,null);
			}
		} //TRY ENDS HERE
		catch(Exception e)
		{
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.QUESTION.QUESTION_ADDED,null);
		}
	}

	@Override
	public ServiceResponse updatePRGQuestionDetails(PrgQuestionDTO prgQuestionDTO) {
		Optional<PrgQuestionEntity> optionalEntity = prgrepo.findById(prgQuestionDTO.getQuestionId());
		try {
			if (optionalEntity.isPresent()) {
				PrgQuestionEntity entity = new PrgQuestionEntity();

				entity = optionalEntity.get();

				entity.setQuestionId(prgQuestionDTO.getQuestionId());
				entity.setBasic(prgQuestionDTO.getBasic());
				entity.setLanguage(prgQuestionDTO.getLanguage());
				entity.setSolution(prgQuestionDTO.getSolution());
				entity.setInputs(prgQuestionDTO.getInputs());
				entity.setTestcase1(prgQuestionDTO.getTestcase1());
				entity.setTestcase1A(prgQuestionDTO.getTestcase1A());
				entity.setTestcase2(prgQuestionDTO.getTestcase2());
				entity.setTestcase2A(prgQuestionDTO.getTestcase2A());
				entity.setTestcase3(prgQuestionDTO.getTestcase3());
				entity.setTestcase3A(prgQuestionDTO.getTestcase3A());
				entity.setTestcase4(prgQuestionDTO.getTestcase4());
				entity.setTestcase4A(prgQuestionDTO.getTestcase4A());
				entity.setTestcase5(prgQuestionDTO.getTestcase5());
				entity.setTestcase5A(prgQuestionDTO.getTestcase5A());
				entity.setTestcase6(prgQuestionDTO.getTestcase6());
				entity.setTestcase6A(prgQuestionDTO.getTestcase6A());
				entity.setTestcase7(prgQuestionDTO.getTestcase7());
				entity.setTestcase7A(prgQuestionDTO.getTestcase7A());
				entity.setTestcase8(prgQuestionDTO.getTestcase8());
				entity.setTestcase8A(prgQuestionDTO.getTestcase8A());
				entity.setTestcase9(prgQuestionDTO.getTestcase9());
				entity.setTestcase9A(prgQuestionDTO.getTestcase9A());
				entity.setTestcase10(prgQuestionDTO.getTestcase10());
				entity.setTestcase10A(prgQuestionDTO.getTestcase10A());
				prgrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.UPDATED, null);
			} else {

				return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.UPDATED_NOT, null);
			}
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.QUESTION.QUESTION_ADDED, null);
		}

	}

	@Override
	public PrgQuestionDTO getPRGQuestionDetals(String questionId) {

		Optional<PrgQuestionEntity> optionalEntity = prgrepo.findById(questionId);
		PrgQuestionDTO dto = new PrgQuestionDTO();
		try {

			if (optionalEntity.isPresent()) {
				PrgQuestionEntity entity = new PrgQuestionEntity();

				entity = optionalEntity.get();

				dto.setQuestionId(entity.getQuestionId());
				dto.setBasic(entity.getBasic());
				dto.setLanguage(entity.getLanguage());
				dto.setSolution(entity.getSolution());
				dto.setInputs(entity.getInputs());
				dto.setTestcase1(entity.getTestcase1());
				dto.setTestcase1A(entity.getTestcase1A());
				dto.setTestcase2(entity.getTestcase2());
				dto.setTestcase2A(entity.getTestcase2A());
				dto.setTestcase3(entity.getTestcase3());
				dto.setTestcase3A(entity.getTestcase3A());
				dto.setTestcase4(entity.getTestcase4());
				dto.setTestcase4A(entity.getTestcase4A());
				dto.setTestcase5(entity.getTestcase5());
				dto.setTestcase5A(entity.getTestcase5A());
				dto.setTestcase6(entity.getTestcase6());
				dto.setTestcase6A(entity.getTestcase6A());
				dto.setTestcase7(entity.getTestcase7());
				dto.setTestcase7A(entity.getTestcase7A());
				dto.setTestcase8(entity.getTestcase8());
				dto.setTestcase8A(entity.getTestcase8A());
				dto.setTestcase9(entity.getTestcase9());
				dto.setTestcase9A(entity.getTestcase9A());
				dto.setTestcase10(entity.getTestcase10());
				dto.setTestcase10A(entity.getTestcase10A());

			} else {

				return null;
			}
			return dto;
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return dto;
		}

	}

	@Override
	public QuestionDTO getQusetionWithStatus(String questionId) {
		QuestionDTO dto = new QuestionDTO();

		try {
			Optional<QuestionEntity> entity1 = qesrepo.findById(questionId);

			if (entity1.isPresent()) {
				QuestionEntity entity = entity1.get();

				dto.setQuestionid(questionId);
				dto.setQuestiontype(entity.getQuestiontype());
				dto.setQuestionHeading(entity.getQuestionHeading());
				dto.setQuestion(entity.getQuestion());
				dto.setUsername(entity.getUsername());
				dto.setStatus(entity.getStatus());

			}

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
		}

		return dto;
	}

	@Override
	public ServiceResponse verifyQuestion(String questionId) {
		try {
			Optional<QuestionEntity> optionalEntity = qesrepo.findById(questionId);
			if (optionalEntity.isPresent()) {
				QuestionEntity entity = new QuestionEntity();

				entity = optionalEntity.get();

				entity.setStatus("VERIFIED");
				qesrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.QUESTION_VERIFIED, null);
			} else {
				return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_NOT_FOUND, null);
			} // IF ELSE ENDS HERE
		} // TRY ENDS HERE
		catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_ADDED, null);
		}

	}
	
	@Override
	public ServiceResponse deleteQuestion(String questionId) {
		try {
			Optional<QuestionEntity> optionalEntity = qesrepo.findById(questionId);
			if (optionalEntity.isPresent()) {
				QuestionEntity entity = new QuestionEntity();

				entity = optionalEntity.get();

				entity.setStatus("DELETED");
				qesrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.QUESTION.QUESTION_DELETED, null);
			} else {
				return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_NOT_FOUND, null);
			} // IF ELSE ENDS HERE
		} // TRY ENDS HERE
		catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail, Constants.QUESTION.QUESTION_NOT_DELETED , null);
		}

	}
	
	public McqQuestionDTO getMcqFullWithOptionsAndAnswer(String questionId) {
		McqQuestionDTO dto = new McqQuestionDTO();

		try {

			Optional<McqQuestionEntity> entity1 = mcqQuestionRepository.findById(questionId);

			if (entity1.isPresent()) {
				McqQuestionEntity entity = entity1.get();
				dto.setQuestionId(questionId);
				dto.setOption1(entity.getOption1());
				dto.setOption2(entity.getOption2());
				dto.setOption3(entity.getOption3());
				dto.setOption4(entity.getOption4());
				dto.setAnswer(entity.getAnswer());

			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		}

		return dto;
	}

//Excel upload-start//
	
//Excel mcq
	public ServiceResponse processSheetMcq(Sheet sheet, String userName) {
		try {
			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				QuestionDTO questionDTO = processRowQuestion(row);
				if (questionDTO != null) {
					questionDTO.setQuestiontype("MCQ");
					questionDTO.setUsername(userName);

					// adding question to question table and retrieving auto-generated question id
					ServiceResponse response = addQuestionDetails(questionDTO);
					String questionId = response.getMessage();

					// adding data to mcq table
					McqQuestionDTO mcqQuestionDTO = processRowMcq(row);
					mcqQuestionDTO.setQuestionId(questionId);
					addMCQQuestionDetails(mcqQuestionDTO);
				}
			}
			return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, messageSource.getMessage("question.details.psh.VAL0001", null, null),
					null);

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource.getMessage("question.details.psh.VAL0002", null, null),
					null);

		}
	}

//getting question and heading from excel sheet
	private QuestionDTO processRowQuestion(Row row) {
		QuestionDTO questionDTO = new QuestionDTO();
		Cell cell1 = row.getCell(0);
		Cell cell2 = row.getCell(1);

		if (cell1 != null && cell2 != null) {
			questionDTO.setQuestionHeading(cell1.toString());
			questionDTO.setQuestion(cell2.toString());
			return questionDTO;
		} else {
			return null;
		}
	}

//getting mcq options and answer from excel
	private McqQuestionDTO processRowMcq(Row row) {
		McqQuestionDTO mcqQuestionDTO = new McqQuestionDTO();
		Cell cell3 = row.getCell(2);
		Cell cell4 = row.getCell(3);
		Cell cell5 = row.getCell(4);
		Cell cell6 = row.getCell(5);
		Cell cell7 = row.getCell(6);

		if (cell3 != null && cell4 != null && cell5 != null && cell6 != null && cell7 != null) {

			mcqQuestionDTO.setOption1(getCellValueAsString(cell3));
			mcqQuestionDTO.setOption2(getCellValueAsString(cell4));
			mcqQuestionDTO.setOption3(getCellValueAsString(cell5));
			mcqQuestionDTO.setOption4(getCellValueAsString(cell6));
			mcqQuestionDTO.setAnswer(getCellValueAsString(cell7));

			return mcqQuestionDTO;
		} else {
			return null;
		}
	}

//converting datatype  from excel to string
	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (int) numericValue) {
					return String.valueOf((int) numericValue);
				} else {
					// If the value is a floating-point or double
					return String.valueOf(numericValue);
				}

			default:
				return null;
		}
	}

//Excel prg
	public ServiceResponse processSheetPrg(Sheet sheet, String userName) {
		try {
			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				QuestionDTO questionDTO = processRowQuestion(row);
				if (questionDTO != null) {
					questionDTO.setQuestiontype("PRG");
					questionDTO.setUsername(userName);

					// adding question to question table and retrieving auto-generated question id
					ServiceResponse response = addQuestionDetails(questionDTO);
					String questionId = response.getMessage();

					// adding data to prg table
					PrgQuestionDTO prgQuestionDTO = processRowPrg(row);
					prgQuestionDTO.setQuestionId(questionId);
					addPRGQuestionDetails(prgQuestionDTO);
				}
			}
			return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, messageSource.getMessage("question.details.psh.VAL0003", null, null),
					null);

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource.getMessage("question.details.psh.VAL0004", null, null),
					null);

		}
	}

	private PrgQuestionDTO processRowPrg(Row row) {
		PrgQuestionDTO prgQuestionDTO = new PrgQuestionDTO();
		Cell cell3 = row.getCell(2);
		Cell cell4 = row.getCell(3);
		Cell cell5 = row.getCell(4);
		Cell cell6 = row.getCell(5);
		Cell cell7 = row.getCell(6);
		Cell cell8 = row.getCell(7);
		Cell cell9 = row.getCell(8);
		Cell cell10 = row.getCell(9);
		Cell cell11 = row.getCell(10);
		Cell cell12 = row.getCell(11);
		Cell cell13 = row.getCell(12);
		Cell cell14 = row.getCell(13);
		Cell cell15 = row.getCell(14);
		Cell cell16 = row.getCell(15);
		Cell cell17 = row.getCell(16);
		Cell cell18 = row.getCell(17);
		Cell cell19 = row.getCell(18);
		Cell cell20 = row.getCell(19);
		Cell cell21 = row.getCell(20);
		Cell cell22 = row.getCell(21);
		Cell cell23 = row.getCell(22);
		Cell cell24 = row.getCell(23);
		Cell cell25 = row.getCell(24);
		Cell cell26 = row.getCell(25);

		if (cell3 != null && cell4 != null && cell5 != null && cell6 != null && cell7 != null) {

			prgQuestionDTO.setSolution(getCellValueAsString(cell3));
			prgQuestionDTO.setLanguage(getCellValueAsString(cell4));
			prgQuestionDTO.setInputs(getCellValueAsString(cell5));
			prgQuestionDTO.setBasic(getCellValueAsString(cell6));

			prgQuestionDTO.setTestcase1(getCellValueAsString(cell7));
			prgQuestionDTO.setTestcase1A(getCellValueAsString(cell8));
			prgQuestionDTO.setTestcase2(getCellValueAsString(cell9));
			prgQuestionDTO.setTestcase2A(getCellValueAsString(cell10));
			prgQuestionDTO.setTestcase3(getCellValueAsString(cell11));
			prgQuestionDTO.setTestcase3A(getCellValueAsString(cell12));
			prgQuestionDTO.setTestcase4(getCellValueAsString(cell13));
			prgQuestionDTO.setTestcase4A(getCellValueAsString(cell14));
			prgQuestionDTO.setTestcase5(getCellValueAsString(cell15));
			prgQuestionDTO.setTestcase5A(getCellValueAsString(cell16));
			prgQuestionDTO.setTestcase6(getCellValueAsString(cell17));
			prgQuestionDTO.setTestcase6A(getCellValueAsString(cell18));
			prgQuestionDTO.setTestcase7(getCellValueAsString(cell19));
			prgQuestionDTO.setTestcase7A(getCellValueAsString(cell20));
			prgQuestionDTO.setTestcase8(getCellValueAsString(cell21));
			prgQuestionDTO.setTestcase8A(getCellValueAsString(cell22));
			prgQuestionDTO.setTestcase9(getCellValueAsString(cell23));
			prgQuestionDTO.setTestcase9A(getCellValueAsString(cell24));
			prgQuestionDTO.setTestcase10(getCellValueAsString(cell25));
			prgQuestionDTO.setTestcase10A(getCellValueAsString(cell26));

			return prgQuestionDTO;
		} else {
			return null;
		}
	}
//Excel upload-end//

	public ServiceResponse updateMCQAttended(String userName) {
		Optional<RegistrationEntity> optionalEntity = regrepo.findById(userName);
		if (optionalEntity.isPresent()) {
			RegistrationEntity studentdetails = optionalEntity.get();
			studentdetails.setIsMCQAttended(true);
			regrepo.save(studentdetails);
			return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.USERLOG.USER_FOUND, null);
		} else {

			return new ServiceResponse(Constants.MESSAGE_STATUS.error, Constants.USERLOG.USER_NOT_FOUND, null);
		}

	}
	
	public ServiceResponse updatePRGAttended(String userName) {
		Optional<RegistrationEntity> optionalEntity = regrepo.findById(userName);
		if (optionalEntity.isPresent()) {
			RegistrationEntity studentdetails = optionalEntity.get();
			studentdetails.setIsPRGAttended(true);
			regrepo.save(studentdetails);
			return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.USERLOG.USER_FOUND, null);
		} else {

			return new ServiceResponse(Constants.MESSAGE_STATUS.error, Constants.USERLOG.USER_NOT_FOUND, null);
		}

	}
	
	public ServiceResponse updateTabSwitching(String userName) {
		Optional<SelectedQuestionEntity> optionalEntity = selectedQuestionRepository.findById(userName);
		if (optionalEntity.isPresent()) {
			SelectedQuestionEntity studentdetails = optionalEntity.get();
			studentdetails.setTabChanged(studentdetails.getTabChanged() + 1);
			selectedQuestionRepository.save(studentdetails);
			return new ServiceResponse(Constants.MESSAGE_STATUS.success, Constants.USERLOG.USER_FOUND_TAB, null);
		} else {

			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.USERLOG.USER_NOT_FOUND, null);
		}
	}

	
}
	
	

