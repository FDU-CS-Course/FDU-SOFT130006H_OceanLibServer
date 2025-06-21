package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Note data access object
 * Handles database operations for note records
 */
@Mapper
public interface NoteCommentDao extends BaseMapper<NoteCommentEntity> {

    void addNoteComment(NoteCommentEntity noteComment);
    boolean deleteNoteComment(String commentId);
    List<NoteCommentEntity> getNoteCommentsByNoteIdWithLikeStatus(String noteId);
    NoteCommentEntity getNoteComment(String commentId);
}
