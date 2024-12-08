package com.reliaquest.api;

import com.reliaquest.api.constants.ApiConstants;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.ResponseWrapperDTO;
import com.reliaquest.api.model.SingleEmployeeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private final String MOCK_SERVER_URL = "http://localhost:8112/api/v1/employee";
    private final String APP_URL = "/v1/employees";
    private final String PATH_SEPARATOR = "/";

    @Test
    void getAllEmployees_returnListOfEmployees_shouldReturnListOfEmployees() throws Exception {
        List<EmployeeDTO> mockEmployees = new ArrayList<>();
        String employeeName = "Tiger Nixon";
        String uuid = "4a3a170b-22cd-4ac2-aad1-9bb5b34a1507";
        String employeeTitle = "Vice Chair Executive Principal of Chief Operations Implementation Specialist";
        String mail = "tnixon@company.com";
        int employeeSalary = 320800;
        int employeeAge = 61;

        EmployeeDTO emp1 = EmployeeDTO.builder()
                .id(uuid)
                .employeeName(employeeName)
                .employeeSalary(employeeSalary)
                .employeeAge(employeeAge)
                .employeeTitle(employeeTitle)
                .employeeEmail(mail)
                .build();

        mockEmployees.add(emp1);

        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(mockEmployees)
                .status("Successfully processed request.").build();

        given(restTemplate.getForObject(MOCK_SERVER_URL, ResponseWrapperDTO.class))
                .willReturn(mockResponse);

        ResultActions perform = mockMvc.perform(get(APP_URL)
                .contentType(MediaType.APPLICATION_JSON));
                perform.andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].employee_name").value(employeeName))
                        .andExpect(jsonPath("$[0].employee_salary").value(employeeSalary))
                        .andExpect(jsonPath("$[0].employee_age").value(employeeAge))
                        .andExpect(jsonPath("$[0].employee_title").value(employeeTitle))
                        .andExpect(jsonPath("$[0].employee_email").value(mail));
    }

    @Test
    void getAllEmployees_returnNoEmployees_shouldReturnEmptyList() throws Exception {

        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(null)
                .status("Successfully processed request.").build();


        given(restTemplate.getForObject(MOCK_SERVER_URL, ResponseWrapperDTO.class))
                .willReturn(mockResponse);

        mockMvc.perform(get(APP_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getEmployeesByNameSearch_noMatches_returnsEmptyList() throws Exception {
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(
                        EmployeeDTO.builder().employeeName("Alice").build(),
                        EmployeeDTO.builder().employeeName("Bob").build()
                ))
                .status("OK").build();

        given(restTemplate.getForObject(MOCK_SERVER_URL, ResponseWrapperDTO.class))
                .willReturn(mockResponse);

        String url = APP_URL + ApiConstants.SEARCH_ENDPOINT + "/Z";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getEmployeesByNameSearch_oneMatch_returnsThatOne() throws Exception {
        EmployeeDTO emp1 = EmployeeDTO.builder().employeeName("Alice").build();
        EmployeeDTO emp2 = EmployeeDTO.builder().employeeName("Bob").build();
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(emp1, emp2))
                .status("OK").build();

        given(restTemplate.getForObject(MOCK_SERVER_URL, ResponseWrapperDTO.class))
                .willReturn(mockResponse);

        String url = APP_URL + ApiConstants.SEARCH_ENDPOINT + "/Ali";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employee_name").value("Alice"));
    }

    @Test
    void getEmployeesByNameSearch_noEmployeesReturned_emptyList() throws Exception {
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Collections.emptyList())
                .status("OK").build();

        given(restTemplate.getForObject(MOCK_SERVER_URL, ResponseWrapperDTO.class))
                .willReturn(mockResponse);
        String url = APP_URL + ApiConstants.SEARCH_ENDPOINT + "/Ali";
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getEmployeeById_found_returnsEmployee() throws Exception {
        String id = UUID.randomUUID().toString();
        EmployeeDTO emp = EmployeeDTO.builder().id(id).employeeName("Jane Doe").build();
        SingleEmployeeResponseDTO response = new SingleEmployeeResponseDTO();
        response.setData(emp);
        response.setStatus("OK");

        String mockUrl = MOCK_SERVER_URL + PATH_SEPARATOR + id;
        given(restTemplate.getForObject(mockUrl, SingleEmployeeResponseDTO.class))
                .willReturn(response);

        String url = APP_URL + PATH_SEPARATOR + id;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.employee_name").value("Jane Doe"));
    }

    @Test
    void getEmployeeById_notFound_returns404() throws Exception {
        String id = "non-existent-id";
        String mockUrl = MOCK_SERVER_URL + PATH_SEPARATOR + id;

        given(restTemplate.getForObject(eq(mockUrl), eq(SingleEmployeeResponseDTO.class)))
                //creating NOT FOUND Exception
                .willThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND,
                        "Not Found",
                        HttpHeaders.EMPTY,
                        null,
                        null
                ));

        String url = APP_URL + PATH_SEPARATOR + id;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found for ID: " + id));
    }

    @Test
    void getHighestSalary_noEmployees_returns0() throws Exception {
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Collections.emptyList())
                .status("OK").build();

        String url = APP_URL + ApiConstants.HIGHEST_SALARY_ENDPOINT;
        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class)))
                .willReturn(mockResponse);

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void getHighestSalary_multipleEmployees_returnsMax() throws Exception {
        EmployeeDTO emp1 = EmployeeDTO.builder().employeeSalary(50000).build();
        EmployeeDTO emp2 = EmployeeDTO.builder().employeeSalary(100000).build();
        EmployeeDTO emp3 = EmployeeDTO.builder().employeeSalary(75000).build();
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(emp1, emp2, emp3))
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class)))
                .willReturn(mockResponse);

        String url = APP_URL + ApiConstants.HIGHEST_SALARY_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100000"));
    }

    @Test
    void getHighestSalary_allNullSalaries_returns0() throws Exception {
        EmployeeDTO emp1 = EmployeeDTO.builder().employeeSalary(null).build();
        EmployeeDTO emp2 = EmployeeDTO.builder().employeeSalary(null).build();
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(emp1, emp2))
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class)))
                .willReturn(mockResponse);

        String url = APP_URL + ApiConstants.HIGHEST_SALARY_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_noEmployees_returnsEmptyList() throws Exception {
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Collections.emptyList())
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class))).willReturn(mockResponse);

        String url = APP_URL + ApiConstants.TOP_EARNING_SALARY_EMPLOYEES_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_lessThanTenEmployees_returnsAllInDescendingOrder() throws Exception {
        EmployeeDTO emp1 = EmployeeDTO.builder().employeeName("Alice").employeeSalary(1000).build();
        EmployeeDTO emp2 = EmployeeDTO.builder().employeeName("Bob").employeeSalary(2000).build();
        EmployeeDTO emp3 = EmployeeDTO.builder().employeeName("Charlie").employeeSalary(1500).build();
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(emp1, emp2, emp3))
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class))).willReturn(mockResponse);

        String url = APP_URL + ApiConstants.TOP_EARNING_SALARY_EMPLOYEES_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("Bob"))
                .andExpect(jsonPath("$[1]").value("Charlie"))
                .andExpect(jsonPath("$[2]").value("Alice"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_moreThanTenEmployees_returnsTopTen() throws Exception {
        // 12 employees with ascending salaries
        EmployeeDTO[] employees = new EmployeeDTO[12];
        for (int i = 1; i <= 12; i++) {
            employees[i-1] = EmployeeDTO.builder().employeeName("E" + i).employeeSalary(i * 1000).build();
        }
        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(employees))
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class))).willReturn(mockResponse);

        String url = APP_URL + ApiConstants.TOP_EARNING_SALARY_EMPLOYEES_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0]").value("E12"))
                .andExpect(jsonPath("$[9]").value("E3"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_someNullSalaries_ignored() throws Exception {
        EmployeeDTO emp1 = EmployeeDTO.builder().employeeName("High").employeeSalary(5000).build();
        EmployeeDTO emp2 = EmployeeDTO.builder().employeeName("NullOne").employeeSalary(null).build();
        EmployeeDTO emp3 = EmployeeDTO.builder().employeeName("Low").employeeSalary(1000).build();
        EmployeeDTO emp4 = EmployeeDTO.builder().employeeName("NullTwo").employeeSalary(null).build();

        ResponseWrapperDTO mockResponse = ResponseWrapperDTO.builder()
                .data(Arrays.asList(emp1, emp2, emp3, emp4))
                .status("OK").build();

        given(restTemplate.getForObject(eq(MOCK_SERVER_URL), eq(ResponseWrapperDTO.class))).willReturn(mockResponse);

        String url = APP_URL + ApiConstants.TOP_EARNING_SALARY_EMPLOYEES_ENDPOINT;
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("High"))
                .andExpect(jsonPath("$[1]").value("Low"));
    }
}
