package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@RestResource(exported = false)
@Entity
@Table(name = "\"progress\"")
public class ProgressEntity {
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

    @Column(name = "\"description\"")
    private String description;

    @Column(name = "\"status\"")
    private String status;

    @Column(name = "\"create_date\"")
    private Date createdate;

    @Column(name = "\"modifier_date\"")
    private Date modiferdate;

    @Column(name = "\"time_submit\"")
    private Date timesubmit;

    @Column(name = "\"week\"")
    private int week;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private SubjectEntity subject;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private StudentEntity student;

    @OneToMany(mappedBy = "progressComment", targetEntity = CommentEntity.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comment;

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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getModiferdate() {
        return modiferdate;
    }

    public void setModiferdate(Date modiferdate) {
        this.modiferdate = modiferdate;
    }

    public Date getTimesubmit() {
        return timesubmit;
    }

    public void setTimesubmit(Date timesubmit) {
        this.timesubmit = timesubmit;
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

    public ProgressEntity(String id, String description, String status, Date createdate, Date modiferdate, Date timesubmit, int week, SubjectEntity subject, StudentEntity student, Set<CommentEntity> comment) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdate = createdate;
        this.modiferdate = modiferdate;
        this.timesubmit = timesubmit;
        this.week = week;
        this.subject = subject;
        this.student = student;
        this.comment = comment;
    }

    public ProgressEntity() {
    }
}
