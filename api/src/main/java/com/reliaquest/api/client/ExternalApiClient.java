package com.reliaquest.api.client;

import com.reliaquest.api.constants.ApiConstants;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.ResponseWrapperDTO;
import com.reliaquest.api.model.SingleEmployeeResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ExternalApiClient {
    private final RestTemplate restTemplate;

    @Value("${mock.base.url}")
    private String BASE_URL;

    @Autowired
    public ExternalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Contacts the external mock employee API to retrieve all employee records.
     * This client encapsulates the remote API call, translating the raw response into a
     * structured list of {@link EmployeeDTO}. If the remote server returns no data or
     * an unexpected structure, this method logs a warning and returns an empty list.
     *
     * @return a list of {@link EmployeeDTO} objects as provided by the external API;
     *         never null, but may be empty if no employees are returned.
     */
    public List<EmployeeDTO> getAllEmployees() {
        String url = BASE_URL + ApiConstants.EMPLOYEE_BASE_API;
        log.info("GET {}", url);
        ResponseWrapperDTO response = restTemplate.getForObject(url, ResponseWrapperDTO.class);

        if (response == null || response.getData() == null) {
            log.warn("No data returned from server for getAllEmployees(). Returning empty list.");
            return Collections.emptyList();
        }

        return response.getData();
    }


    /**
     * Retrieves a single employee by ID from the external mock API.
     * <p>
     * If the API returns 404, this method will return null.
     * Otherwise, it returns the {@link EmployeeDTO} retrieved.
     *
     * @param id the unique identifier of the employee to fetch.
     * @return the {@link EmployeeDTO} if found, or null if not found.
     */
    public EmployeeDTO getEmployeeById(String id) {
        String url = BASE_URL + ApiConstants.EMPLOYEE_BASE_API + ApiConstants.PATH_SEPARATOR + id;
        log.info("GET {}", url);
        try {
            SingleEmployeeResponseDTO response = restTemplate.getForObject(url, SingleEmployeeResponseDTO.class);
            if (response == null || response.getData() == null) {
                log.warn("No data returned from server for getEmployeeById({}). Returning null.", id);
                return null;
            }
            return response.getData();
        } catch (HttpClientErrorException.NotFound ex) {
            //handles RestTemplate's 404
            log.warn("Employee not found on external API for id: {}", id);
            return null;
        }
    }
}
