package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.UserNotifyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Note data access object
 * Handles database operations for note records
 */
@Mapper
public interface NoteDao extends BaseMapper<NoteEntity> {


    String getNoteCreator(String noteID);

    void readNote(String noteId);
    
    /**
     * Get latest notes
     * @return List of latest notes
     */
    List<NoteEntity> getLatestNotes();
    
    /**
     * Get latest notes with like status for current user
     * @param username Current user's username
     * @return List of latest notes with like status
     */
    List<NoteEntity> getLatestNotesWithLikeStatus(@Param("username") String username);
    
    /**
     * Get notes by keywords
     * @param searchString Search keywords
     * @return List of notes matching keywords
     */
    List<NoteEntity> getNotesByKeywords(@Param("searchString") String searchString);
    
    /**
     * Get notes by keywords with like status for current user
     * @param searchString Search keywords
     * @param username Current user's username
     * @return List of notes matching keywords with like status
     */
    List<NoteEntity> getNotesByKeywordsWithLikeStatus(@Param("searchString") String searchString, 
                                                       @Param("username") String username);
    
    /**
     * Get notes by tag
     * @param tag Note tag
     * @return List of notes with specified tag
     */
    List<NoteEntity> getNotesByTag(@Param("tag") String tag);
    
    /**
     * Get notes by tag with like status for current user
     * @param tag Note tag
     * @param username Current user's username
     * @return List of notes with specified tag and like status
     */
    List<NoteEntity> getNotesByTagWithLikeStatus(@Param("tag") String tag, 
                                                  @Param("username") String username);

    /**
     * Get notes by creator name
     * @param username Current user's name
     * @return list of notes created by that user
     */
    List<NoteEntity> getNotesByMyNameWithLikeStatus(@Param("username") String username);

    /**
     * Get note by ID
     * @param noteId Note ID
     * @return Note entity
     */
    NoteEntity getNoteById(@Param("noteId") String noteId);
    
    /**
     * Get note by ID with like status for current user
     * @param noteId Note ID
     * @param username Current user's username
     * @return Note entity with like status
     */
    NoteEntity getNoteByIdWithLikeStatus(@Param("noteId") String noteId, 
                                         @Param("username") String username);
    
    /**
     * Delete note (soft delete)
     * @param noteID Note ID to delete
     */
    void deleteNote(@Param("noteID") String noteID);

    /**
     * Get note comments by note ID
     * @param noteId Note ID
     * @return List of note comments
     */
    List<NoteCommentEntity> getNoteCommentsByNoteId(@Param("noteId") String noteId);

    void increaseCommentCnt(@Param("noteID") String noteID);
}
