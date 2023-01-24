package com.axonstech.training.dto;

import com.axonstech.training.entity.Company;
import com.axonstech.training.entity.Employee;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * A DTO for the {@link com.axonstech.training.entity.Employee} entity
 */
@Data
public class EmployeeDto implements Serializable {
    private Long id;
    private  String username;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  Boolean active;
    private  Integer version;
    private CompanyDto companyDto;

    public static EmployeeDto form(Employee employee){

            EmployeeDto dto = new EmployeeDto();
            BeanUtils.copyProperties(employee, dto);

            CompanyDto companyDto = new CompanyDto();
            if(employee.getCompany() != null){
                BeanUtils.copyProperties(employee.getCompany(), companyDto);
                dto.setCompanyDto(companyDto);
            }
            return dto;

    }


}