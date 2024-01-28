package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(callSuper = true)
public abstract class ResponseDTO extends DataDTO {

    private UUID id;
}
