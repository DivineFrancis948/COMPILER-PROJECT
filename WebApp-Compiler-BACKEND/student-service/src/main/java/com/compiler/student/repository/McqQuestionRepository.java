package com.compiler.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.compiler.student.entity.McqQuestionEntity;




@Repository
public interface McqQuestionRepository extends JpaRepository<McqQuestionEntity, String>
{

}
