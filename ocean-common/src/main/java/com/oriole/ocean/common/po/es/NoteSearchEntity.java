package com.oriole.ocean.common.po.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "note_search")
public class NoteSearchEntity {
    @Id
    private String id;

    @Field(name = "analyzer_content", type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String content;

    @Field(name = "build_username", type = FieldType.Text)
    private String buildUsername;

    @Field(name = "analyzer_tag", type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String tag;

    @Field(name = "build_date", type = FieldType.Date)
    private Date buildDate;

    @Field(name = "refresh_date", type = FieldType.Date)
    private Date refreshDate;

    @Field(name = "is_deleted", type = FieldType.Byte)
    private Byte isDeleted;

    @Field(name = "is_anon", type = FieldType.Byte)
    private Byte isAnon;

    @Field(name = "is_allow_comment", type = FieldType.Byte)
    private Byte isAllowComment;

    @Field(name = "like_num", type = FieldType.Integer)
    private Integer likeNum;

    @Field(name = "comment_num", type = FieldType.Integer)
    private Integer commentNum;

    @Field(name = "read_num", type = FieldType.Integer)
    private Integer readNum;
}
