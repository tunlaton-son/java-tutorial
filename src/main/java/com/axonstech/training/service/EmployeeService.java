package com.axonstech.training.service;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.entity.Company;
import com.axonstech.training.entity.Employee;
import com.axonstech.training.repository.CompanyRepository;
import com.axonstech.training.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public Page<EmployeeDto> getEmployees(Boolean onlyActives, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if(onlyActives != null ){

            Page<Employee> pEmployee = employeeRepository.findByActive(onlyActives.booleanValue(), pageable);
            return pEmployee.map(employee -> {
                EmployeeDto dto = new EmployeeDto();
                BeanUtils.copyProperties(employee, dto);

                CompanyDto companyDto = new CompanyDto();
                BeanUtils.copyProperties(employee.getCompany(), companyDto);
                dto.setCompanyDto(companyDto);
                return dto;
            });

        }

        Page<Employee> pEmployee = employeeRepository.findAll(pageable);

        return pEmployee.map(employee -> {
            EmployeeDto dto = new EmployeeDto();
            BeanUtils.copyProperties(employee, dto);

            CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(employee.getCompany(), companyDto);
            dto.setCompanyDto(companyDto);
            return dto;
        });
    }

    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        EmployeeDto employeeDto = new EmployeeDto();
        BeanUtils.copyProperties(employee , employeeDto);

        CompanyDto companyDto = new CompanyDto();
        BeanUtils.copyProperties(employee.getCompany(), companyDto);

        employeeDto.setCompanyDto(companyDto);

        return employeeDto;
    }

    public Employee saveEmp(Employee employee) throws Exception {
        Optional<Employee> oEmployee = employeeRepository.findByUsernameIgnoreCase(employee.getUsername());
        if(oEmployee.isPresent()){
            throw new Exception("username is already taken");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmp(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmp(Long id) {
        employeeRepository.deleteById(id);
    }

    public void addToCompany(Long userId, String compCode) throws Exception {

        Optional<Employee> oEmployee = employeeRepository.findById(userId);
        if(oEmployee.isEmpty()){
            throw new Exception("username is not exist");
        }

        Optional<Company> oCompany = companyRepository.findById(compCode);
        if(oEmployee.isEmpty()){
            throw new Exception("company is not exist");
        }

        Employee employee = oEmployee.get();
        Company company = oCompany.get();
        employee.setCompany(company);

        employeeRepository.save(employee);
    }
}
