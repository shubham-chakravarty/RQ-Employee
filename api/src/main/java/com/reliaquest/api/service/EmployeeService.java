package com.reliaquest.api.service;

import com.reliaquest.api.client.ExternalApiClient;
import com.reliaquest.api.model.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
     * <p>
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

}
