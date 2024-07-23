package com.compiler.jdoodle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compiler.jdoodle.entity.QuestionEntity;



@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, String>
{
    @Query("SELECT MAX(qe.id) FROM QuestionEntity qe")
    Long findMaxId();
}
