package com.compiler.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compiler.auth.service.RegistrationService;

@RestController
@RequestMapping("dashboard")
public class Admin 
{
	@Autowired
	RegistrationService regservice;
	
	@GetMapping("/getdetails")
	ResponseEntity<?> getdetailsforDashboard()
	{
		return new ResponseEntity(regservice.dashBoard(),HttpStatus.OK);
	}

}
