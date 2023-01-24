package com.axonstech.training.repository;

import com.axonstech.training.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    Page<Company> findAll(Pageable pageable);

    Optional<Company> findByCompanyName(String companyName);




}