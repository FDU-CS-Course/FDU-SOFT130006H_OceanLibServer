package com.oriole.ocean.common.po.mongo.comment;

import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;

@Data
public class NoteCommentEntity extends AbstractComment implements java.io.Serializable {
    private String noteId;
    private String noteCommentBuildUsername;
    private Integer likeNum;
    private Integer replyCount;
    private Date createTime;
    private ArrayList<CommentReplyEntity> replyCommentList = null;

    public NoteCommentEntity(String cid, String noteId, String commentBuildUsername, String commentContent) {
        super(cid,commentContent);
        this.noteId = noteId;
        this.noteCommentBuildUsername = commentBuildUsername;
        this.replyCommentList = new ArrayList<>();
        this.replyCount = 0;
    }
}
