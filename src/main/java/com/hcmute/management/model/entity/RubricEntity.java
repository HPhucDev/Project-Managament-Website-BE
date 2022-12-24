package com.hcmute.management.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@RestResource(exported = false)
@Table(name="\"rubric\"")
@Getter
@Setter
@NoArgsConstructor
public class RubricEntity {
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
    @Column(name = "\"suggest\"")
    private String suggest;
    @Column(name = "\"create_date\"")
    private LocalDateTime createDate;
}
