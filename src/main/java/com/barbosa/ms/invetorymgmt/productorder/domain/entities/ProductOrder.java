package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(of = {"description", "status", "items"}, callSuper = false)
@NoArgsConstructor
@Data
@Table(name = "productorder")
@Entity
public class ProductOrder extends AbstractEntity {

    @NotNull(message = "{field.description.required}")
    @NotBlank(message = "{field.description.not-blank}")
    @NotEmpty(message = "{field.description.required}")
    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "varchar(1) not null default 'A'", nullable = false)
    private String status;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> items;

    @Builder
    public ProductOrder(UUID id, String description, String status, Set<OrderItem> items) {
        this.status = status;
        this.items = items;
        this.description = description;
        super.setId(id);
    }

    @Override
    public void prePersist() {
        super.prePersist();

        if(Objects.isNull(this.getStatus()))
            this.setStatus("A");
    }
}

