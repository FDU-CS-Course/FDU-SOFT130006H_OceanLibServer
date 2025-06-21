package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Note like record entity
 * Records the like relationship between users and notes
 */
@Data
@TableName("`note_like`")
public class NoteCommentLikeEntity implements java.io.Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("username")
    private String username;

    @TableField("comment_id")
    private String commentId;

    @TableField("like_time")
    private Date likeTime;

    /**
     * Default constructor
     */
    public NoteCommentLikeEntity() {
        this.likeTime = new Date();
    }

    /**
     * Constructor with username and commentId
     * @param username The username who likes the comment
     * @param commentId The ID of the liked comment
     */
    public NoteCommentLikeEntity(String username, String commentId) {
        this.username = username;
        this.commentId = commentId;
        this.likeTime = new Date();
    }
}
