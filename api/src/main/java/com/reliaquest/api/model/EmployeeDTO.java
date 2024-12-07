package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Builder
public class EmployeeDTO {
    private String id;
    private String employeeName;
    private Integer employeeSalary;
    private Integer employeeAge;
    private String employeeTitle;
    private String employeeEmail;
}