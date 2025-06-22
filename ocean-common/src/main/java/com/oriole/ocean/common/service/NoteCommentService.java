package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentLikeEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;

import java.util.List;

/**
 * Note comment service interface
 * Provides business logic for note comment operations
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
     * Get note comments by note ID (for anonymous users)
     * @param noteId Note ID
     * @return List of note comments
     */
    List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId);

    /**
     * Get note comments by note ID with like status (for authenticated users)
     * @param noteId Note ID
     * @param username Current user's username
     * @return List of note comments with like status
     */
    List<NoteCommentEntity> getNoteCommentsByNoteIdWithLikeStatus(String noteId, String username);

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
     * Like or unlike a comment
     * @param username Username who likes/unlikes
     * @param commentId Comment ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return NoteCommentLikeEntity if liked, null if unliked
     */
    NoteCommentLikeEntity likeNoteComment(String username, String commentId, boolean isLike);

    /**
     * Check if a user has liked a specific comment
     * @param username Username to check
     * @param commentId Comment ID to check
     * @return true if liked, false otherwise
     */
    boolean hasUserLikedNoteComment(String username, String commentId);

    /**
     * Get like record for a user and comment
     * @param username Username
     * @param commentId Comment ID
     * @return NoteCommentLikeEntity if exists, null otherwise
     */
    NoteCommentLikeEntity getNoteCommentLike(String username, String commentId);
}
