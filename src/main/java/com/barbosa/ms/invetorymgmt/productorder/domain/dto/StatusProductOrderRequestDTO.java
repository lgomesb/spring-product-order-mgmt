package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusProductOrderRequestDTO {

    @NotNull(message = "{field.description.required}")
    @NotBlank(message = "{field.description.not-blank}")
    @NotEmpty(message = "{field.description.required}")
    private StatusProductOrderEnum status;
}
