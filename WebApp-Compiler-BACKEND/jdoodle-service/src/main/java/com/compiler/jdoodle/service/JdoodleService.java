package com.compiler.jdoodle.service;

import org.json.simple.JSONObject;

import com.compiler.jdoodle.dto.EmbeddedidDTO;
import com.compiler.jdoodle.dto.ServiceResponse;
import com.compiler.jdoodle.dto.StudentPrgDto;

public interface JdoodleService {

	public JSONObject executePrg(StudentPrgDto studentPrgDto);
	
	public ServiceResponse submitPrg(EmbeddedidDTO embeddedidDTO);
	
}
