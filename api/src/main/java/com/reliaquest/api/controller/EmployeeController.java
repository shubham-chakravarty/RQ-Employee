package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeController implements IEmployeeController<EmployeeDTO, CreateEmployeeInput> {

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return null;
    }

    /**
     * @param searchString
     * @return
     */
    @Override
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByNameSearch(String searchString) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<EmployeeDTO> getEmployeeById(String id) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    /**
     * @param employeeInput
     * @return
     */
    @Override
    public ResponseEntity<EmployeeDTO> createEmployee(CreateEmployeeInput employeeInput) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
}
