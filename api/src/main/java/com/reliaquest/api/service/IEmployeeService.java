package com.reliaquest.api.service;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import java.util.List;

public interface IEmployeeService {

    List<EmployeeDTO> findAllEmployees();

    List<EmployeeDTO> findEmployeesByName(String searchString);

    EmployeeDTO findEmployeeById(String id);

    int findHighestSalary();

    List<String> findTopTenHighestEarningNames(int countOfRecords);

    EmployeeDTO createEmployee(CreateEmployeeInput input);

    String deleteEmployeeById(String id);
}
