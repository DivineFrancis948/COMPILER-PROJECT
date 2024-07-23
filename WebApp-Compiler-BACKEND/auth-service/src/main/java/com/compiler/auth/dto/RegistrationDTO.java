package com.compiler.auth.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class RegistrationDTO 
{

	@NotEmpty
    private String userName;
	
    // Password must contain at least one digit, one lowercase letter, one uppercase letter,
    // and be at least 8 characters long
	@NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Password must meet the criteria")
    private String password;

	@NotEmpty
	@Email
    private String email;

	@NotEmpty
    @Pattern(regexp = "\\d+", message = "Only digits are allowed")
    private String phoneNumber;

    private String userType;

    private String college;

    private String branch;

    private String ktuId;
    
    private String empid;

    private String totalMarks;

    private String numberOfQuestions;
    
    private String status;
    private boolean isPRGAttended;
    private boolean isMCQAttended;
    
    @NotEmpty
    private String fullname;
    
    @NotEmpty
    private String gender;
    
    private Date mdate;
    private Date cdate;
    private Date logtime;
    private Long id;
    
    
    



	public boolean isPRGAttended() {
		return isPRGAttended;
	}

	public void setPRGAttended(boolean isPRGAttended) {
		this.isPRGAttended = isPRGAttended;
	}

	public boolean isMCQAttended() {
		return isMCQAttended;
	}

	public void setMCQAttended(boolean isMCQAttended) {
		this.isMCQAttended = isMCQAttended;
	}

	public Date getMdate() {
		return mdate;
	}

	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}

	public Date getCdate() {
		return cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public Date getLogtime() {
		return logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getKtuId() {
		return ktuId;
	}

	public void setKtuId(String ktuId) {
		this.ktuId = ktuId;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(String totalMarks) {
		this.totalMarks = totalMarks;
	}

	public String getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(String numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
    
    

}
