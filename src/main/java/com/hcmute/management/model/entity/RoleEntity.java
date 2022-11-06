package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.management.common.AppUserRole;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.UUID;

@RestResource(exported = false)
@Entity
@Table(name = "\"roles\"")
@NoArgsConstructor
public class RoleEntity {
    @Id
    @Column(name = "\"id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppUserRole name;

    public AppUserRole getName() {
        return name;
    }

    public void setName(AppUserRole name) {
        this.name = name;
    }
}
