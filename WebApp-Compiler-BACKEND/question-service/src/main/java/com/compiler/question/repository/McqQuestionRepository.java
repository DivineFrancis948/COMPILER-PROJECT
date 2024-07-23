package com.compiler.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.question.entity.McqQuestionEntity;


@Repository
public interface McqQuestionRepository extends JpaRepository<McqQuestionEntity, String>
{

}
