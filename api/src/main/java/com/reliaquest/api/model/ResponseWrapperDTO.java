package com.reliaquest.api.model;

import java.util.List;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapperDTO {
    private List<EmployeeDTO> data;
    private String status;
}
