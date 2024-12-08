package com.reliaquest.api.model;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SingleEmployeeResponseDTO {
    private EmployeeDTO data;
    private String status;
}
