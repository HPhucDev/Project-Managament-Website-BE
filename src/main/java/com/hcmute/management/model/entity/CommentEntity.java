package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"comment\"")
@RestResource(exported = false)
public class CommentEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "\"id\"")
    private String id;

    @ManyToOne()
    @JoinColumn(name = "\"user\"")
    private UserEntity userComment;

    @Column(name = "\"message\"")
    private String message;

    @ManyToOne
    @JoinColumn(name = "\"progress\"")
    private ProgressEntity progressComment;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(required = true, example = "2021-08-20T00:00:00")
    @Column(name = "\"time\"")
    private LocalDateTime time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUserComment() {
        return userComment;
    }

    public void setUserComment(UserEntity userComment) {
        this.userComment = userComment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProgressEntity getProgressComment() {
        return progressComment;
    }

    public void setProgressComment(ProgressEntity progressComment) {
        this.progressComment = progressComment;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public CommentEntity() {
    }

    public CommentEntity(String id, UserEntity userComment, String message, ProgressEntity progressComment, LocalDateTime time) {
        this.id = id;
        this.userComment = userComment;
        this.message = message;
        this.progressComment = progressComment;
        this.time = time;
    }
}
