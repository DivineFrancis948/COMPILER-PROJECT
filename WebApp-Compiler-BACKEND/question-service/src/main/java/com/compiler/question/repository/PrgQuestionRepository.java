package com.compiler.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.question.entity.PrgQuestionEntity;

@Repository
public interface PrgQuestionRepository extends JpaRepository<PrgQuestionEntity, String>
{

}
