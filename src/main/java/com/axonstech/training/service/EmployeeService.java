package com.axonstech.training.service;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.entity.Employee;
import com.axonstech.training.repository.EmployeeRepository;
import com.axonstech.training.entity.Company;
import com.axonstech.training.repository.CompanyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    public Page<EmployeeDto> getEmployees(Boolean onlyActives, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if(onlyActives != null ){
            Page<Employee> pEmployee = employeeRepository.findByActive(onlyActives.booleanValue(), pageable);
            return pEmployee.map(e -> {
                EmployeeDto dto = new EmployeeDto();
                BeanUtils.copyProperties(e, dto);
                return dto;
            });
        }
        return employeeRepository.findAll(pageable).map(e -> {
            EmployeeDto dto = new EmployeeDto();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }

    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        EmployeeDto employeeDto = new EmployeeDto();
        BeanUtils.copyProperties(employee, employeeDto);
        Company company = employee.getCompany();
        CompanyDto companyDto = new CompanyDto();
        BeanUtils.copyProperties(company, companyDto);
        employeeDto.setCompany(companyDto);
        return employeeDto;
    }

    public Employee save(Employee employee) throws Exception {
        Optional<Employee> oEmployee = employeeRepository.findByUsernameIgnoreCase(employee.getUsername());
        if(oEmployee.isPresent()){
            throw new Exception("username is already taken");
        }
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee addToCompany(Long userId, String companyCode) throws Exception {
        Optional<Employee> oEmployee = employeeRepository.findById(userId);
        if(oEmployee.isEmpty()){
            throw new Exception("not found user");
        }
        Optional<Company> oCompany = companyRepository.findById(companyCode);
        if(oCompany.isEmpty()){
            throw new Exception("not found company");
        }

        Employee employee = oEmployee.get();
        employee.setCompany(oCompany.get());

        return employeeRepository.save(employee);
    }
}
