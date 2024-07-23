package com.compiler.auth.controller;

import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.compiler.auth.dto.AdminDTO;
import com.compiler.auth.dto.CustomUserDetails;
import com.compiler.auth.dto.FileUploadDto;
import com.compiler.auth.dto.JwtResponseDTO;
import com.compiler.auth.dto.LoginDTO;
import com.compiler.auth.dto.RegistrationDTO;
import com.compiler.auth.dto.ServiceResponse;
import com.compiler.auth.dto.StatusDTO;
import com.compiler.auth.jwt.JWTServices;
import com.compiler.auth.service.RegistrationService;
import com.compiler.auth.serviceImp.RegistrationServiceImp;
import com.compiler.auth.util.Constants;
import com.compiler.auth.service.TableService;
import com.compiler.auth.exception.RecordNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("register")
public class RegistrationController 
{
    @Autowired
    private JWTServices jwtUtil;
    
	@Autowired
	RegistrationService regservice;
	
	private static Logger logger = LogManager.getLogger(RegistrationController.class);

	@Autowired
	TableService tableService;

	
	@PostMapping("/questionar")
	ResponseEntity<?> addUserDetails(@Valid @RequestBody RegistrationDTO regdto)
	{
		return new ResponseEntity(regservice.addUserDetails(regdto),HttpStatus.OK);
	}
	
	@PostMapping("/student")
	ResponseEntity<?> addStudentDetails(@Valid @RequestBody RegistrationDTO regdto)
	{
		return new ResponseEntity(regservice.addStudentDetails(regdto),HttpStatus.OK);
	}
	
	@PostMapping("/approve")
	ResponseEntity<?> approveDetails(@RequestBody StatusDTO stdto)
	{
		return new ResponseEntity(regservice.approveDetails(stdto),HttpStatus.OK);
	}
	
	@GetMapping("/getdetails")
	ResponseEntity<?> getallDetails()
	{
		return new ResponseEntity(regservice.searchfor(),HttpStatus.OK);
	}
	
