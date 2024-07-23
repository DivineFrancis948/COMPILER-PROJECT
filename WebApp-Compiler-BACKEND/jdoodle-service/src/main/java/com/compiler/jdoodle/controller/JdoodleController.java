package com.compiler.jdoodle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compiler.jdoodle.dto.EmbeddedidDTO;
import com.compiler.jdoodle.dto.StudentPrgDto;
import com.compiler.jdoodle.service.JdoodleService;


@RestController
@RequestMapping("jdoodle")
public class JdoodleController {
	
	@Autowired
	JdoodleService jdoodleService;
	
	@PostMapping("/execute/prg")
	ResponseEntity<?> executePrg(@RequestBody StudentPrgDto studentPrgDto)
	{
		return new ResponseEntity(jdoodleService.executePrg(studentPrgDto),HttpStatus.OK);
	}
	
	@PostMapping("/submit/prg")
	ResponseEntity<?> submitPrg(@RequestBody EmbeddedidDTO embeddedidDTO)
	{
		return new ResponseEntity(jdoodleService.submitPrg(embeddedidDTO),HttpStatus.OK);
	}

}
