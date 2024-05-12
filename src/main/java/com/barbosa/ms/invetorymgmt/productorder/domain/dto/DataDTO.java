package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class DataDTO {
    
    @NotNull(message = "{field.description.required}")
    @NotBlank(message = "{field.description.not-blank}")
    @NotEmpty(message = "{field.description.required}")
    private String description;
    
}
