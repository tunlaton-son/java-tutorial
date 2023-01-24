package com.axonstech.training.dto.request;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import com.axonstech.training.entity.Employee;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class SaveEmployeeRequest implements Serializable {
    private Long id;
    private  String username;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  Boolean active;
    private  Integer version;
    private String password;
    private CompanyDto companyDto;

    public static SaveEmployeeRequest form(Employee employee){

        SaveEmployeeRequest dto = new SaveEmployeeRequest();
        BeanUtils.copyProperties(employee, dto);

        CompanyDto companyDto = new CompanyDto();
        if(employee.getCompany() != null){
            BeanUtils.copyProperties(employee.getCompany(), companyDto);
            dto.setCompanyDto(companyDto);
        }
        return dto;

    }

}
