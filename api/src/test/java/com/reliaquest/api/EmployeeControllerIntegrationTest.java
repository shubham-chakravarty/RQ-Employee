package com.reliaquest.api;

import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.ResponseWrapperDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private final String MOCK_SERVER_URL = "http://localhost:8112/api/v1/employee";
    private final String APP_URL = "http://localhost:8111/v1/employees";

    @Test
    void getAllEmployees_returnListOfEmployees_shouldReturnListOfEmployees() throws Exception {
        List<EmployeeDTO> mockEmployees = new ArrayList<>();
        EmployeeDTO emp1 = EmployeeDTO.builder()
                .id("4a3a170b-22cd-4ac2-aad1-9bb5b34a1507")
                .employeeName("Tiger Nixon")
                .employeeSalary(320800)
                .employeeAge(61)
                .employeeTitle("Vice Chair Executive Principal of Chief Operations Implementation Specialist")
                .employeeEmail("tnixon@company.com")
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
                        .andExpect(jsonPath("$[0].employee_name").value("Tiger Nixon"))
                        .andExpect(jsonPath("$[0].employee_salary").value(320800))
                        .andExpect(jsonPath("$[0].employee_age").value(61))
                        .andExpect(jsonPath("$[0].employee_title").value("Vice Chair Executive Principal of Chief Operations Implementation Specialist"))
                        .andExpect(jsonPath("$[0].employee_email").value("tnixon@company.com"));
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
}
