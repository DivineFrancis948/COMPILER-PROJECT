package com.compiler.auth.repository.specification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import com.compiler.auth.entity.QuestionEntity;
import com.compiler.auth.entity.RegistrationEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
public class QuestionSpecification {
	public static Specification<QuestionEntity> getQuestionSpec(String searchParam) {
		return new Specification<QuestionEntity>() {
			@Override
			public Predicate toPredicate(Root<QuestionEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate finalPredicate = null;
				JSONParser parser = new JSONParser();
				JSONObject searchObject;
				try {
					if (searchParam != null && !searchParam.isEmpty()) { //to avoid end file at position 0
					    searchObject = (JSONObject) parser.parse(searchParam);
					
					
						String userName = (String) searchObject.get("username");
						String questionid = (String) searchObject.get("questionid");
						String questiontype = (String) searchObject.get("questiontype");
						String questionHeading = (String) searchObject.get("questionHeading");
						String question = (String) searchObject.get("question");
						String status = (String) searchObject.get("status");
						String createdDate = (String) searchObject.get("createdDate");
						
					
					
						if (!StringUtils.isEmpty(userName)) {
							Predicate userNamePredicate = criteriaBuilder.equal(root.get("username"), userName);
							
							finalPredicate = criteriaBuilder.and(userNamePredicate);
							
						}
					
						if (!StringUtils.isEmpty(questionid)) {
							Predicate questionidPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("questionid")),questionid.toUpperCase()+"%");
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, questionidPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(questionidPredicate);
							}
						}
						if (!StringUtils.isEmpty(questiontype)) {
							Predicate questiontypePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("questiontype")),questiontype.toUpperCase()+"%");
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, questiontypePredicate);
							}else {
							finalPredicate = criteriaBuilder.and(questiontypePredicate);
							}
						}
						if (!StringUtils.isEmpty(questionHeading)) {
							Predicate questionHeadingPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("questionHeading")),"%"+questionHeading.toUpperCase()+"%");
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, questionHeadingPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(questionHeadingPredicate);
							}
						}
						if (!StringUtils.isEmpty(question)) {
							Predicate questionPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("question")),"%"+question.toUpperCase()+"%");
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, questionPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(questionPredicate);
							}
						}
					
						if (!StringUtils.isEmpty(status)) {
							Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, statusPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(statusPredicate);
							}
						}
						
						if (!StringUtils.isEmpty(createdDate)) {
							Predicate createdDatePredicate = criteriaBuilder.equal(root.get("createdDate"), createdDate);
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, createdDatePredicate);
							}else {
							finalPredicate = criteriaBuilder.and(createdDatePredicate);
							}
						}
	
//						Order proTimeOrder = criteriaBuilder.desc(root.get("userName"));
//						query.orderBy(proTimeOrder);
					} else {
						searchObject = null;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return finalPredicate;
			}
		};
	}
	
}