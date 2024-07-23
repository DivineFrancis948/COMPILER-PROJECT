package com.compiler.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.compiler.question.entity.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, String>
{
    @Query("SELECT COUNT(q) FROM QuestionEntity q")
    long countAllQuestions();
    List<QuestionEntity> findByQuestiontype(String questionType);
    long countByQuestiontype(String questiontype);

}
