package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Entity
@RestResource(exported = false)
@Table(name="\"lecturer\"")
public class LecturerEntity {
    @Id
    private String id;
    private String qualification;
    private String position;
    @Column(name = "\"full_name\"")
    private String fullName;

    @Column(name = "\"email\"")
    private String email;

    @JsonIgnore
    @Column(name = "\"password\"")
    private String password;

    @Column(name = "\"gender\"")
    private String gender;

    @Column(name = "\"phone\"")
    private String phone;

    @Column(name = "\"status\"")
    private boolean status;

    private boolean active;
    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    public  LecturerEntity (){}

    public LecturerEntity(String id, String qualification, String position) {
        this.id = id;
        this.qualification = qualification;
        this.position = position;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
