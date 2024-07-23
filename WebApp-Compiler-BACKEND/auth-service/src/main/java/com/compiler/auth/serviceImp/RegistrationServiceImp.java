package com.compiler.auth.serviceImp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.compiler.auth.dto.AdminDTO;
import com.compiler.auth.dto.CustomUserDetails;
import com.compiler.auth.dto.LoginDTO;
import com.compiler.auth.dto.RegistrationDTO;
import com.compiler.auth.dto.ServiceResponse;
import com.compiler.auth.dto.StatusDTO;
import com.compiler.auth.entity.AdminEntity;
import com.compiler.auth.entity.RegistrationEntity;
import com.compiler.auth.repository.AdminSettingRepository;
import com.compiler.auth.repository.QuestionRepository;
import com.compiler.auth.repository.RegistrationRepository;
import com.compiler.auth.repository.SelectedQuestionRepository;
import com.compiler.auth.repository.StudentMcqRepository;
import com.compiler.auth.repository.specification.UserSpecification;
import com.compiler.auth.service.RegistrationService;
import com.compiler.auth.util.Constants;
import com.compiler.auth.entity.SelectedQuestionEntity;
import com.compiler.auth.entity.StudentMcqEntity;
import com.compiler.auth.exception.RecordNotFoundException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class RegistrationServiceImp implements RegistrationService
{
	@Autowired
	RegistrationRepository regrepo;
	
	@Autowired
	SelectedQuestionRepository selectedquestionrepo;
	
	@Autowired
	QuestionRepository questionrepo;
	
	@Autowired
	StudentMcqRepository studentMcqRepository;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	AdminSettingRepository adminsettings;
	
	private static Logger logger = LogManager.getLogger(RegistrationServiceImp.class);
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public ServiceResponse addUserDetails(RegistrationDTO regdto) {
		try {
			Date currentDate = new Date();
			RegistrationEntity entity = new RegistrationEntity();
			String encodedPassword = passwordEncoder.encode(regdto.getPassword());
			entity.setPassword(encodedPassword);
			entity.setUserName(regdto.getUserName());
			entity.setFullname(regdto.getFullname());
			entity.setEmail(regdto.getEmail());
			entity.setPhoneNumber(regdto.getPhoneNumber());
			entity.setEmpid(regdto.getEmpid());
			entity.setIsMCQAttended(false);
			entity.setIsPRGAttended(false);
			entity.setCdate(currentDate);
			entity.setGender(regdto.getGender());
			entity.setUserType("QUESTIONNAIRE");
			entity.setIsMCQAttended(false);
			entity.setIsPRGAttended(false);
			entity.setStatus("PROCESSD");
			entity.setTotalMarks("0");
			regrepo.save(entity);
			return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.USERLOG.USER_ADDED, null);

		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
			return new ServiceResponse("fail", "User Inserted UnSuceessfully", null);

		}
	}
	
	public ServiceResponse addStudentDetails(RegistrationDTO regdto)
	{
		try
		{
			if(regdto.getUserName() == "") {
				return new ServiceResponse(Constants.MESSAGE_STATUS.Fail,"Student Inserted UnSuceessfully",null);
			}else {
				Date currentDate = new Date();
				String encodedPassword = passwordEncoder.encode(regdto.getPassword());
				RegistrationEntity entity = new RegistrationEntity();
				entity.setUserName(regdto.getUserName());
				entity.setFullname(regdto.getFullname());
				entity.setEmail(regdto.getEmail());
				entity.setPhoneNumber(regdto.getPhoneNumber());
				entity.setCollege(regdto.getCollege());
				entity.setBranch(regdto.getBranch());
				entity.setKtuId(regdto.getKtuId());
				entity.setCdate(currentDate);
				entity.setGender(regdto.getGender());
				entity.setPassword(encodedPassword);
				entity.setUserType("STUDENT");
				entity.setStatus("PROCESSD");
				entity.setTotalMarks("0");
				
				regrepo.save(entity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.USERLOG.USER_ADDED,null);
		  }
		}
		catch(Exception e)
		{
			logger.error("Error : " + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.USERLOG.STUDENT_NOT_ADDED,null);

		}
	}
	
	public ServiceResponse approveDetails(StatusDTO stdto)
	{
		try
		{
			Optional<RegistrationEntity> entity = regrepo.findById(stdto.getUsername());
			RegistrationEntity row=entity.get();
			row.setStatus("APPROVED");
			row.setUserType(stdto.getUsertype());
			regrepo.save(row);
			return new ServiceResponse(Constants.MESSAGE_STATUS.success,Constants.USERLOG.STUDENT_ADDED,null);

		}
		catch(Exception e)
		{
			logger.error("Error : " + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.fail,Constants.USERLOG.STUDENT_NOT_ADDED,null);

		}
	}
	
	public JSONObject searchfor() {
		JSONObject result = new JSONObject();
		try {
//			PageRequest pageable = PageRequest.of(start / pageSize, pageSize);
//			Specification<EntityClass> spec = SecurityUserSpec.getUserSpec(searchParam);
			List<RegistrationEntity> usersList = regrepo.findAll();
			JSONArray array = new JSONArray();
//			JSONArray countByStatus = countByStatus(spec);
			for (RegistrationEntity users : usersList) {
				JSONObject obj = new JSONObject();
				obj.put("username", users.getUserName());
				obj.put("fullname", users.getFullname());
				obj.put("email", users.getEmail());
				obj.put("phonenumber", users.getPhoneNumber());
				obj.put("gender", users.getGender());
				obj.put("branch", users.getBranch());
				obj.put("college", users.getCollege());
				obj.put("ktuid", users.getKtuId());
				obj.put("empid", users.getEmpid());
				obj.put("usertype", users.getUserType());
				array.add(obj);
			}
			result.put("aaData", array);
			result.put("iTotalDisplayRecords", regrepo.findAll().size());
			result.put("iTotalRecords", regrepo.findAll().size());
//			result.put("countByStatus", countByStatus);
		} catch (Exception e) {
			result.put("aaData", null);
			logger.error("Error:" + e.getMessage(), e);
			return result;
		}
		return result;
	}
	
	public boolean checkemailpassword(LoginDTO ldto)
	{
//		System.out.println(ldto.getEmail());
//		System.out.println(ldto.getPassword());
		try
		{
			Optional<RegistrationEntity> opt = regrepo.findIdByEmail(ldto.getEmail());
	        if (opt.isEmpty()) 
	        {
//	        	System.out.println("user not present ");
	        	return false;
	        }
	        else
	        {
//	        	System.out.println("user  present ");

	        	RegistrationEntity user = opt.get();
	    		if(user.getEmail().equals(ldto.getEmail()) && passwordEncoder.matches(ldto.getPassword(), user.getPassword()))
	    		{
	    			return true;
	    		}
	    		return false;
			
	        }
		}
		catch(Exception e)
		{
			logger.error("Error:" + e.getMessage(), e);
			return false;
		}

    }
	
	
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			Optional<RegistrationEntity> opt = regrepo.findIdByEmail(email);
			if (opt.isEmpty()) {
				throw new UsernameNotFoundException("User with email: " + email + " not found!");
			} else {
				RegistrationEntity user = opt.get();
				Set<SimpleGrantedAuthority> authorities = Collections
						.singleton(new SimpleGrantedAuthority(user.getUserType()));
				return new CustomUserDetails(
						user.getUserName(),
						user.getEmail(),
						user.getPassword(),
						authorities,
						user.getPhoneNumber(),
						user.getUserType());
			}

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new CustomUserDetails(null, null, null, null, null, null);
		}

	}
	
	public JSONObject dashBoard() {
		JSONObject result = new JSONObject();
		try {

			List<RegistrationEntity> questionsList = regrepo.findByUserType("QUESTIONNAIRE");
			JSONArray array = new JSONArray();
			for (RegistrationEntity users : questionsList) {
				JSONObject obj1 = new JSONObject();
				obj1.put("employeeid", users.getEmpid());
				obj1.put("noofquestions", users.getNumberOfQuestions());
				array.add(obj1);
			}
			result.put("employee", array);

			JSONArray array2 = new JSONArray();
			List<RegistrationEntity> studentsList = regrepo.findByUserType("STUDENT");
			for (RegistrationEntity users : studentsList) {
				JSONObject obj2 = new JSONObject();
				obj2.put("username", users.getUserName());
				obj2.put("totalmarks", users.getTotalMarks());
				array2.add(obj2);
			}
			result.put("student", array2);

			JSONArray array3 = new JSONArray();
			List<RegistrationEntity> studentsList1 = regrepo.findAll();
			for (RegistrationEntity users : studentsList1) {
				JSONObject obj3 = new JSONObject();
				obj3.put("username", users.getUserName());
				obj3.put("status", users.getStatus());
				array3.add(obj3);
			}
			result.put("student2", array3);

			JSONArray array4 = new JSONArray();
			List<SelectedQuestionEntity> studentsList4 = selectedquestionrepo.findAll();
			for (SelectedQuestionEntity users : studentsList4) 
			{
				JSONObject obj4 = new JSONObject();
				obj4.put("username", users.getUserName());
				obj4.put("tabchanged", users.getTabChanged());
				obj4.put("timer", users.getTimer());
				array4.add(obj4);
			}
			result.put("TabSwitched", array4);
			
			JSONArray array5 = new JSONArray();
			List<RegistrationEntity> studentsList5 = regrepo.findAll();
			for (RegistrationEntity users : studentsList5) {
				JSONObject obj5 = new JSONObject();
				obj5.put("username", users.getUserName());
				obj5.put("role", users.getUserType());
				array5.add(obj5);
			}
			result.put("student3", array5);

			result.put("noofstudents", regrepo.countByUserType("STUDENT"));
			result.put("noofadmin", regrepo.countByUserType("ADMIN"));
			result.put("noofquestionair", regrepo.countByUserType("QUESTIONAIR"));
			result.put("allquestions", questionrepo.countAllQuestions());
			result.put("mcqquestions", questionrepo.countByQuestiontype("MCQ"));
			result.put("programquestions", questionrepo.countByQuestiontype("PRG"));

		} catch (Exception e) {
			result.put("student3", null);
			logger.error("Error:" + e.getMessage(), e);
			return result;
		}
		return result;
	}
	
	public String isMcqAttended(String username)
	{
		Optional<RegistrationEntity> user = regrepo.findById(username);

		if (user.isPresent())
		{
//			System.out.println("User is present in the table");
			RegistrationEntity selectedQuestionEntity=user.get();
			if(selectedQuestionEntity.getIsMCQAttended())
			{

				return "YES";
			}
			else
			{

				return "NO";
			}
			
		}
		else
		{
			return "YES";
		}
	}
	
	public String isExamSubmitted(String username)
	{
		Optional<RegistrationEntity> user = regrepo.findById(username);

		if (user.isPresent())
		{
//			System.out.println("User is present in the Registration Table");
			RegistrationEntity selectedQuestionEntity=user.get();
//			System.out.println(selectedQuestionEntity.getTotalMarks());
			if(selectedQuestionEntity.getTotalMarks().equals("0"))
			{

				return "NO";
			}
			else
			{

				return "YES";
			}
			
		}
		else
		{
			return "NO";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject searchByLimit(String searchParam, int start, int pageSize) {
		JSONObject result = new JSONObject();
		try {
			Pageable pageable = PageRequest.of(start / pageSize, pageSize);
			Specification<RegistrationEntity> spec = UserSpecification.getUserSpec(searchParam);
			Page<RegistrationEntity> usersList = regrepo.findAll(spec, pageable);
			JSONArray array = new JSONArray();
			JSONArray countByStatus = countByStatus(spec);
			JSONArray countByType = countByType(spec);
			for (RegistrationEntity registrationEntity : usersList) {
				JSONObject obj = new JSONObject();
				obj.put("userName", registrationEntity.getUserName());
				obj.put("phoneNumber", registrationEntity.getPhoneNumber());
				
				obj.put("fullname", registrationEntity.getFullname());
				obj.put("email", registrationEntity.getEmail());
				obj.put("userType", registrationEntity.getUserType());
				
				obj.put("status", registrationEntity.getStatus());
				obj.put("totalMarks",registrationEntity.getTotalMarks());
				obj.put("isMCQAttended",registrationEntity.getIsMCQAttended());
				obj.put("isPRGAttended",registrationEntity.getIsPRGAttended());
//				obj.put("emailId", users.getEmailId());
//				obj.put("cellPhone", users.getCellNo());
				array.add(obj);
			}
			result.put("aaData", array);
			result.put("iTotalDisplayRecords", regrepo.findAll(spec).size());
			result.put("iTotalRecords", regrepo.findAll(spec).size());
			result.put("countByStatus", countByStatus);
			result.put("countByType", countByType);
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
		}
		return result;
	}
	

	private JSONArray countByStatus(Specification<RegistrationEntity> spec) {
	    JSONArray array = new JSONArray();
	    try {
	        List<RegistrationEntity> headerList = regrepo.findAll(spec);
	        // Count registrations by status
	        Map<String, Long> countByStatus = headerList.stream()
	                .collect(Collectors.groupingBy(RegistrationEntity::getStatus, Collectors.counting()));
	        for (String status : countByStatus.keySet()) {
	            JSONObject obj = new JSONObject();
	            obj.put("name", status);
	            obj.put("count", countByStatus.get(status));
	            array.add(obj);
	        }
	        
	    } catch (Exception e) {
	        logger.error("Error:" + e.getMessage(), e);
	    }
	    return array;
	}
	
	private JSONArray countByType(Specification<RegistrationEntity> spec) {
	    JSONArray array = new JSONArray();
	    try {
	        List<RegistrationEntity> headerList = regrepo.findAll(spec);
	        // Count registrations by status
	        Map<String, Long> countByType = headerList.stream()
	                .collect(Collectors.groupingBy(RegistrationEntity::getUserType, Collectors.counting()));
	        for (String userType : countByType.keySet()) {
	            JSONObject obj = new JSONObject();
	            obj.put("name", userType);
	            obj.put("count", countByType.get(userType));
	            array.add(obj);
	        }
	        
	    } catch (Exception e) {
	        logger.error("Error:" + e.getMessage(), e);
	    }
	    return array;
	}
	
	
	//get user by userName
	@Override
	public RegistrationDTO getUserByName(String userName)throws RecordNotFoundException {
						
			Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
			
			if(!findByKey.isPresent()) {
				throw new RecordNotFoundException("can't find specified record.");
			}else {
			  try {
				RegistrationEntity registrationEntity = findByKey.get();
				RegistrationDTO registrationDTO = new RegistrationDTO();
				
				registrationDTO.setUserName(userName);
				
				registrationDTO.setFullname(registrationEntity.getFullname());
				registrationDTO.setEmail(registrationEntity.getEmail());
				
				registrationDTO.setPhoneNumber(registrationEntity.getPhoneNumber());
				registrationDTO.setCollege(registrationEntity.getCollege());
				
				registrationDTO.setBranch(registrationEntity.getBranch());
				
				registrationDTO.setGender(registrationEntity.getGender());
				registrationDTO.setUserType(registrationEntity.getUserType());
				
				registrationDTO.setStatus(registrationEntity.getStatus());
				
				registrationDTO.setCdate(registrationEntity.getCdate());
				registrationDTO.setMdate(registrationEntity.getMdate());
				registrationDTO.setEmpid(registrationEntity.getEmpid());
				registrationDTO.setId(registrationEntity.getId());	
				registrationDTO.setKtuId(registrationEntity.getKtuId());	
				registrationDTO.setLogtime(registrationEntity.getLogtime());	
				registrationDTO.setNumberOfQuestions(registrationEntity.getNumberOfQuestions());
				registrationDTO.setTotalMarks(registrationEntity.getTotalMarks());
				
				
				
				return registrationDTO;
			
			}catch(Exception e) {
				logger.error("Error:" + e.getMessage(), e);
				return null;
			}
		 }
			
	}
	//verify user
	@Override
	public ServiceResponse verifyUser(RegistrationDTO registrationDto) throws RecordNotFoundException{
		try {
		
			String userName = registrationDto.getUserName();
			
			Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
			
			if (!findByKey.isPresent()) {
				throw new RecordNotFoundException("register.details.psh.VAL0001");
			} else {
				RegistrationEntity registrationEntity = findByKey.get();
				
				
				if (registrationEntity.getStatus().contentEquals("VERIFIED")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0002", null, null), null);
				}
				if (registrationEntity.getStatus().contentEquals("DELETED")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0003", null, null), null);
				}
				
				registrationEntity.setStatus("VERIFIED");
				
				regrepo.save(registrationEntity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.Success,
						messageSource.getMessage("register.details.psh.VAL0004", null, null), null);
			}
			
		
		} catch (RecordNotFoundException e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0005", null, null), null);
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0006", null, null), null);
	}
	}
	

	//set as admin
	@Override
	public ServiceResponse setAsAdmin(RegistrationDTO registrationDto) throws RecordNotFoundException{
		try {
		
			String userName = registrationDto.getUserName();
			
			Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
			
			if (!findByKey.isPresent()) {
				throw new RecordNotFoundException("register.details.psh.VAL0007");
			} else {
				RegistrationEntity registrationEntity = findByKey.get();
				
				
				if (registrationEntity.getUserType().contentEquals("ADMIN")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0008", null, null), null);
				}
				if (registrationEntity.getUserType().contentEquals("STUDENT")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0009", null, null), null);
				}
				if (registrationEntity.getStatus().contentEquals("DELETED")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0010", null, null), null);
				}
				registrationEntity.setUserType("ADMIN");
				
				regrepo.save(registrationEntity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.Success,
						messageSource.getMessage("register.details.psh.VAL0011", null, null), null);
			}
			
		
		} catch (RecordNotFoundException e) {
			logger.error("Error:" + e.getMessage(), e);

			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0012", null, null), null);
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0013", null, null), null);
	}
	}
//
//	//delete user
	@Override
	public ServiceResponse deleteUser(String userName) throws RecordNotFoundException {
		try {
			
			Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
			if (!findByKey.isPresent()) {
				throw new RecordNotFoundException("register.details.psh.VAL0014");
			} else {
				RegistrationEntity registrationEntity = findByKey.get();
				
				if (registrationEntity.getStatus().contentEquals("DELETED")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.Failed, messageSource
							.getMessage("register.details.psh.VAL0015", null, null), null);
				}
				
				registrationEntity.setStatus("DELETED");
				
				regrepo.save(registrationEntity);
			}
			return new ServiceResponse(Constants.MESSAGE_STATUS.Success,
					messageSource.getMessage("register.details.psh.VAL0016", null, null), null);
		} catch (RecordNotFoundException e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0017", null, null), null);
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse("Failed",
					messageSource.getMessage("register.details.psh.VAL0018", null, null), null);
		}
	}
	
	
	//update user
	@Override
	public ServiceResponse updateUser(RegistrationDTO registrationDto) throws RecordNotFoundException{
		try {
		
			String userName = registrationDto.getUserName();
			
			Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
			
			if (!findByKey.isPresent()) {
				throw new RecordNotFoundException("register.details.psh.VAL0019");
			} else {
				RegistrationEntity registrationEntity = findByKey.get();
				
				
				if (registrationEntity.getUserType().contentEquals("ADMIN")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0020", null, null), null);
				}
				if (registrationEntity.getStatus().contentEquals("DELETED")) {
					return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource
							.getMessage("register.details.psh.VAL0021", null, null), null);
				}
				
				registrationEntity.setFullname(registrationDto.getFullname());
				registrationEntity.setEmail(registrationDto.getEmail());
				registrationEntity.setPhoneNumber(registrationDto.getPhoneNumber());
				registrationEntity.setCollege(registrationDto.getCollege());
				registrationEntity.setBranch(registrationDto.getBranch());
				registrationEntity.setGender(registrationDto.getGender());
				registrationEntity.setTotalMarks(registrationDto.getTotalMarks());	
				registrationEntity.setIsMCQAttended(registrationDto.isMCQAttended());	
				registrationEntity.setIsPRGAttended(registrationDto.isPRGAttended());	
//				System.out.println(registrationDto.isMCQAttended());
				registrationEntity.setStatus("PROCESSD");
				
				regrepo.save(registrationEntity);
				return new ServiceResponse(Constants.MESSAGE_STATUS.Success,
						messageSource.getMessage("register.details.psh.VAL0022", null, null), null);
			}
			
		
		} catch (RecordNotFoundException e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0023", null, null), null);
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ServiceResponse(Constants.MESSAGE_STATUS.Failed,
					messageSource.getMessage("register.details.psh.VAL0024", null, null), null);
	}
	}
	
	//create using excel
	public ServiceResponse processSheet(Sheet sheet, String userType) {
		try {
			List<JSONObject> excelErrorList = new ArrayList<>();
	        Iterator<Row> rowIterator = sheet.rowIterator();
	        rowIterator.next();
	        
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            
	            RegistrationDTO registrationDTO = new RegistrationDTO();
	            if(userType=="student") {
	            	 registrationDTO = processRow(row);
	            }else if(userType=="questionnaire"){
	            	 registrationDTO = processRowQuestionnaire(row);
	            }
	            if (registrationDTO != null) {
	            	
	            	String userName = registrationDTO.getUserName();
	            	Optional<RegistrationEntity> findByKey = regrepo.findById(userName);
	            	Optional<RegistrationEntity> findByPhoneNumber = regrepo.findByPhoneNumber(registrationDTO.getPhoneNumber());
	            	Optional<RegistrationEntity> findByEmailId = regrepo.findByEmail(registrationDTO.getEmail());
	                if (findByKey.isPresent()) { 
	                	JSONObject entry = new JSONObject();
	                    entry.put("userName", userName);
	                    entry.put("message", "Username already exists");
	                    excelErrorList.add(entry);
	                    
	                }else if (findByPhoneNumber.isPresent()) {
	                    JSONObject entry = new JSONObject();
	                    entry.put("userName", userName);
	                    entry.put("message", "Phone number already exists");
	                    excelErrorList.add(entry);
	                } else if (findByEmailId.isPresent()) {
	                    JSONObject entry = new JSONObject();
	                    entry.put("userName", userName);
	                    entry.put("message", "Email Id already exists");
	                    excelErrorList.add(entry);
	                } else {
	                	
	                	if(userType=="student") {
	                		addStudentDetails(registrationDTO);
	                	}else if(userType == "questionnaire"){
	                		addUserDetails(registrationDTO);
	                	}
	                }
	                
				}
			}
	        return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, messageSource.getMessage("register.details.psh.VAL0025", null, null), excelErrorList);

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
	        return new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, messageSource.getMessage("register.details.psh.VAL0026", null, null), null);

		}
    }

	
