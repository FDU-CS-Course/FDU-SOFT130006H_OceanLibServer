package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mysql.NoteCommentLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Note like data access object
 * Handles database operations for comment like records
 */
@Mapper
public interface NoteCommentLikeDao extends BaseMapper<NoteCommentLikeEntity> {
    
    /**
     * Add a like record
     * @param noteCommentLikeEntity The like record to add
     * @return Number of affected rows
     */
    int addNoteCommentLike(NoteCommentLikeEntity noteCommentLikeEntity);
    
    /**
     * Remove a like record
     * @param username The username who likes the comment
     * @param commentId The ID of the comment
     * @return Number of affected rows
     */
    int removeNoteCommentLike(@Param("username") String username, @Param("commentId") String commentId);
    
    /**
     * Check if a user has liked a specific comment
     * @param username The username to check
     * @param commentId The ID of the comment
     * @return The like record if exists, null otherwise
     */
    NoteCommentLikeEntity findNoteCommentLike(@Param("username") String username, @Param("commentId") String commentId);
    
    /**
     * Check if a user has liked a specific comment (returns boolean)
     * @param username The username to check
     * @param commentId The ID of the comment
     * @return true if liked, false otherwise
     */
    boolean hasUserLikedNoteComment(@Param("username") String username, @Param("commentId") String commentId);
} 