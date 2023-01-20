package com.axonstech.training.controller;

import com.axonstech.training.entity.Company;
import com.axonstech.training.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public Page<Company> getAllEmp(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return companyService.getCompanies(page, size);
    }

    @GetMapping("/{id}")
    public Company getEmp(@PathVariable String id){
        return companyService.getCompany(id);
    }

    @PostMapping
    public Company saveEmp(@RequestBody Company company) throws Exception {
        return companyService.save(company);
    }


    @PutMapping
    public Company updateEmp(@RequestBody Company company){
        return companyService.update(company);
    }


    @DeleteMapping("/{id}")
    public void deleteEmp(@PathVariable String id) {
        companyService.delete(id);
    }
}
