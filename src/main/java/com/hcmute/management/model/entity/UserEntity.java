package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@RestResource(exported = false)
@Table(name = "\"users\"")
public class UserEntity {
    @Id
    @Column(name = "\"user_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "\"full_name\"")
    private String fullName;

    @Column(name = "\"email\"")
    private String email;

    @JsonIgnore
    @Column(name = "\"password\"")
    private String password;
    @OneToOne(mappedBy = "user",targetEntity = StudentEntity.class)
    @JsonIgnore
    private StudentEntity student;

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    @Column(name = "\"gender\"")
    private String gender;

    @Column(name = "\"user_name\"")
    private String userName;

    @Column(name = "\"status\"")
    private boolean status;

    private boolean active;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "\"user_role\"", joinColumns = @JoinColumn(name = "\"user_id\""), inverseJoinColumns = @JoinColumn(name = "\"role_id\""))
    private Set<RoleEntity> roles;
    @JsonIgnore
    @OneToOne(mappedBy = "groupLeader",targetEntity = SubjectEntity.class)
    private SubjectEntity subjectLeader;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "\"subject\"")
    private SubjectEntity subject;

    @OneToOne(mappedBy = "user",targetEntity = LecturerEntity.class)
    @JsonIgnore
    private LecturerEntity lecturer;

    @OneToMany(mappedBy = "userComment",targetEntity = CommentEntity.class,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comment;
    @Column(name = "\"imageLink\"")
    private String imgLink;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(required = true, example = "2021-08-20T00:00:00")
    private LocalDateTime birthDay;

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public LecturerEntity getLecturer() {
        return lecturer;
    }

    public void setLecturer(LecturerEntity lecturer) {
        this.lecturer = lecturer;
    }

    public UserEntity(String password, String userName) {
        this.password = password;
        this.userName = userName;
    }

    public SubjectEntity getSubjectLeader() {
        return subjectLeader;
    }

    public void setSubjectLeader(SubjectEntity subjectLeader) {
        this.subjectLeader = subjectLeader;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public UserEntity() {
    }
}
