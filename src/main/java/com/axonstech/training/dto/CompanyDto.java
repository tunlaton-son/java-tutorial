package com.axonstech.training.dto;

import com.axonstech.training.entity.Company;
import com.axonstech.training.entity.Employee;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link com.axonstech.training.entity.Company} entity
 */
@Data
public class CompanyDto implements Serializable {
    private  String companyCode;
    private  String companyName;
    private  List<EmployeeDto> employees;

    public static CompanyDto form(Company company){

        CompanyDto dto = new CompanyDto();
        BeanUtils.copyProperties(company, dto);

        List<EmployeeDto> employeeDtos = new ArrayList<>();
        if(company.getEmployees() != null){
            BeanUtils.copyProperties(company.getEmployees(), employeeDtos);
            dto.setEmployees(employeeDtos);
        }
        return dto;

    }
}