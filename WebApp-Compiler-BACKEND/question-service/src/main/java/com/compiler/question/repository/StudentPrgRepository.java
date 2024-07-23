package com.compiler.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.compiler.question.entity.StudentPrgEmbeddedId;
import com.compiler.question.entity.StudentPrgEntity;



@Repository
public interface StudentPrgRepository extends JpaRepository<StudentPrgEntity, StudentPrgEmbeddedId>
{
	@Query("SELECT SUM(sp.totalmark) FROM StudentPrgEntity sp WHERE sp.embeddedid.username = :username")
    Long findTotalmarkByUsername(@Param("username") String username);

}
