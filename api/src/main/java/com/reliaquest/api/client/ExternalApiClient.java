package com.reliaquest.api.client;

import com.reliaquest.api.constants.ApiConstants;
import com.reliaquest.api.model.*;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ExternalApiClient implements IExternalApiClient {
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
            // handles RestTemplate's 404
            log.warn("Employee not found on external API for id: {}", id);
            return null;
        }
    }

    /**
     * Creates a new employee via the external mock API.
     * <p>
     * Sends a POST request with the given input data. On success, returns the
     * {@link EmployeeDTO} of the newly created employee. If the external API responds
     * with an error (e.g., validation failure or other issue), a
     * {@link HttpClientErrorException} or {@link HttpServerErrorException} will be thrown.
     *
     * @param input the data needed to create a new employee
     * @return the {@link EmployeeDTO} returned by the external API after creation
     */
    public EmployeeDTO createEmployee(CreateEmployeeInput input) {
        String url = BASE_URL + ApiConstants.EMPLOYEE_BASE_API;
        log.info("POST {}", url);

        SingleEmployeeResponseDTO response = restTemplate.postForObject(url, input, SingleEmployeeResponseDTO.class);

        if (response == null || response.getData() == null) {
            log.warn("No data returned from external API after employee creation.");
            throw new IllegalStateException("Employee creation failed: no response data.");
        }

        return response.getData();
    }

    /**
     * Deletes an employee by their name via the external API.
     *
     * @param name the name of the employee to delete.
     */
    public void deleteEmployeeByName(String name) {
        String url = BASE_URL + ApiConstants.EMPLOYEE_BASE_API;
        log.info("DELETE {} with name {}", url, name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeleteEmployeeRequestDTO> request = new HttpEntity<>(new DeleteEmployeeRequestDTO(name), headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
    }
}
