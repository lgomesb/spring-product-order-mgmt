package com.barbosa.ms.productordermgmt.product-order-mgmt.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "{field.name.required}")
    @NotBlank(message = "{field.name.not-blank}")
    @NotEmpty(message = "{field.name.required}")
    @Column(columnDefinition = "varchar(255) not null")
    private String name;
    
    @Column(columnDefinition = "varchar(1) not null default 'A'")
    private String status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by", columnDefinition = "varchar(100) not null default '99999'")
    private String createdBy;

    @Column(name = "modified_on")
    private LocalDateTime modifieldOn;

    @Column(name = "modified_by", columnDefinition = "varchar(100)")
    private String modifiedBy;

    @PrePersist
    public void prePersist() {
        setCreatedOn(LocalDateTime.now());
        setCreatedBy("99999");
        setStatus("A");
    }
    
}
