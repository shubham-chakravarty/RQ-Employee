package com.reliaquest.api.service;

import com.reliaquest.api.client.ExternalApiClient;
import com.reliaquest.api.model.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
