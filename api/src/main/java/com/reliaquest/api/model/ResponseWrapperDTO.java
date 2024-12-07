package com.reliaquest.api.model;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class ResponseWrapperDTO {
    private List<EmployeeDTO> data;
    private String status;
}
