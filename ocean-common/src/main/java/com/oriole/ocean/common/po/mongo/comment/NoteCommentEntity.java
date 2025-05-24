package com.oriole.ocean.common.po.mongo.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;

@Data
public class NoteCommentEntity implements java.io.Serializable {
    private String _id;
    private String noteId;
    private String noteCommentBuildUsername;
    private Integer likeNum;
    private Date createTime;
    private String replyTo;
    private String replyToUsername;
    private String commentContent;

    public NoteCommentEntity(String noteId, String noteCommentBuildUsername, String commentContent, String replyTo, String replyToUsername) {
        this.noteId = noteId;
        this.commentContent = commentContent;
        this.noteCommentBuildUsername = noteCommentBuildUsername;
        this.replyTo = replyTo;
        this.replyToUsername = replyToUsername;
    }
}
