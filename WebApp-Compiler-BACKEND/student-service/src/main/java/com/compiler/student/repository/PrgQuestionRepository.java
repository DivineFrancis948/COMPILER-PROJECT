package com.compiler.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.student.entity.PrgQuestionEntity;


@Repository
public interface PrgQuestionRepository extends JpaRepository<PrgQuestionEntity, String>
{

}
