package com.compiler.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.question.entity.RegistrationEntity;


@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String>
{

}
