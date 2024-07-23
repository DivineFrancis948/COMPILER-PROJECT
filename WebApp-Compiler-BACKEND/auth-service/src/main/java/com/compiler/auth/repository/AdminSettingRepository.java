package com.compiler.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compiler.auth.entity.AdminEntity;


public interface AdminSettingRepository extends JpaRepository<AdminEntity, Long>{

}
