package com.reliaquest.api.service;

import com.reliaquest.api.client.ExternalApiClient;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService {

    private ExternalApiClient externalApiClient;

    /**
     * Retrieves a comprehensive list of all employees by invoking the external API client.
     * The service layer does not itself apply filtering or caching for this operation. Its main
     * role is to coordinate between the controller and the external client. If no data is
     * returned by the external API, it gracefully returns an empty list.
     *
     * @return a list of {@link EmployeeDTO}, possibly empty if the external source returns none.
     */
    public List<EmployeeDTO> findAllEmployees() {
        log.info("Fetching all employees via ExternalEmployeeApiClient");
        List<EmployeeDTO> employees = externalApiClient.getAllEmployees();
        log.debug("Found {} employees from mock API", employees.size());
        return employees;
    }

    /**
     * Retrieves and filters employees whose {@code employeeName} contains the given substring.
     * This method delegates to the external API client to fetch all employees, then applies
     * an in-memory filter. If no employees match, an empty list is returned.
     *
     * @param searchString the substring to search for within employee names.
     * @return a filtered list of {@link EmployeeDTO}. May be empty if no matches found.
     */
    public List<EmployeeDTO> findEmployeesByName(String searchString) {
        log.info("Searching employees by name containing '{}'", searchString);
        List<EmployeeDTO> allEmployees = externalApiClient.getAllEmployees();
        List<EmployeeDTO> filtered = allEmployees.stream()
                .filter(e -> e.getEmployeeName() != null && e.getEmployeeName().contains(searchString))
                .collect(Collectors.toList());
        log.debug("Filtered list size: {}", filtered.size());
        return filtered;
    }

    /**
     * Finds an employee by their unique identifier.
     * If the employee is not found, throws {@link EmployeeNotFoundException}.
     *
     * @param id the employee's unique identifier
     * @return the corresponding {@link EmployeeDTO} if found
     */
    public EmployeeDTO findEmployeeById(String id) {
        log.info("Attempting to find employee with id: {}", id);
        EmployeeDTO employee = externalApiClient.getEmployeeById(id);
        if (employee == null) {
            log.debug("Employee with id {} not found", id);
            throw new EmployeeNotFoundException(id);
        }
        log.debug("Employee with id {} found: {}", id, employee.getEmployeeName());
        return employee;
    }

    /**
     * Finds the highest salary among all employees.
     * Retrieves all employees from the external client and calculates the maximum salary.
     * If no employees or salaries are available, returns 0.
     *
     * @return the highest salary as an integer, or 0 if no employees exist.
     */
    public int findHighestSalary() {
        log.info("Fetching all employees to determine highest salary");
        List<EmployeeDTO> employees = externalApiClient.getAllEmployees();
        if (employees.isEmpty()) {
            log.debug("No employees found. Returning 0 as highest salary.");
            return 0;
        }
        int maxSalary = employees.stream()
                .filter(e -> e.getEmployeeSalary() != null)
                .mapToInt(EmployeeDTO::getEmployeeSalary)
                .max()
                .orElse(0);
        log.debug("Highest salary determined as: {}", maxSalary);
        return maxSalary;
    }

    /**
     * Finds the highest N earning employees by salary and returns their names.
     * <p>
     * This method fetches all employees from the external source, filters out those without a valid salary,
     * sorts the remaining employees by salary in descending order, and then returns the names of up to
     * <code>countOfRecords</code> of these top earners. If there are fewer than <code>countOfRecords</code> employees
     * available, it returns as many as it can. If no employees have a valid salary, it returns an empty list.
     *
     * @param countOfRecords the number of top earners to retrieve
     * @return a list of up to <code>countOfRecords</code> employee names sorted by their salary in descending order
     */
    public List<String> findTopTenHighestEarningNames(int countOfRecords) {
        log.info("Fetching all employees to determine top {} highest earners",countOfRecords);
        List<EmployeeDTO> employees = externalApiClient.getAllEmployees();

        List<String> topEarners = employees.stream()
                .filter(e -> e.getEmployeeSalary() != null)
                .sorted((a, b) -> b.getEmployeeSalary().compareTo(a.getEmployeeSalary()))
                .limit(countOfRecords)
                .map(EmployeeDTO::getEmployeeName)
                .collect(Collectors.toList());

        log.debug("Top earners found: {}", topEarners);
        return topEarners;
    }

    /**
     * Calls the external API to create a new employee with the given input details.
     * <p>
     * If the external API call is successful, returns the newly created employee’s data.
     * If the external API returns an error, propagates that error. Handling can be done
     * via a global exception handler.
     *
     * @param input the employee creation request data
     * @return the created {@link EmployeeDTO} from the external API
     */
    public EmployeeDTO createEmployee(CreateEmployeeInput input) {
        log.info("Creating employee via external API: {}", input.getName());
        EmployeeDTO created = externalApiClient.createEmployee(input);
        log.debug("Created employee: {}", created);
        return created;
    }

    /**
     * Deletes an employee by their ID by first fetching the employee and then
     * deleting it by their name (as required by the external API).
     *
     * @param id the ID of the employee to delete.
     * @return a success message if the employee is deleted successfully.
     * @throws EmployeeNotFoundException if the employee is not found or has no valid name.
     */
    public String deleteEmployeeById(String id) {
        // Fetch employee by ID
        EmployeeDTO employee = externalApiClient.getEmployeeById(id);

        if (employee == null || employee.getEmployeeName() == null) {
            log.warn("Employee with ID {} not found or does not have a valid name for deletion.", id);
            throw new EmployeeNotFoundException(id);
        }

        // Delete employee by name
        externalApiClient.deleteEmployeeByName(employee.getEmployeeName());
        String successMessage = "Employee with ID " + id + " and name " + employee.getEmployeeName() + " deleted successfully.";
        log.info(successMessage);
        return successMessage;
    }


}
