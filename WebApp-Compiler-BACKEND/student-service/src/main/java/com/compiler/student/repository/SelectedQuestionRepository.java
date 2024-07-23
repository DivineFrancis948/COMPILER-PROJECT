package com.compiler.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compiler.student.entity.SelectedQuestionEntity;



public interface SelectedQuestionRepository extends JpaRepository<SelectedQuestionEntity,String>{

}
