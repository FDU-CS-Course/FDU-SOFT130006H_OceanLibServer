package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Note like data access object
 * Handles database operations for note like records
 */
@Mapper
public interface NoteLikeDao extends BaseMapper<NoteLikeEntity> {
    
    /**
     * Add a like record
     * @param noteLikeEntity The like record to add
     * @return Number of affected rows
     */
    int addNoteLike(NoteLikeEntity noteLikeEntity);
    
    /**
     * Remove a like record
     * @param username The username who likes the note
     * @param noteId The ID of the note
     * @return Number of affected rows
     */
    int removeNoteLike(@Param("username") String username, @Param("noteId") String noteId);
    
    /**
     * Check if a user has liked a specific note
     * @param username The username to check
     * @param noteId The ID of the note
     * @return The like record if exists, null otherwise
     */
    NoteLikeEntity findNoteLike(@Param("username") String username, @Param("noteId") String noteId);
    
    /**
     * Check if a user has liked a specific note (returns boolean)
     * @param username The username to check
     * @param noteId The ID of the note
     * @return true if liked, false otherwise
     */
    boolean hasUserLikedNote(@Param("username") String username, @Param("noteId") String noteId);
} 