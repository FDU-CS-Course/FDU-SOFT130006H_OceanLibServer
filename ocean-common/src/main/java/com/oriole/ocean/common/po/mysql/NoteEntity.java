package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.DateTimeException;
import java.util.Date;

@Data
@TableName("`note`")
public class NoteEntity implements java.io.Serializable {
    @TableId("note_id")
    private String id;

    @TableField("build_username")
    private String buildUsername;

    @TableField("content")
    private String content;

    @TableField("tag")
    private String tag;

    @TableField("build_date")
    private Date buildDate;

    @TableField("refresh_date")
    private Date refreshDate;

    @TableField("is_deleted")
    private Byte isDeleted;

    @TableField("is_anon")
    private Byte isAnon;

    @TableField("is_allow_comment")
    private Byte isAllowComment;

    @TableField("like_num")
    private Integer likeNum;

    @TableField("comment_num")
    private Integer commentNum;

    @TableField("read_num")
    private Integer readNum;
}
