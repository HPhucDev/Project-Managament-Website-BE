package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@RestResource(exported = false)
@Entity
@Table(name = "\"progress\"")
public class ProgressEntity {
    @Id
    @Column(name = "\"progress_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "\"description\"")
    private String description;

    @Column(name = "\"status\"")
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(required = true, example = "2021-08-20T00:00:00")
    @Column(name = "\"create_date\"")
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(required = true, example = "2021-08-20T00:00:00")
    @Column(name = "\"modifier_date\"")
    private LocalDateTime modiferDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(required = true, example = "2021-08-20T00:00:00")
    @Column(name = "\"time_submit\"")
    private LocalDateTime timeSubmit;

    @Column(name = "\"week\"")
    private int week;

    @ManyToOne()
    @JoinColumn(name = "\"subject_id\"")
    @JsonIgnore
    private SubjectEntity subject;

    @ManyToOne()
    @JoinColumn(name = "\"student_id\"")
    private StudentEntity student;

    @OneToMany(mappedBy = "progressComment", targetEntity = CommentEntity.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comment;

    @OneToMany(mappedBy = "progress",targetEntity = AttachmentEntity.class,cascade = CascadeType.ALL)
    private Set<AttachmentEntity> attachments;

    public Set<AttachmentEntity> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<AttachmentEntity> attachments) {
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getModiferDate() {
        return modiferDate;
    }

    public void setModiferDate(LocalDateTime modiferDate) {
        this.modiferDate = modiferDate;
    }

    public LocalDateTime getTimeSubmit() {
        return timeSubmit;
    }

    public void setTimeSubmit(LocalDateTime timeSubmit) {
        this.timeSubmit = timeSubmit;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }


    public Set<CommentEntity> getComment() {
        return comment;
    }

    public void setComment(Set<CommentEntity> comment) {
        this.comment = comment;
    }


    public ProgressEntity() {
    }
}
