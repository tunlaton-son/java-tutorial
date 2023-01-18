package com.axonstech.training;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public Page<Employee> getAllEmp(@RequestParam(name = "onlyActive", required = false) Boolean onlyActives,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        if(onlyActives != null ){
            return employeeRepository.findByActive(onlyActives.booleanValue(), pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Employee getEmp(Long id){
        return employeeRepository.findById(id).get();
    }

    @PostMapping
    public Employee saveEmp(@RequestBody Employee employee) throws Exception {
        Optional<Employee> oEmployee = employeeRepository.findByUsernameIgnoreCase(employee.getUsername());
        if(oEmployee.isPresent()){
            throw new Exception("username is already taken");
        }
        return employeeRepository.save(employee);
    }

    @PutMapping
    public Employee updateEmp(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmp(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
