package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "\"classes\"")
@RestResource(exported = false)
public class ClassEntity {
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

    @Column(name = "\"class_name\"")
    private String className;
    @OneToMany( fetch = FetchType.EAGER, mappedBy = "classes",targetEntity = StudentEntity.class)
    @JsonIgnore
    private Set<StudentEntity> listStudent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<StudentEntity> getListStudent() {
        return listStudent;
    }

    public void setListStudent(Set<StudentEntity> listStudent) {
        this.listStudent = listStudent;
    }

    public String getClassName() {
        return className;
    }

    public void setClassname(String className) {
        this.className = className;
    }

    public ClassEntity(String className) {
        this.className = className;
    }

    public ClassEntity() {
    }
}
