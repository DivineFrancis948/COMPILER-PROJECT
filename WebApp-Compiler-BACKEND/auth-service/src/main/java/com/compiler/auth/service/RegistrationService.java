package com.compiler.auth.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.json.simple.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.compiler.auth.dto.AdminDTO;
import com.compiler.auth.dto.CustomUserDetails;
import com.compiler.auth.dto.LoginDTO;
import com.compiler.auth.dto.RegistrationDTO;
import com.compiler.auth.dto.ServiceResponse;
import com.compiler.auth.dto.StatusDTO;
import com.compiler.auth.exception.RecordNotFoundException;

public interface RegistrationService 
{
	public ServiceResponse addUserDetails(RegistrationDTO regdto);
	public ServiceResponse addStudentDetails(RegistrationDTO regdto);
	public ServiceResponse approveDetails(StatusDTO stdto);
	public JSONObject searchfor();
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
	public boolean checkemailpassword(LoginDTO ldto);
	
	public JSONObject searchByLimit(String searchParam, int start, int pageSize);
	RegistrationDTO getUserByName(String userName) throws RecordNotFoundException;
	ServiceResponse verifyUser(RegistrationDTO registrationDto) throws RecordNotFoundException;
	ServiceResponse setAsAdmin(RegistrationDTO registrationDto) throws RecordNotFoundException;
	ServiceResponse deleteUser(String userName) throws RecordNotFoundException;
	public JSONObject dashBoard();
	public String isMcqAttended(String username);
	public String isExamSubmitted(String username);
	public int getTimer(String username);
	ServiceResponse updateUser(RegistrationDTO registrationDto) throws RecordNotFoundException;

	public ServiceResponse processSheet(Sheet sheet, String userType);
	public JSONObject getQuestionIdAndMark(String userName);
	public ServiceResponse tablesettings(AdminDTO admindto);
	public JSONObject getTableSettings();
	public ServiceResponse deletetablesettings(AdminDTO admindto);






}
