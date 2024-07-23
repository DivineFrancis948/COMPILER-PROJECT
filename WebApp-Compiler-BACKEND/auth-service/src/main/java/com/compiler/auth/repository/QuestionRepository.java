package com.compiler.auth.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compiler.auth.entity.QuestionEntity;


@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, String>
{
    @Query("SELECT COUNT(q) FROM QuestionEntity q")
    long countAllQuestions();
    
    long countByQuestiontype(String questiontype);

    Page<QuestionEntity> findAll(Specification<QuestionEntity> spec, Pageable pageable);
	List<QuestionEntity> findAll(Specification<QuestionEntity> spec);
}
