package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Note comment data access object
 * Handles database operations for note comment records
 */
@Mapper
public interface NoteCommentDao extends BaseMapper<NoteCommentEntity> {

    void addNoteComment(NoteCommentEntity noteComment);
    boolean deleteNoteComment(String commentId);
    
    /**
     * Get note comments by note ID without like status (for anonymous users)
     * @param noteId Note ID
     * @return List of note comments
     */
    List<NoteCommentEntity> getNoteCommentsByNoteId(@Param("noteId") String noteId);
    
    /**
     * Get note comments by note ID with like status for authenticated user
     * @param noteId Note ID
     * @param username Current user's username
     * @return List of note comments with like status
     */
    List<NoteCommentEntity> getNoteCommentsByNoteIdWithLikeStatus(@Param("noteId") String noteId, @Param("username") String username);
    
    NoteCommentEntity getNoteComment(String commentId);
}
