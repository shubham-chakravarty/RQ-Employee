package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
     * Retrieves a list of employees whose names contain the specified search string.
     * The search is case-sensitive, and any employee whose {@code employeeName} includes
     * the provided substring will be returned. If no matches are found, this returns
     * an empty list.
     *
     * @param searchString the substring to search for in employee names.
     * @return a {@link ResponseEntity} containing a list of {@link EmployeeDTO} whose names
     *         match the search criteria. Returns HTTP 200 OK with an empty list if no matches.
     */
    @Override
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByNameSearch(String searchString) {
        log.info("Received request to search employees by name: {}", searchString);
        List<EmployeeDTO> filteredEmployees = employeeService.findEmployeesByName(searchString);
        log.debug("Found {} employees matching search string '{}'", filteredEmployees.size(), searchString);
        return ResponseEntity.ok(filteredEmployees);
    }

    /**
     * Retrieves a single employee by their ID.
     * Returns a 200 OK with the employee data if found.
     * If not found, returns 404 Not Found (handled by {@link EmployeeControllerAdvice}).
     *
     * @param id the employee's unique identifier
     * @return a {@link ResponseEntity} containing the {@link EmployeeDTO} if found
     */
    @Override
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id) {
        log.info("Received request to get employee by id: {}", id);
        EmployeeDTO employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Retrieves the highest salary among all employees.
     * <p>
     * This endpoint calls the service layer to fetch all employees and determine the maximum
     * salary value. If no employees are found, it returns 0.
     *
     * @return a {@link ResponseEntity} containing the highest salary (an integer).
     *         Returns HTTP 200 OK with 0 if no employees exist.
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Received request to get highest salary of employees");
        int highestSalary = employeeService.findHighestSalary();
        log.debug("Highest salary found: {}", highestSalary);
        return ResponseEntity.ok(highestSalary);
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
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid CreateEmployeeInput employeeInput) {
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
