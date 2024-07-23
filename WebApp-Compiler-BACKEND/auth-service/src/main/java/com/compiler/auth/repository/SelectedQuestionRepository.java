package com.compiler.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.compiler.auth.entity.SelectedQuestionEntity;


public interface SelectedQuestionRepository extends JpaRepository<SelectedQuestionEntity,String>{

}
