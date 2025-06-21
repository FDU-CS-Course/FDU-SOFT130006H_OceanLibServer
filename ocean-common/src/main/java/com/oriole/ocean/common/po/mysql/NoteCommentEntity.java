package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class NoteCommentEntity implements java.io.Serializable {
    @TableField("id")
    private String id;

    @TableField("note_id")
    private String noteId;

    @TableField("content")
    private String content;

    @TableField("build_username")
    private String buildUsername;

    @TableField("like_num")
    private Integer likeNum;

    @TableField("build_date")
    private Date buildDate;

    @TableField("is_deleted")
    private Byte isDeleted;

    @TableField("reply_id")
    private String replyId;

    @TableField("reply_username")
    private String replyUsername;

    /**
     * Whether the current user has liked this comment.
     * This field is not mapped to the database, only used for data transfer
     */
    @TableField(exist = false)
    private Boolean isLikedByCurrentUser;

    public NoteCommentEntity(
            String noteId, String buildUsername,
            String content, String replyId, String replyUsername) {
        this.noteId = noteId;
        this.content = content;
        this.buildUsername = buildUsername;
        this.replyId = replyId;
        this.replyUsername = replyUsername;
    }
}
