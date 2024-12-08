package com.reliaquest.api.controller;

import com.reliaquest.api.constants.ApiConstants;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.service.IEmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/employees")
@Slf4j
@AllArgsConstructor
public class EmployeeController implements IEmployeeController<EmployeeDTO, CreateEmployeeInput> {

    private final IEmployeeService employeeService;

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
     * Retrieves the top 10 highest-earning employee names.
     * <p>
     * This endpoint sorts employees by their salary in descending order and returns
     * the names of the top 10 earners. If there are fewer than 10 employees, it returns
     * the names of all the employees sorted by salary. If no employees or no salaries
     * are available, it returns an empty list.
     *
     * @return a {@link ResponseEntity} containing a list of {@link String} representing
     *         the top 10 highest earners' names. Returns HTTP 200 OK, even if empty.
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Received request to get top 10 highest earning employee names");
        List<String> topEarners =
                employeeService.findTopTenHighestEarningNames(ApiConstants.TOP_EARNERS_COUNT); // default set to 10
        log.debug("Returning {} top earning employee names", topEarners.size());
        return ResponseEntity.ok(topEarners);
    }

    /**
     * Creates a new employee with the given input data.
     * <p>
     * Validates the input fields and, if valid, calls the service layer to create a new employee
     * via the external API. On success, returns the newly created employee’s details with
     * HTTP 201 CREATED. If validation fails, returns 400 Bad Request.
     *
     * @param employeeInput the input data required to create a new employee
     * @return a {@link ResponseEntity} containing the created {@link EmployeeDTO}.
     */
    @Override
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid CreateEmployeeInput employeeInput) {
        log.info("Received request to create employee: {}", employeeInput.getName());
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeInput);
        log.debug("Employee created: {}", createdEmployee.getId());
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete.
     * @return a ResponseEntity with a success message if the deletion is successful,
     *         or a not found status if the employee does not exist.
     */
    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        String message = employeeService.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
