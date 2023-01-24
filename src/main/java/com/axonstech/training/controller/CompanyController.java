package com.axonstech.training.controller;

import com.axonstech.training.dto.CompanyDto;
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
    public Page<CompanyDto> getCompanies(@RequestParam(name = "onlyActive", required = false) Boolean onlyActives,
                                         @RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {

        return companyService.getCompanies(onlyActives, page, size);
    }

    @GetMapping("/{companyCode}")
    public CompanyDto getComp(@PathVariable String companyCode){
        return companyService.getCompany(companyCode);
    }

    @PostMapping
    public CompanyDto saveComp(@RequestBody CompanyDto companyDto) throws Exception {
        return companyService.saveComp(companyDto);
    }

    @PutMapping
    public CompanyDto updateComp(@RequestBody CompanyDto companyDto){
        return companyService.updateComp(companyDto);
    }

    @DeleteMapping("/{id}")
    public void deleteComp(@PathVariable String id) {
        companyService.deleteComp(id);
    }
}
