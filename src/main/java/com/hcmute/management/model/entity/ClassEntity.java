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
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private int id;

    @Column(name = "\"class_name\"")
    private String classname;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "classes")
    @JsonIgnore
    private Set<StudentEntity> listStudent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<StudentEntity> getListStudent() {
        return listStudent;
    }

    public void setListStudent(Set<StudentEntity> listStudent) {
        this.listStudent = listStudent;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public ClassEntity(String classname) {
        this.classname = classname;
    }

    public ClassEntity() {
    }
}
