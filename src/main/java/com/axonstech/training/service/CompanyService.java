package com.axonstech.training.service;

import com.axonstech.training.entity.Company;
import com.axonstech.training.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public Page<Company> getCompanies(Boolean onlyActives, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if(onlyActives != null ){
            return companyRepository.findAll(pageable);
        }
        return companyRepository.findAll(pageable);
    }

    public Company getCompany(String id) {
        return companyRepository.findById(id).get();
    }

    public Company saveComp(Company company) throws Exception {
        Optional<Company> oCompany = companyRepository.findByCompanyName(company.getCompanyName());
        if(oCompany.isPresent()){
            throw new Exception("company name is already taken");
        }
        return companyRepository.save(company);
    }

    public Company updateComp(Company company) {
        return companyRepository.save(company);
    }

    public void deleteComp(String id) {
        companyRepository.deleteById(id);
    }


}