    @PostMapping("/login")
    public ResponseEntity<?> LoginandGenerate(@RequestBody LoginDTO dto)
    {            
    	JwtResponseDTO jwt=new JwtResponseDTO();
    	if(regservice.checkemailpassword(dto))
    	{
            final CustomUserDetails userDetails = regservice.loadUserByUsername(dto.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            jwt.setToken(token);
            jwt.setRole(userDetails.getRole());
            jwt.setUsername(userDetails.getUsername());
            jwt.setIsmcq(regservice.isMcqAttended(userDetails.getUsername()));
            jwt.setTimer(regservice.getTimer(userDetails.getUsername()));
            jwt.setIsSubmitted(regservice.isExamSubmitted(userDetails.getUsername()));
            return new ResponseEntity<>(jwt,HttpStatus.OK);
    	}
    	else
    	{
    		jwt.setToken("NOT FOUND");
            jwt.setRole("NOT FOUND");
            jwt.setUsername("NOT FOUND");
//            jwt.setError("EMAIL CHECK FAILED");
    		return new ResponseEntity<>(jwt, HttpStatus.BAD_REQUEST);


    	}

    }
    
	@GetMapping("/checkismcq/{userName}")
	ResponseEntity<?> getallDetails(@PathVariable("userName") String userName)
	{
		

    	JwtResponseDTO jwt=new JwtResponseDTO();
    	jwt.setIsmcq(regservice.isMcqAttended(userName));
		return new ResponseEntity(jwt,HttpStatus.OK);
	}
    
    
    
    
	
	//search by page
	@GetMapping("/search")
	public ResponseEntity<JSONObject> searchByPage(@RequestParam("searchParam") String searchParam,
												   @RequestParam("iDisplayStart") String iDisplayStart,
												   @RequestParam("iDisplayLength") String iDisplayLength) {
		JSONObject list = regservice.searchByLimit(searchParam, Integer.parseInt(iDisplayStart),
				Integer.parseInt(iDisplayLength));

		return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
	}
 
	//get single user
	@GetMapping("/getUserByName/{userName}")
	public ResponseEntity<Object> getUser(@PathVariable("userName") String userName)throws RecordNotFoundException{
			return new ResponseEntity<>(regservice.getUserByName(userName),new HttpHeaders(),HttpStatus.ACCEPTED);
	}
	
	//verify Guest
	@PostMapping("/verifyUser")
	public ResponseEntity<Object>verify(@RequestBody RegistrationDTO dto) throws RecordNotFoundException {
		return new ResponseEntity<>(regservice.verifyUser(dto),new HttpHeaders(),HttpStatus.OK);
	}
	
	//set as Admin
	@PostMapping("/setAsAdmin")
	public ResponseEntity<ServiceResponse>setAdmin(@RequestBody RegistrationDTO dto) throws RecordNotFoundException {
		return new ResponseEntity<>(regservice.setAsAdmin(dto),new HttpHeaders(),HttpStatus.OK);
	}
	
		@PostMapping("/updateUser")
		public ResponseEntity<Object>update(@RequestBody RegistrationDTO dto) throws RecordNotFoundException {
//			System.out.println(dto.isMCQAttended());
			return new ResponseEntity<>(regservice.updateUser(dto),new HttpHeaders(),HttpStatus.OK);
		}
		
		@GetMapping("/searchQuestion")
		public ResponseEntity<JSONObject> searchQuestionsByPage(@RequestParam("searchParam") String searchParam,
													   @RequestParam("iDisplayStart") String iDisplayStart,
													   @RequestParam("iDisplayLength") String iDisplayLength) {
			JSONObject list = tableService.searchQuestionsByPageByLimit(searchParam, Integer.parseInt(iDisplayStart),
					Integer.parseInt(iDisplayLength));
			return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
		}
		
		//delete user
		@DeleteMapping("/deleteUser/{userName}")
		public ResponseEntity<ServiceResponse>delete(@PathVariable ("userName") String userName) throws RecordNotFoundException{
			return new ResponseEntity<>(regservice.deleteUser(userName),new HttpHeaders(),HttpStatus.OK);
		}

		
	


	//create students excel

	@PostMapping(value = "/createExcel", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse> saveExcel(@RequestBody FileUploadDto fileUploadDto) throws IOException {
		try {
			InputStream inputStream = new ByteArrayInputStream(fileUploadDto.getFileData());
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			ServiceResponse response = regservice.processSheet(sheet,"student");
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
		}catch(Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ResponseEntity<>(new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//create questionnaire excel
	@PostMapping(value = "/createExcelQuestionnaire", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse> saveExcelQuestionnaire(@RequestBody FileUploadDto fileUploadDto) throws IOException {
		try {
			InputStream inputStream = new ByteArrayInputStream(fileUploadDto.getFileData());
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			ServiceResponse response = regservice.processSheet(sheet,"questionnaire");
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
		}catch(Exception e) {
			logger.error("Error:" + e.getMessage(), e);
			return new ResponseEntity<>(new ServiceResponse(Constants.MESSAGE_STATUS.FAILED, Constants.USERLOG.INTERNAL_SRVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//download pdf
	@PostMapping("/student/pdfDownload")
	public ResponseEntity<JSONObject> downloadPdfData(@RequestBody String searchParam) throws Exception {
		return new ResponseEntity<JSONObject>(tableService.regTablePdfDownload(searchParam), HttpStatus.OK);
	}
	//download pdf
	@PostMapping("/student/excelDownload")
	public ResponseEntity<JSONObject> downloadExcelData(@RequestBody String searchParam) throws Exception {
		return new ResponseEntity<JSONObject>(tableService.regTableExcelDownload(searchParam), HttpStatus.OK);
	}
	
	//getting mcq questiond and mcq mark;
	@GetMapping("/getQuestionIdAndMark/{userName}")
	public ResponseEntity<Object> getQuestionIdAndMark(@PathVariable("userName") String userName)throws RecordNotFoundException{
			return new ResponseEntity<>(regservice.getQuestionIdAndMark(userName),new HttpHeaders(),HttpStatus.ACCEPTED);
	}
	
//	SETTINGS FOR ADMIN FOR UPDATING TIME CATEGORY AND LANGUAGES
	@PostMapping("/settings")
	public ResponseEntity<?> settings(@RequestBody AdminDTO admindto) throws Exception {
		return new ResponseEntity<ServiceResponse>(regservice.tablesettings(admindto), new HttpHeaders(),HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getsettings")
	public ResponseEntity<Object> getSettings()throws RecordNotFoundException{
			return new ResponseEntity<>(regservice.getTableSettings(),new HttpHeaders(),HttpStatus.ACCEPTED);
	}
	
//	SETTINGS FOR ADMIN FOR UPDATING TIME CATEGORY AND LANGUAGES
	@PostMapping("/delete/settings")
	public ResponseEntity<?> deleteSettings(@RequestBody AdminDTO admindto) throws Exception {
		return new ResponseEntity<ServiceResponse>(regservice.deletetablesettings(admindto), new HttpHeaders(),HttpStatus.ACCEPTED);
	}
 
}
