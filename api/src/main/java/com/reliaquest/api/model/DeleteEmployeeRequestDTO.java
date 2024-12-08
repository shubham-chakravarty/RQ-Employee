package com.reliaquest.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteEmployeeRequestDTO {

    @NotBlank
    private String name;
}
