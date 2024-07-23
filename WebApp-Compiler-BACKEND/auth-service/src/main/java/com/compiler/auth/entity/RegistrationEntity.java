package com.compiler.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="REGISTRATION")
public class RegistrationEntity 
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",unique = true)
    private Long id;
    
    @Id
    @Column(name = "user_name",unique = true)
    private String userName;
    
    @Column(name = "full_name")
    private String fullname;
    
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "gender")
    private String gender;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phone_number",unique = true)
    private String phoneNumber;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "college")
    private String college;

    @Column(name = "branch")
    private String branch;

    @Column(name = "ktu_id")
    private String ktuId;
    
    @Column(name = "emp_id")
    private String empid;

    @Column(name = "total_marks")
    private String totalMarks;

    @Column(name = "no_of_questions")
    private String numberOfQuestions;
    
    @Column(name = "status")
    private String status;
    
    @Column(name="logined_time")
    private Date logtime;
    
    @Column(name="modified_date")
    private Date mdate;
    
    @Column(name="created_Date")
    private Date cdate;
    
    @Column(name = "is_mcq_attended")
    private boolean isMCQAttended;
    
    @Column(name = "is_prg_attended")
    private boolean isPRGAttended;
    
    
    

	public boolean getIsMCQAttended() {
		return isMCQAttended;
	}

	public void setIsMCQAttended(boolean isMCQAttended) {
		this.isMCQAttended = isMCQAttended;
	}

	public boolean getIsPRGAttended() {
		return isPRGAttended;
	}

	public void setIsPRGAttended(boolean isPRGAttended) {
		this.isPRGAttended = isPRGAttended;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getLogtime() {
		return logtime;
	}

	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
    
    
    

    
}
