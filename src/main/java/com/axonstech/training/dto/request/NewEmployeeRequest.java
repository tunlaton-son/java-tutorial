package com.axonstech.training.dto.request;

import com.axonstech.training.dto.CompanyDto;
import com.axonstech.training.dto.EmployeeDto;
import lombok.Data;

@Data
public class NewEmployeeRequest{
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
}