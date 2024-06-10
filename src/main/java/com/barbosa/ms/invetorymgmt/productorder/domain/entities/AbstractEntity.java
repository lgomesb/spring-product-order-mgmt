package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by", columnDefinition = "varchar(100) not null default '99999'")
    private String createdBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "modified_by", columnDefinition = "varchar(100)")
    private String modifiedBy;

    @PreUpdate
    @PrePersist
    public void prePersist() {
        if(this.createdOn == null) {
            this.setCreatedOn(LocalDateTime.now());
            this.setCreatedBy("99999");
        } else {
            this.setModifiedOn(LocalDateTime.now());
            this.setModifiedBy("88888");
        }
    }

}
