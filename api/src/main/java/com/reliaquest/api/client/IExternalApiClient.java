package com.reliaquest.api.client;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import java.util.List;

public interface IExternalApiClient {
    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(String id);

    EmployeeDTO createEmployee(CreateEmployeeInput input);

    void deleteEmployeeByName(String name);
}
