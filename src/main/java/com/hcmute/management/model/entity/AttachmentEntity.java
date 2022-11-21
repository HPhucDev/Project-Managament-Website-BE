package com.hcmute.management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "\"attachments\"")
@RestResource(exported = false)
public class AttachmentEntity {
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
    @Column(name = "\"attachment_link\"")
    private String link;
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "\"progress_id\"")
    private ProgressEntity progress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ProgressEntity getProgress() {
        return progress;
    }

    public void setProgress(ProgressEntity progress) {
        this.progress = progress;
    }

    public AttachmentEntity() {
        this.id = String.valueOf(UUID.randomUUID());
    }
}
