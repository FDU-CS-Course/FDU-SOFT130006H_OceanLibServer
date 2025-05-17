package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.DateTimeException;
import java.util.Date;

@Data
@TableName("`note`")
public class NoteEntity implements java.io.Serializable {
    @TableId("id")
    private Integer id;
    private String buildUsername;
    // contents
    private String content;
    private String tag;
    // extra info
    private Date buildDate;
    private Date refreshDate;
    // states
    private Byte isDeleted;
    private Byte isAnon;
    private Byte isAllowComment;
    // statistics (changing with relation 'likes')
    private Integer likeNum;
    private Integer commentNum;
    private Integer readNum;
}
