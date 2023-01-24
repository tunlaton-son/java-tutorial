package com.axonstech.training.controller;

import com.axonstech.training.entity.Company;
import com.axonstech.training.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public Page<Company> getCompanies(@RequestParam(name = "onlyActive", required = false) Boolean onlyActives,
                                      @RequestParam(required = false, defaultValue = "1") int page,
                                      @RequestParam(required = false, defaultValue = "10") int size) {

        return companyService.getCompanies(onlyActives, page, size);
    }

    @GetMapping("/{id}")
    public Company getComp(@PathVariable String id){
        return companyService.getCompany(id);
    }

    @PostMapping
    public Company saveComp(@RequestBody Company company) throws Exception {
        return companyService.saveComp(company);
    }

    @PutMapping
    public Company updateComp(@RequestBody Company company){
        return companyService.updateComp(company);
    }

    @DeleteMapping("/{id}")
    public void deleteComp(@PathVariable String id) {
        companyService.deleteComp(id);
    }
}
