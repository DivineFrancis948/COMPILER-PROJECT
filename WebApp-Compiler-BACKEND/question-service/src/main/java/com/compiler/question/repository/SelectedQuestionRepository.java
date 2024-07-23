package com.compiler.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compiler.question.entity.SelectedQuestionEntity;

public interface SelectedQuestionRepository extends JpaRepository<SelectedQuestionEntity,String>
{
//    boolean existsByUsernameAndMcqqueslistIsEmpty(String username);

}
