package com.compiler.auth.serviceImp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.compiler.auth.dto.AdminDTO;
import com.compiler.auth.dto.ServiceResponse;
import com.compiler.auth.entity.QuestionEntity;
import com.compiler.auth.entity.RegistrationEntity;
import com.compiler.auth.repository.QuestionRepository;
import com.compiler.auth.repository.RegistrationRepository;
import com.compiler.auth.repository.specification.QuestionSpecification;
import com.compiler.auth.repository.specification.UserSpecification;
import com.compiler.auth.service.TableService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class TableServiceImp implements TableService  {
	private static Logger logger = LogManager.getLogger(TableServiceImp.class);
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	RegistrationRepository registrationRepository;
	
	@Override
	public JSONObject searchQuestionsByPageByLimit(String searchParam, int start, int pageSize) {
		JSONObject result = new JSONObject();
		try {
			Pageable pageable = PageRequest.of(start / pageSize, pageSize);
			Specification<QuestionEntity> spec = QuestionSpecification.getQuestionSpec(searchParam);
			Page<QuestionEntity> questionList = questionRepository.findAll(spec, pageable);
			JSONArray array = new JSONArray();
			JSONArray countQuestionByStatus = countQuestionByStatus(spec);
			JSONArray countByType = countByType(spec);
			for (QuestionEntity questionEntity : questionList) {
				JSONObject obj = new JSONObject();
				obj.put("userName", questionEntity.getUsername());
				obj.put("questionId", questionEntity.getQuestionid());
				obj.put("questionType", questionEntity.getQuestiontype());
				obj.put("questionHeading", questionEntity.getQuestionHeading());
				obj.put("question", questionEntity.getQuestion());
				obj.put("status", questionEntity.getStatus());
				obj.put("createdDate", questionEntity.getCreatedDate());

				array.add(obj);
			}
			result.put("aaData", array);
			result.put("iTotalDisplayRecords", questionRepository.findAll(spec).size());
			result.put("iTotalRecords", questionRepository.findAll(spec).size());
			result.put("countByStatus", countQuestionByStatus);
			result.put("countByType", countByType);
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
		}
		return result;
	}
	
	private JSONArray countByType(Specification<QuestionEntity> spec) {
		JSONArray array = new JSONArray();
		try {
			List<QuestionEntity> headerList = questionRepository.findAll(spec);
			// Count registrations by status
			Map<String, Long> countByType = headerList.stream()
					.collect(Collectors.groupingBy(QuestionEntity::getQuestiontype, Collectors.counting()));
			for (String type : countByType.keySet()) {
				JSONObject obj = new JSONObject();
				obj.put("name", type);
				obj.put("count", countByType.get(type));
				array.add(obj);
			}
		} catch (Exception e) {
			logger.error("Error:" + e.getMessage(), e);
		}
		return array;
	}
		
	private JSONArray countQuestionByStatus(Specification<QuestionEntity> spec) {
		JSONArray array = new JSONArray();
		try {
			List<QuestionEntity> headerList = questionRepository.findAll(spec);
			// Count registrations by status
			Map<String, Long> countByStatus = headerList.stream()
					.collect(Collectors.groupingBy(QuestionEntity::getStatus, Collectors.counting()));
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
	
	@Override
	public JSONObject regTablePdfDownload(String searchParam) throws IOException {
//		System.out.println("oooooooooooooo");
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
		JSONObject obj = new JSONObject();
//		Resource resource = new ClassPathResource("logo");
//		InputStream inputStream = resource.getInputStream();
		try {
			ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
			Document document = new Document();
			PdfWriter.getInstance(document, fileOutputStream);
			document.open();
//			byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
//			addImage(document, bdata);
			Paragraph paragraph = new Paragraph("STUDENT LIST", catFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph);
			document.add(Chunk.NEWLINE);
			PdfPTable table = new PdfPTable(8);
			Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
			table.setWidthPercentage(100);
			float[] columnWidths = { 1.2f, 2.2f, 2f, 1.8f, 1.7f, 1.7f, 1.7f, 1.0f };
			table.setWidths(columnWidths);
			PdfPCell cell0 = new PdfPCell(new Paragraph("SLNO", font));
			PdfPCell cell1 = new PdfPCell(new Paragraph("USER NAME", font));
			PdfPCell cell2 = new PdfPCell(new Paragraph("NAME", font));
			PdfPCell cell3 = new PdfPCell(new Paragraph("MOBILE", font));
			PdfPCell cell4 = new PdfPCell(new Paragraph("EMAIL", font));
			PdfPCell cell5 = new PdfPCell(new Paragraph("MARK", font));
			PdfPCell cell6 = new PdfPCell(new Paragraph("USERTYPE", font));
			PdfPCell cell7 = new PdfPCell(new Paragraph("STATUS", font));
			table.addCell(cell0);
			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
			table.addCell(cell4);
			table.addCell(cell5);
			table.addCell(cell6);
			table.addCell(cell7);
			table.setHeaderRows(1);
			table = getUserTableData(searchParam, table);
			document.add(table);
			document.close();
//	   if(table.size()!=1) {
//	    obj.put(Constants.USERLOG.REPORT_STATUS, true);
//	    byte[] isr = fileOutputStream.toByteArray();
//	    byte[] encoded = Base64.getEncoder().encode(isr);
//	    String enc = new String(encoded);
//	    String fileName = Constants.USERLOG.USER_LOG_PDF_NAME2;
//	    obj.put(Constants.DATA, enc);
//	    obj.put(Constants.FILE_NAME, fileName);
//	    obj.put(Constants.CONTENT_TYPE, Constants.PDF_CONTENT_TYPE);
//	   }else {
//	    obj.put(Constants.USERLOG.REPORT_STATUS, false);
//	   }
			byte[] isr = fileOutputStream.toByteArray();
			byte[] encoded = Base64.getEncoder().encode(isr);
			String enc = new String(encoded);
			String fileName = "student_list";
			obj.put("data", enc);
			obj.put("fileName", fileName);
			obj.put("contentType", "application/pdf");
			return obj;
		} catch (Exception e) {
			logger.error("Error ; " + e.getMessage(), e);
			return obj;
		}
	}
	
	public PdfPTable getUserTableData(String searchParam, PdfPTable table) {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 9);
		PdfPCell dataCell = null;
//		String logStatus = "";
		try {
			int i = 1;
			List<RegistrationEntity> userDetails = registrationRepository.findAll(UserSpecification.getUserSpec(searchParam));
			for (RegistrationEntity user : userDetails) {
				String slNo = Integer.toString(i);
				i++;
				dataCell = new PdfPCell(new Paragraph(slNo, font));
				table.addCell(dataCell);
				String userName = user.getUserName();
				dataCell = new PdfPCell(new Paragraph(userName, font));
				table.addCell(dataCell);
				String name = user.getFullname();
				dataCell = new PdfPCell(new Paragraph(name, font));
				table.addCell(dataCell);
				String number = user.getPhoneNumber();
				dataCell = new PdfPCell(new Paragraph(number, font));
				table.addCell(dataCell);
				String email = user.getEmail();
				dataCell = new PdfPCell(new Paragraph(email, font));
				table.addCell(dataCell);
				String marks = user.getTotalMarks();
				dataCell = new PdfPCell(new Paragraph(marks, font));
				table.addCell(dataCell);
				String userType = user.getUserType();
//				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SS a");
				dataCell = new PdfPCell(new Paragraph(userType, font));
				table.addCell(dataCell);
				String status = user.getStatus();
				dataCell = new PdfPCell(new Paragraph(status, font));
				table.addCell(dataCell);
//				Date sysDate = log.getSysDate();
//				df = new SimpleDateFormat("dd-MMM-yyyy");
//				dataCell = new PdfPCell(new Paragraph(df.format(logTime), font));
//				table.addCell(dataCell);
			}
		} catch (DataAccessException e) {
			logger.error("Error : " + e.getMessage(), e);
		}
		return table;
	}

	@Override
	public JSONObject regTableExcelDownload(String searchParam) throws IOException {
		JSONObject obj = new JSONObject();
		try {
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("ExcelUserDataTable");
			XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
			XSSFFont headerFont = (XSSFFont) workbook.createFont();
//			headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("user name");
			header.getCell(0).setCellStyle(headerStyle);
			header.createCell(1).setCellValue("full name");
			header.getCell(1).setCellStyle(headerStyle);
			header.createCell(2).setCellValue("phone number");
			header.getCell(2).setCellStyle(headerStyle);
			header.createCell(3).setCellValue("email");
			header.getCell(3).setCellStyle(headerStyle);
			header.createCell(4).setCellValue("total mark");
			header.getCell(4).setCellStyle(headerStyle);
			header.createCell(5).setCellValue("user type");
			header.getCell(5).setCellStyle(headerStyle);
			header.createCell(6).setCellValue("status");
			header.getCell(6).setCellStyle(headerStyle);
			sheet = getUserTableExcelSheet(searchParam, sheet);
			sheet.autoSizeColumn((short) 0);
			sheet.autoSizeColumn((short) 1);
			sheet.autoSizeColumn((short) 2);
			sheet.autoSizeColumn((short) 3);
			sheet.autoSizeColumn((short) 4);
			sheet.autoSizeColumn((short) 5);
			sheet.autoSizeColumn((short) 6);
			ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
			workbook.write(fileOutputStream);
			byte[] isr = fileOutputStream.toByteArray();
			byte[] encoded = Base64.getEncoder().encode(isr);
			String enc = new String(encoded);
			String fileName = "ExcelDataTable";
			obj.put("data", enc);
			obj.put("fileName", fileName);
			obj.put("contentType", "application/vnd.ms-excel");
			return obj;
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
			return obj;
		}
	}
	
	public Sheet getUserTableExcelSheet(String searchParam, Sheet sheet) {
//		String logStatus = "";
		try {
			List<RegistrationEntity> user = registrationRepository.findAll(UserSpecification.getUserSpec(searchParam));
			for (int i = 0; i < user.size(); i++) {
				Row row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(user.get(i).getUserName());
				row.createCell(1).setCellValue(user.get(i).getFullname());
				row.createCell(2).setCellValue(user.get(i).getPhoneNumber());
				row.createCell(3).setCellValue(user.get(i).getEmail());
				row.createCell(4).setCellValue(user.get(i).getTotalMarks());
				row.createCell(5).setCellValue(user.get(i).getUserType());
				row.createCell(6).setCellValue(user.get(i).getStatus());
			}
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
		}
		return sheet;
	}
	
	


}
