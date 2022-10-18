package com.hcmute.management.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name ="\"students\"")
public class StudentEntity {
    @Id
    @Column(name = "\"id\"")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @Column(name = "\"school_year\"")
    private Date school_year;

    @Column(name = "\"education_program\"")
    private String education_program;

    @ManyToOne()
    @JoinColumn(name ="classes_id")
    private ClassEntity classes;

    @Column(name ="\"major\"")
    private String major;

    public String getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getSchool_year() {
        return school_year;
    }

    public void setSchool_year(Date school_year) {
        this.school_year = school_year;
    }

    public String getEducation_program() {
        return education_program;
    }

    public void setEducation_program(String education_program) {
        this.education_program = education_program;
    }

    public ClassEntity getClasses() {
        return classes;
    }

    public void setClasses(ClassEntity classes) {
        this.classes = classes;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public StudentEntity( UserEntity user, Date school_year, String education_program, ClassEntity classes, String major) {
        this.user = user;
        this.school_year = school_year;
        this.education_program = education_program;
        this.classes = classes;
        this.major = major;
    }

    public StudentEntity() {
    }
}
