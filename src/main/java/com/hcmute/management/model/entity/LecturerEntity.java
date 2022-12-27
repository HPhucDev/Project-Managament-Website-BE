package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Set;

@Entity
@RestResource(exported = false)
@Table(name="\"lecturer\"")
public class LecturerEntity {
    @Id
    @Column(name = "\"id\"")
    private String id;
    @Column(name ="\"qualification\"")
    private String qualification;
    @Column(name = "\"position\"")
    private String position;

    @OneToOne()
    @JoinColumn(name="\"user\"",referencedColumnName = "user_id")
    @JsonIgnore
    private UserEntity user;
    
    @OneToMany(mappedBy = "lecturer",targetEntity = SubjectEntity.class,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SubjectEntity> subjects;

    public Set<SubjectEntity> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectEntity> subjects) {
        this.subjects = subjects;
    }

    public  LecturerEntity (){}

    public LecturerEntity(String id, String qualification, String position, UserEntity user) {
        this.id = id;
        this.qualification = qualification;
        this.position = position;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
