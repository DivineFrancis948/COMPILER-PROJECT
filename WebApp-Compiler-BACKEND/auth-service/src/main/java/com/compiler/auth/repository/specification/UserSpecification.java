package com.compiler.auth.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.compiler.auth.entity.RegistrationEntity;

public class UserSpecification {

	public static Specification<RegistrationEntity> getUserSpec(String searchParam) {
		return new Specification<RegistrationEntity>() {
			@Override
			public Predicate toPredicate(Root<RegistrationEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate finalPredicate = null;
				JSONParser parser = new JSONParser();
				JSONObject searchObject;
				try {
					if (searchParam != null && !searchParam.isEmpty()) { //to avoid end file at position 0
					    searchObject = (JSONObject) parser.parse(searchParam);
					
					
						String userName = (String) searchObject.get("userName");
						String phoneNumber = (String) searchObject.get("phoneNumber");
						String fullname = (String) searchObject.get("fullname");
						String email = (String) searchObject.get("email");
						String userType = (String) searchObject.get("userType");
						String status = (String) searchObject.get("status");

					
						if (!StringUtils.isEmpty(userName)) {
							Predicate userNamePredicate = criteriaBuilder.equal(root.get("userName"), userName);
							
							finalPredicate = criteriaBuilder.and(userNamePredicate);
							
						}
					
						if (!StringUtils.isEmpty(phoneNumber)) {
							Predicate phoneNoPredicate = criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, phoneNoPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(phoneNoPredicate);
							}
						}
						if (!StringUtils.isEmpty(fullname)) {
							Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("fullname")), "%"+fullname.toUpperCase()+"%");
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, namePredicate);
							}else {
							finalPredicate = criteriaBuilder.and(namePredicate);
							}
						}
						if (!StringUtils.isEmpty(email)) {
							Predicate emailPredicate = criteriaBuilder.equal(root.get("email"), email);
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, emailPredicate);
							}else {
							finalPredicate = criteriaBuilder.and(emailPredicate);
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
						
						if (!StringUtils.isEmpty(userType)) {
							Predicate userTypePredicate = criteriaBuilder.equal(root.get("userType"), userType);
							if (finalPredicate != null) {
							finalPredicate = criteriaBuilder.and(finalPredicate, userTypePredicate);
							}else {
							finalPredicate = criteriaBuilder.and(userTypePredicate);
							}
						}
	


					} else {
						searchObject = null;
					}
					Order proTimeOrder = criteriaBuilder.desc((root.get("cdate")));
					query.orderBy(proTimeOrder);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				return finalPredicate;
			}
		};
	}

	
}
