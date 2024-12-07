package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/employees")
@Slf4j
@AllArgsConstructor
public class EmployeeController implements IEmployeeController<EmployeeDTO, CreateEmployeeInput> {

    private final EmployeeService employeeService;

    /**
     * Handles the HTTP GET request to retrieve all employees.
     * This endpoint returns the full list of employees currently available. It does not apply
     * any filtering or transformation other than delegating the call to the service layer.
     * On success, returns a 200 OK response with a JSON array of employee objects.
     *
     * @return a {@link ResponseEntity} containing a list of {@link EmployeeDTO} objects.
     *         If no employees are present, an empty array is returned with a 200 OK.
     */
    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.info("Received request to get all employees");
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        log.debug("Returning {} employees to the client", employees.size());
        return ResponseEntity.ok(employees);
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
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid CreateEmployeeInput employeeInput) {
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
