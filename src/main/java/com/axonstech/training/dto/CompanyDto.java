package com.axonstech.training.dto;

import com.axonstech.training.entity.Company;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Company} entity
 */
@Data
public class CompanyDto implements Serializable {
    private String companyCode;
    private String companyName;
}
