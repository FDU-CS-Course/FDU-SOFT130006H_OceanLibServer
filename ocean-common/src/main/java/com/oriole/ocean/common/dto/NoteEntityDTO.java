package com.oriole.ocean.common.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.DateTimeException;
import java.util.Date;

@Data
@TableName("`note`")
public class NoteEntityDTO implements java.io.Serializable {
    private String tag;
    private String content;
    private String buildUsername;
    private Byte isAnon;
    private Byte isApproved;
    private Byte isAllowComment;
}
