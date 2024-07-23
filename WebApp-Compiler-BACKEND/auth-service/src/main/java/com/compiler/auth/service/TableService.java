package com.compiler.auth.service;

import java.io.IOException;

import org.json.simple.JSONObject;



public interface TableService {
	
	public JSONObject searchQuestionsByPageByLimit(String searchParam, int start, int pageSize);

	JSONObject regTablePdfDownload(String searchParam) throws IOException;

	JSONObject regTableExcelDownload(String searchParam) throws IOException;
}
