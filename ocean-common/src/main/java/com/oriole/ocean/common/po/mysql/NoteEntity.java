package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.DateTimeException;
import java.util.Date;

@Data
@TableName("`note`")
public class NoteEntity implements java.io.Serializable {
    @TableId("note_id")
    private Integer noteID;
    private String tag;
    private String content;
    @TableId("like_num")
    private Integer likeNum;
    @TableId("comment_num")
    private Integer commentNum;
    @TableId("read_num")
    private Integer readNum;
    @TableId("refresh_date")
    private Date refreshDate;
    @TableId("build_date")
    private Date buildDate;
    @TableId("build_username")
    private String buildUsername;
    @TableId("is_anon")
    private Byte isAnon;
    @TableId("is_approved")
    private Byte isApproved;
    @TableId("is_allow_comment")
    private Byte isAllowComment;
}
