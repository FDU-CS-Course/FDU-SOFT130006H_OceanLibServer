package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentLikeEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;

import java.util.List;

/**
 * Note service interface
 * Provides business logic for note operations
 */
public interface NoteCommentService {

    /**
     * Check if user is the creator of a comment
     * @param commentId Comment ID
     * @param username Username to check
     * @return true if user is creator, false otherwise
     */
    boolean isCommentCreator(String commentId, String username);

    /**
     * Delete a note comment
     * @param commentId Comment ID
     */
    void deleteNoteComment(String commentId);

    /**
     * Get note comments by note ID
     * @param noteId Note ID
     * @return List of note comments
     */
    List<NoteCommentEntity> getNoteCommentsByNoteIdWithLikeStatus(String noteId);

    /**
     * Get note comments by comment ID
     * @param commentId comment ID
     * @return note comment
     */
    NoteCommentEntity getNoteComment(String commentId);

    /**
     * Create a new note comment
     * @param noteCommentEntity Note comment entity to create
     * @return Created note comment entity
     */
    NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity);


    /**
     * Like or unlike a note
     * @param username Username who likes/unlikes
     * @param commentId Note ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return NoteLikeEntity if liked, null if unliked
     */
    NoteCommentLikeEntity likeNoteComment(String username, String commentId, boolean isLike);

    /**
     * Check if a user has liked a specific note
     * @param username Username to check
     * @param commentId Note ID to check
     * @return true if liked, false otherwise
     */
    boolean hasUserLikedNoteComment(String username, String commentId);

    /**
     * Get like record for a user and note
     * @param username Username
     * @param commentId Note ID
     * @return NoteLikeEntity if exists, null otherwise
     */
    NoteCommentLikeEntity getNoteCommentLike(String username, String commentId);
}
