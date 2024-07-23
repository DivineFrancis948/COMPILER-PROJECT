package com.compiler.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.student.entity.RegistrationEntity;


@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String>
{
	Optional<RegistrationEntity> findIdByEmail(String username);
	
	Page<RegistrationEntity> findAll(Specification<RegistrationEntity> spec, Pageable pageable);
    List<RegistrationEntity> findByUserType(String userType);
    long countByUserType(String userType);


	List<RegistrationEntity> findAll(Specification<RegistrationEntity> spec);
	
	Optional<RegistrationEntity> findByPhoneNumber(String phoneNumber);
	Optional<RegistrationEntity> findByEmail(String email);
}