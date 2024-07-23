package com.compiler.jdoodle.serviceImp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compiler.jdoodle.dto.EmbeddedidDTO;
import com.compiler.jdoodle.dto.ServiceResponse;
import com.compiler.jdoodle.dto.StudentPrgDto;
import com.compiler.jdoodle.entity.PrgQuestionEntity;
import com.compiler.jdoodle.entity.StudentPrgEmbeddedId;
import com.compiler.jdoodle.entity.StudentPrgEntity;
import com.compiler.jdoodle.repository.QuestionRepository;
import com.compiler.jdoodle.repository.StudentPrgRepository;
import com.compiler.jdoodle.service.JdoodleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;




@Service
public class JdoodleServiceImp implements JdoodleService{
	
	private static Logger logger = LogManager.getLogger(JdoodleServiceImp.class);

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	StudentPrgRepository studentPrgRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
    @Value("${jDoodle.apiUrl}")
    private String apiUrl;

    @Value("${jDoodle.clientId}")
    private String clientId;

    @Value("${jDoodle.clientSecret}")
    private String clientSecret;
	
	//execute code
	public JSONObject executePrg(StudentPrgDto studentPrgDto)
	{
		JSONObject obj = new JSONObject();
		
		//
		String question_id = studentPrgDto.getQuestionid();
		String user_name = studentPrgDto.getUsername();
		StudentPrgEmbeddedId embeddedid=new StudentPrgEmbeddedId();
		embeddedid.setQuestionid(question_id);
		embeddedid.setUsername(user_name);
		Optional<StudentPrgEntity> entity=studentPrgRepository.findById(embeddedid);
		StudentPrgEntity findByCompositeKeys=entity.get();	
		//
		

		try
		{
			if(entity.isPresent())
			{
	
				String code = studentPrgDto.getSolution();
				String language = studentPrgDto.getLanguage();
				String input = studentPrgDto.getInput();
				
				String version = "";
				
				if(language == "java" || language == "python3") {
					version = "3";
				}else if(language == "c" || language == "cpp") {
					version = "4";
				}
				
				Map<String, Object> requestBody = new HashMap<>();
		        requestBody.put("clientId", clientId);
		        requestBody.put("clientSecret", clientSecret);
		        requestBody.put("script", code);
		        requestBody.put("language", language);
		        requestBody.put("versionIndex", version);
		        requestBody.put("stdin", input);
		        
		        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestBody, String.class);

		        String responseBody = response.getBody();
		        String output = extractOutputFromJson(responseBody);
		        
		        obj.put("output",output);
		        return obj;

			}
			else
			{
		        obj.put("error","User not found");
		        return obj;

			} 

		} 
		catch(Exception e)
		{
	        obj.put("error","Cannot execute");
	        return obj;

		} 
	}
	
	
	//  to extract output value from whole output
	private String extractOutputFromJson(String responseBody) {
      try {
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode jsonNode = objectMapper.readTree(responseBody);
          return jsonNode.get("output").asText();
      } catch (IOException e) {
          e.printStackTrace();
          return null;
      }
	}

	
	//submit code
	public ServiceResponse submitPrg(EmbeddedidDTO embeddedidDTO)
	{
		
		String question_id = embeddedidDTO.getQuestionid();
		String user_name = embeddedidDTO.getUsername();
		StudentPrgEmbeddedId embeddedid=new StudentPrgEmbeddedId();
		embeddedid.setQuestionid(question_id);
		embeddedid.setUsername(user_name);
		Optional<StudentPrgEntity> entity=studentPrgRepository.findById(embeddedid);
		StudentPrgEntity findByCompositeKeys=entity.get();		

		try
		{
			if(entity.isPresent())
			{
				
				///
				StudentPrgEntity studentPrgEntity = new StudentPrgEntity();
				//find row in studentPrgEntity
				
				String code = studentPrgEntity.getSolution();
				String language = studentPrgEntity.getLanguage();
				
				String version = "";
				
				if(language == "java" || language == "python3") {
					version = "3";
				}else if(language == "c" || language == "cpp") {
					version = "4";
				}
				
				//get each test cases from program_questions
				String input="",inputAns="";
				int totalMark=0,testCasePassed=0;
				
				PrgQuestionEntity  prgQuestionEntity = new PrgQuestionEntity();
				for(int i=1; i<=7; i++) {
					if(i==1) {
						input = prgQuestionEntity.getTestcase1();
					    inputAns = prgQuestionEntity.getTestcase1A();
					}else if(i==2) {
						input = prgQuestionEntity.getTestcase2();
					    inputAns = prgQuestionEntity.getTestcase2A();
					}else if(i==3) {
						input = prgQuestionEntity.getTestcase3();
					    inputAns = prgQuestionEntity.getTestcase3A();
					}else if(i==4) {
						input = prgQuestionEntity.getTestcase4();
					    inputAns = prgQuestionEntity.getTestcase4A();
					}else if(i==5) {
						input = prgQuestionEntity.getTestcase5();
					    inputAns = prgQuestionEntity.getTestcase5A();
					}else if(i==6) {
						input = prgQuestionEntity.getTestcase6();
					    inputAns = prgQuestionEntity.getTestcase6A();
					}else if(i==7) {
						input = prgQuestionEntity.getTestcase7();
					    inputAns = prgQuestionEntity.getTestcase7A();
					}
				
				
					Map<String, Object> requestBody = new HashMap<>();
			        requestBody.put("clientId", clientId);
			        requestBody.put("clientSecret", clientSecret);
			        requestBody.put("script", code);
			        requestBody.put("language", language);
			        requestBody.put("versionIndex", version);
			        requestBody.put("stdin", input);
			        
			        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestBody, String.class);
	
			        String responseBody = response.getBody();
			        String output = extractOutputFromJson(responseBody);
			        
			        if(inputAns == output) {
			        	testCasePassed+=1;
			        	totalMark+=10;
			        }
				}
				
				
		        studentPrgEntity.setTestCasePassed(testCasePassed);
		        studentPrgEntity.setTotalmark(totalMark);
		        
		        studentPrgRepository.save(studentPrgEntity);
		        
		        return new ServiceResponse("success","Code Submitted successfully",null);

			}
			else
			{
				return new ServiceResponse("fail","User Not Found",null);
			} 

		} 
		catch(Exception e)
		{
			return new ServiceResponse("fail","User Code Not Submitted",null);

		} 
	}

	
}
