package com.compiler.jdoodle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.compiler.jdoodle.entity.StudentPrgEmbeddedId;
import com.compiler.jdoodle.entity.StudentPrgEntity;


@Repository
public interface StudentPrgRepository extends JpaRepository<StudentPrgEntity, StudentPrgEmbeddedId>
{
	


}
