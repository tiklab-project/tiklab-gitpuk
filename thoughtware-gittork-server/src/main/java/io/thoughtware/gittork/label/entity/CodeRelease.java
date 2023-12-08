package io.thoughtware.gittork.label.entity;

import io.thoughtware.dal.jpa.annotation.*;

@Entity
@Table(name="code_branch")
public class CodeRelease {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "release_id")
    private String releaseId;

    @Column(name = "label_id")
    private String labelId;

    @Column(name = "title")
    private String title;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message")
    private String message;


    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}












































