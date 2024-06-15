package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum StatusProductOrderEnum {
    DRAFT(0),
    REJECTED(1),
    APPROVED(2),
    COMPLETED(3);

    private final int value;

    public static StatusProductOrderEnum ofName(String name) {
        for(StatusProductOrderEnum value : values()) {
            if(value.name().equalsIgnoreCase(name))
                return value;
        }
        throw new IllegalArgumentException("Not exists enum item with name: " + name);
    }
}
