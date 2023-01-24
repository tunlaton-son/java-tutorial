package com.axonstech.training.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.axonstech.training.entity.Company} entity
 */
@Data
public class CompanyDto implements Serializable {
    private  String companyCode;
    private  String companyName;
    private  List<EmployeeDto> employees;
}