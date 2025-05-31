package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.util.Date;

/**
 * Note like record entity
 * Records the like relationship between users and notes
 */
@Data
@TableName("`note_like`")
public class NoteLikeEntity implements java.io.Serializable {
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @TableField("username")
    private String username;
    
    @TableField("note_id")
    private String noteId;
    
    @TableField("like_time")
    private Date likeTime;
    
    /**
     * Default constructor
     */
    public NoteLikeEntity() {
        this.likeTime = new Date();
    }
    
    /**
     * Constructor with username and noteId
     * @param username The username who likes the note
     * @param noteId The ID of the liked note
     */
    public NoteLikeEntity(String username, String noteId) {
        this.username = username;
        this.noteId = noteId;
        this.likeTime = new Date();
    }
}
