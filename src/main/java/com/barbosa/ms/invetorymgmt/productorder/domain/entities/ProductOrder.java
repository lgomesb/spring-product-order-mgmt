package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ToString()
@NoArgsConstructor
@Getter
@Setter
@Table(name = "productorder")
@Entity
public class ProductOrder extends AbstractEntity {

    @NotNull(message = "{field.name.required}")
    @NotBlank(message = "{field.name.not-blank}")
    @NotEmpty(message = "{field.name.required}")
    @Column(columnDefinition = "varchar(1) not null default 'A'", nullable = false)
    private String status;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> items;

    @Builder
    public ProductOrder(UUID id, String status, Set<OrderItem> items) {
        this.status = status;
        this.items = items;
        super.setId(id);
    }

    @Override
    public void prePersist() {
        super.prePersist();

        if(Objects.isNull(this.getStatus()))
            this.setStatus("A");
    }
}
