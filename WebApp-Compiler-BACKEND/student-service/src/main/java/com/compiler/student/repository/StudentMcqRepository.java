package com.compiler.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compiler.student.entity.StudentMcqEmbedded;
import com.compiler.student.entity.StudentMcqEntity;

@Repository
public interface StudentMcqRepository extends JpaRepository<StudentMcqEntity, StudentMcqEmbedded>
{

	 List<StudentMcqEntity> findByStudentMcqEmbeddedUserName(String userName);
	
}
