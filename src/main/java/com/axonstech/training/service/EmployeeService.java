package com.axonstech.training.service;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.dto.request.SaveEmployeeRequest;
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

                return dto.form(employee);
            });

        }

        Page<Employee> pEmployee = employeeRepository.findAll(pageable);

        return pEmployee.map(employee -> {
            EmployeeDto dto = new EmployeeDto();

            return dto.form(employee);
        });
    }

    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        EmployeeDto employeeDto = new EmployeeDto();
        if(employee != null){

            BeanUtils.copyProperties(employee , employeeDto);

            if(employee.getCompany() != null){
                CompanyDto companyDto = new CompanyDto();
                BeanUtils.copyProperties(employee.getCompany(), companyDto);
                employeeDto.setCompanyDto(companyDto);
            }

        }

        return employeeDto;
    }

    public SaveEmployeeRequest saveEmp(SaveEmployeeRequest employeeDto) throws Exception {
        Optional<Employee> oEmployee = employeeRepository.findByUsernameIgnoreCase(employeeDto.getUsername());
        if(oEmployee.isPresent()){
            throw new Exception("username is already taken");
        }

        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDto, employee);

        employee = employeeRepository.save(employee);
        return employeeDto.form(employee);
    }

    public EmployeeDto updateEmp(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto, employee);

        employee = employeeRepository.save(employee);


        return employeeDto.form(employee);
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
