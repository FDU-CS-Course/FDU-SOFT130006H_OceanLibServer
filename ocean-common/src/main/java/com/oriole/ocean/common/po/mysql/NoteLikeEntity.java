package com.oriole.ocean.common.po.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("`note_like`")
public class NoteLikeEntity implements java.io.Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;          // 自增主键（ORM需要，业务无关）

    private Integer postId;      // 帖子ID
    private Integer userId;      // 用户ID
}
