package com.axonstech.training.controller;

import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.entity.Employee;
import com.axonstech.training.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<Employee> getAllEmp(@RequestParam(name = "onlyActive", required = false) Boolean onlyActives,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size) {

        return employeeService.getEmployees(onlyActives, page, size);
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmp(@PathVariable Long id){
        return employeeService.getEmployee(id);
    }

    @PostMapping
    public Employee saveEmp(@RequestBody Employee employee) throws Exception {
        return employeeService.saveEmp(employee);
    }

    @PutMapping
    public Employee updateEmp(@RequestBody Employee employee){
        return employeeService.updateEmp(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmp(@PathVariable Long id) {
        employeeService.deleteEmp(id);
    }


    @PostMapping("/{userId}/company/{compCode}")
    public void addToCompany(@PathVariable Long userId, @PathVariable String compCode) throws Exception {
        employeeService.addToCompany(userId, compCode);
    }
}
