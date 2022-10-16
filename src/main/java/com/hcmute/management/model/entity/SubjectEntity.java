package com.hcmute.management.model.entity;

import org.aspectj.apache.bcel.classfile.Module;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@RestResource(exported = false)
@Table(name = "\"subject\"")
public class SubjectEntity {
    @Id
    @Column(name = "\"project_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(name="\"name\"")
    private String name;
    @Column(name="\"target\"")
    private String target;
    @Column(name = "\"Requirement\"")
    private String requirement;
    @Column(name = "\"Product\"")
    private String product;
    @Column(name = "\"description\"")
    private String description;
    @Column(name = "\"group_cap\"")
    private int groupCap;
    @Column(name = "\"reg_from_other_major\"")
    private Boolean regFromOtherMajor;
    @Column(name = "\"major\"")
    private String major;
    @Column(name = "\"subject_type\"")
    private String subjectType;
    @Column(name = "\"year\"")
    private String year;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "\"group_leader\"")
    private UserEntity groupLeader;
    @OneToMany(mappedBy = "subject",targetEntity = UserEntity.class,cascade = CascadeType.ALL)
    private List<UserEntity> groupMember;
    @Column(name = "\"status\"")
    private int status;
    @Column(name = "\"start_date\"")
    private LocalDateTime startDate;
    @Column(name = "\"end_Date\"")
    private LocalDateTime endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupCap() {
        return groupCap;
    }

    public void setGroupCap(int groupCap) {
        this.groupCap = groupCap;
    }

    public Boolean getRegFromOtherMajor() {
        return regFromOtherMajor;
    }

    public void setRegFromOtherMajor(Boolean regFromOtherMajor) {
        this.regFromOtherMajor = regFromOtherMajor;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public UserEntity getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(UserEntity groupLeader) {
        this.groupLeader = groupLeader;
    }

    public List<UserEntity> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(List<UserEntity> groupMember) {
        this.groupMember = groupMember;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SubjectEntity() {
    }

}
