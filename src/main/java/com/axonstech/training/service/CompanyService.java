package com.axonstech.training.service;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.entity.Company;
import com.axonstech.training.entity.Employee;
import com.axonstech.training.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
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

    public Page<CompanyDto> getCompanies(Boolean onlyActives, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if(onlyActives != null ){

            Page<Company> pCompany = companyRepository.findAll(pageable);
            return pCompany.map(company -> {
                CompanyDto dto = new CompanyDto();

                return dto.form(company);
            });
        }

        Page<Company> pCompany = companyRepository.findAll(pageable);
        return pCompany.map(company -> {
            CompanyDto dto = new CompanyDto();

            return dto.form(company);
        });
    }

    public CompanyDto getCompany(String companyCode) {
        CompanyDto companyDto = new CompanyDto();

        Optional<Company> oCompany = companyRepository.findByCompanyCode(companyCode);
        if(oCompany.isPresent()){
          return  companyDto.form(oCompany.get());
        }

        return companyDto;
    }

    public CompanyDto saveComp(CompanyDto companyDto) throws Exception {
        Optional<Company> oCompany = companyRepository.findByCompanyName(companyDto.getCompanyName());
        if(oCompany.isPresent()){
            throw new Exception("company name is already taken");
        }

        Company company = new Company();

        BeanUtils.copyProperties(companyDto, company);

        company = companyRepository.save(company);
        return companyDto.form(company);

    }

    public CompanyDto updateComp(CompanyDto companyDto) {
        Company company = new Company();
        BeanUtils.copyProperties(company, companyDto);

        company = companyRepository.save(company);

        return companyDto.form(company);
    }

    public void deleteComp(String id) {
        companyRepository.deleteById(id);
    }


}