//	//create using excel student
	private RegistrationDTO processRow(Row row) {
	    RegistrationDTO registrationDTO = new RegistrationDTO();
	    Cell cell1 = row.getCell(0);
	    System.out.println(row.getCell(0));
	    Cell cell2 = row.getCell(1);
	    Cell cell3 = row.getCell(2);
	    Cell cell4 = row.getCell(3);
	    Cell cell5 = row.getCell(4);
	    Cell cell6 = row.getCell(5);
	    Cell cell7 = row.getCell(6);
	    Cell cell8 = row.getCell(7);
	    Cell cell9 = row.getCell(8);

	    try {
	        if (cell1 != null && cell2 != null && cell3 != null && cell4 != null && cell5 != null && cell6 != null && cell7 != null && cell8 != null && cell9 != null) {

	            registrationDTO.setFullname(cell1.toString());
	            registrationDTO.setUserName(cell2.toString());
	            registrationDTO.setEmail(cell3.toString());

	            // Handle numeric cell value extraction with try-catch
	            try {
	                BigInteger phoneNum = new BigInteger(String.valueOf((long) cell4.getNumericCellValue()));
	                registrationDTO.setPhoneNumber(phoneNum.toString());
	            } catch (Exception e) {
	                // Handle exception if the numeric cell extraction fails
	    			logger.error("Error:" + e.getMessage(), e);
	                registrationDTO.setPhoneNumber(null); // or set a default value
	            }

	            registrationDTO.setCollege(cell5.toString());
	            registrationDTO.setBranch(cell6.toString());
	            registrationDTO.setKtuId(cell7.toString());
	            registrationDTO.setPassword(cell8.toString());
	            registrationDTO.setGender(cell9.toString());
	            return registrationDTO;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        // Handle any other exceptions that might occur during cell value extraction
			logger.error("Error:" + e.getMessage(), e);
	        return null; // or throw a custom exception if needed
	    }
	}
	
	public int getTimer(String username) {
	    try {
	        Optional<SelectedQuestionEntity> studentquestiondetails = selectedquestionrepo.findById(username);

	        if (studentquestiondetails.isPresent()) 
	        {
	            SelectedQuestionEntity studenttimer = studentquestiondetails.get();
//	        	System.out.println(studenttimer.getTimer());
	            return studenttimer.getTimer();
	        } else {
		        Optional<AdminEntity> entity = adminsettings.findById((long) 1);
		        if(entity.isPresent())
		        {
			        AdminEntity timer=entity.get();

			        if (timer.getTimer() == null || timer.getTimer().isEmpty())
		        	{
//			        	System.out.println(60);
		        		return 60;
		        	}
		        	else
		        	{
				        int timervalue = Integer.parseInt(timer.getTimer());
//			        	System.out.println(timervalue);
			            return timervalue;//

		        	}
		        }
		        else
		        {
//		        	System.out.println(60);
		        	return 60;
		        }
		     // Convert the string to an integer
	            // Handle the case when the entity with the given ID is not found
	            // You may throw a custom exception, log the error, or return a default value based on your requirements
	        }
	    } catch (Exception e) {
	        // Handle the exception appropriately, log it, or take any necessary action
			logger.error("Error:" + e.getMessage(), e);
	        return 0;// or throw a custom exception or return another appropriate default value based on your requirements
	    }
	}



//	//create using excel questionnaire
	private RegistrationDTO processRowQuestionnaire(Row row) {
	    RegistrationDTO registrationDTO = new RegistrationDTO();
	    Cell cell1 = row.getCell(0);
	    Cell cell2 = row.getCell(1);
	    Cell cell3 = row.getCell(2);
	    Cell cell4 = row.getCell(3);
	    Cell cell5 = row.getCell(4);
	    Cell cell6 = row.getCell(5);
	    Cell cell7 = row.getCell(6);


	    try {
	        if (cell1 != null && cell2 != null && cell3 != null && cell4 != null && cell5 != null && cell6 != null && cell7 != null ) {

	            registrationDTO.setFullname(cell1.toString());
	            registrationDTO.setUserName(cell2.toString());
	            registrationDTO.setEmail(cell3.toString());

	            // Handle numeric cell value extraction with try-catch
	            try {
	                BigInteger phoneNum = new BigInteger(String.valueOf((long) cell4.getNumericCellValue()));
	                registrationDTO.setPhoneNumber(phoneNum.toString());
	            } catch (Exception e) {
	                // Handle exception if the numeric cell extraction fails
	    			logger.error("Error:" + e.getMessage(), e);
	                registrationDTO.setPhoneNumber(null); // or set a default value
	            }
	            											
	            registrationDTO.setEmpid(cell5.toString());
	            registrationDTO.setPassword(cell6.toString());
	            registrationDTO.setGender(cell7.toString());
	          
	            return registrationDTO;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        // Handle any other exceptions that might occur during cell value extraction
			logger.error("Error:" + e.getMessage(), e);
	        return null; // or throw a custom exception if needed
	    }
	}
	
public JSONObject getQuestionIdAndMark(String userName) {
		
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		JSONObject detail = new JSONObject();
		try {
			
			List<StudentMcqEntity> studentOptionalList = studentMcqRepository.findByStudentMcqEmbeddedUserName(userName);
			
			if(studentOptionalList.isEmpty()) {
				obj.put("message", "failed");
			}else{
				
				for(StudentMcqEntity i :studentOptionalList) {
					detail.put(i.getStudentMcqEmbedded().getQuestionId(), i.getMarks());
				}
				array.add(detail);
				obj.put("message", "success");
				obj.put("details",array);
			}
			
		}catch(Exception e) {
			logger.error("Error : " + e.getMessage(), e);
		}
		return obj;
	}

	public ServiceResponse tablesettings(AdminDTO admindto) {
	    try {
	        Optional<AdminEntity> entity = adminsettings.findById((long) 1);
	
	        if (entity.isPresent()) {
	            AdminEntity adminsettingss = entity.get();	
	            if (admindto.getLanguages() != null) 
	            {
	                List<String> languages = new ArrayList<>();
	                if(adminsettingss.getProgrammingLanguages()!=null)
	                {
		            	List<String> languageList=adminsettingss.getProgrammingLanguages();
		                for(String qlist:languageList)
		                {
		                	languages.add(qlist);
		                }
		                languages.add(admindto.getLanguages());
	                }
	                else
	                {
		                languages.add(admindto.getLanguages());
	                }
	                adminsettingss.setProgrammingLanguages(languages);
	            }

	            if (admindto.getCategories() != null) 
	            {	                
	            	List<String> categories = new ArrayList<>();
	                if(adminsettingss.getQuestionCategories()!=null)
	                {
		            	List<String> categorylist=adminsettingss.getQuestionCategories();
		                for(String qlist:categorylist)
		                {
		                	categories.add(qlist);
		                }
		                categories.add(admindto.getCategories());

	                }
	                else
	                {
		                categories.add(admindto.getCategories());

	                }
	                adminsettingss.setQuestionCategories(categories);
	            }
	
	            if (admindto.getTimer() != null) 
	            {
	                adminsettingss.setTimer(admindto.getTimer());
	            }
	            adminsettings.save(adminsettingss);
	            return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, Constants.USERLOG.USER_UPDATE_EXIST, null);
	        } else {
	        	
	            AdminEntity settings = new AdminEntity();
	
	            if (admindto.getLanguages() != null) {
	                List<String> languages = new ArrayList<>();
	                languages.add(admindto.getLanguages());
	                settings.setProgrammingLanguages(languages);
	            }

	            if (admindto.getCategories() != null) {
	                List<String> categories = new ArrayList<>();
	                categories.add(admindto.getCategories());
	                settings.setQuestionCategories(categories);
	            }
	
	            if (admindto.getTimer() != null) {
	                settings.setTimer(admindto.getTimer());
	            }
	
	            adminsettings.save(settings);
	            return new ServiceResponse("ADDED", "Updated User Not Exist", null);
	        }
	    } catch (EmptyResultDataAccessException e) {
			logger.error("Error:" + e.getMessage(), e);
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "EmptyResultDataAccessException: " + e.getMessage(), null);
	    } catch (DataAccessException e) {
			logger.error("Error:" + e.getMessage(), e);
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "DataAccessException: " + e.getMessage(), null);
	    } catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "Unexpected Exception: " + e.getMessage(), null);
	    }
	}


	// Other imports...

	public JSONObject getTableSettings() {
	    JSONObject result = new JSONObject();
	    List<JSONObject> languageListJson = new ArrayList<>();
	    List<JSONObject> categoryListJson = new ArrayList<>();

	    try {
	        Optional<AdminEntity> entity = adminsettings.findById((long) 1);

	        if (entity.isPresent()) {
	            AdminEntity adminsettingss = entity.get();

	            List<String> languageList = adminsettingss.getProgrammingLanguages();
	            for (String qlist : languageList) {
	                JSONObject language = new JSONObject();
	                language.put("language", qlist);
	                languageListJson.add(language);
	            }

	            List<String> categoryList = adminsettingss.getQuestionCategories();
	            for (String qlist : categoryList) {
	                JSONObject category = new JSONObject();
	                category.put("category", qlist);
	                categoryListJson.add(category);
	            }

	            result.put("Programminglanguege", languageListJson);
	            result.put("Category", categoryListJson);
	            result.put("Timer", adminsettingss.getTimer());

	            return result;
	        } else {
	            result.put("Programminglanguege", "No Language");
	            result.put("Category", "No Category");
	            result.put("Timer", "No Timer");
	            return result;
	        }
	    } catch (EmptyResultDataAccessException e) {
	        // Handle the case where no entity is found (findById returns null)
            result.put("Programminglanguege", "No Language");
            result.put("Category", "No Category");
            result.put("Timer", "No Timer");	        return result;
	    } catch (DataAccessException e) {
	        // Handle other data access exceptions
            result.put("Programminglanguege", "No Language");
            result.put("Category", "No Category");
            result.put("Timer", "No Timer");	        return result;
	    } catch (Exception e) {
	        // Handle any other exceptions
            result.put("Programminglanguege", "No Language");
            result.put("Category", "No Category");
            result.put("Timer", "No Timer");	        return result;
	    }
	}


	public ServiceResponse deletetablesettings(AdminDTO admindto) {
	    try {
	        Optional<AdminEntity> entity = adminsettings.findById((long) 1);

	        if (entity.isPresent()) {
	            AdminEntity adminsettingss = entity.get();

	            if (admindto.getLanguages() != null) {
	                List<String> languageList = adminsettingss.getProgrammingLanguages();
	                removeElementFromList(languageList, admindto.getLanguages());
	                adminsettingss.setProgrammingLanguages(languageList);
	            }

	            if (admindto.getCategories() != null) {
	                List<String> categoryList = adminsettingss.getQuestionCategories();
	                removeElementFromList(categoryList, admindto.getCategories());
	                adminsettingss.setQuestionCategories(categoryList);
	            }

	            adminsettings.save(adminsettingss);
	            return new ServiceResponse(Constants.MESSAGE_STATUS.SUCCESS, Constants.QUESTION.DELETED, null);
	        } else {
	            return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, Constants.QUESTION.NO_RECORD, null);
	        }
	    } catch (EmptyResultDataAccessException e) {
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "EmptyResultDataAccessException: " + e.getMessage(), null);
	    } catch (DataAccessException e) {
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "DataAccessException: " + e.getMessage(), null);
	    } catch (Exception e) {
	        return new ServiceResponse(Constants.MESSAGE_STATUS.ERROR, "Unexpected Exception: " + e.getMessage(), null);
	    }
	}

	// Utility method to remove an element from a list
	private void removeElementFromList(List<String> list, String elementToRemove) {
	    Iterator<String> iterator = list.iterator();
	    while (iterator.hasNext()) {
	        String qlist = iterator.next();
	        if (qlist.equals(elementToRemove)) {
	            iterator.remove();
	        }
	    }
	}
	
}
