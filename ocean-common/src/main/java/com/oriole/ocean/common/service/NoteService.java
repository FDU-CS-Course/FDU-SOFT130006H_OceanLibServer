package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;
import java.util.List;

/**
 * Note service interface
 * Provides business logic for note operations
 */
public interface NoteService {

    /**
     * Check if user is the creator of a note
     * @param noteId Note ID
     * @param username Username to check
     * @return true if user is creator, false otherwise
     */
    boolean isNoteCreator(String noteId, String username);

    /**
     * Delete a note (soft delete)
     * @param noteID Note ID to delete
     */
    void deleteNote(String noteID);

    /**
     * Favorite/unfavorite a note
     * @param username Username
     * @param isFavor Whether to favor or unfavor
     * @param noteID Note ID
     * @param id Existing favor record ID (for unfavor operation)
     * @return Favor entity or null
     */
    FavorEntity favoriteNote(String username, boolean isFavor, String noteID, String id);

    /**
     * Get note by ID
     * @param noteID Note ID
     * @return Note entity
     */
    NoteEntity getNoteById(String noteID);

    /**
     * Get note by ID with like status for current user
     * @param noteID Note ID
     * @param username Current user's username
     * @return Note entity with like status
     */
    NoteEntity getNoteByIdWithLikeStatus(String noteID, String username);

    /**
     * Get user behavior (favorites) for a specific note
     * @param username Username
     * @param noteId Note ID
     * @return Favor entity or null
     */
    FavorEntity getBehaviourByUsernameAndNoteId(String username, String noteId);

    /**
     * Get user behaviors (favorites) list
     * @param username Username
     * @param pageNo Page number
     * @param pageSize Page size
     * @return List of favor entities
     */
    List<FavorEntity> getBehaviourByUsername(String username, int pageNo, int pageSize);

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
    List<NoteEntity> getLatestNotesWithLikeStatus(String username);

    /**
     * Get notes by creator name
     * @param username Current user's name
     * @return list of notes created by that user
     */
    List<NoteEntity> getNotesByUsernameWithLikeStatus(String username);

    /**
     * Get notes by keywords
     * @param searchString Search keywords
     * @return List of matching notes
     */
    List<NoteEntity> getNotesByKeywords(String searchString);

    /**
     * Get notes by keywords with like status for current user
     * @param searchString Search keywords
     * @param username Current user's username
     * @return List of matching notes with like status
     */
    List<NoteEntity> getNotesByKeywordsWithLikeStatus(String searchString, String username);

    /**
     * Get notes by tag
     * @param tag Note tag
     * @return List of notes with specified tag
     */
    List<NoteEntity> getNotesByTag(String tag);

    /**
     * Get notes by tag with like status for current user
     * @param tag Note tag
     * @param username Current user's username
     * @return List of notes with specified tag and like status
     */
    List<NoteEntity> getNotesByTagWithLikeStatus(String tag, String username);

    /**
     * Create a new note
     * @param noteEntity Note entity to create
     * @return Created note entity
     */
    NoteEntity createNote(NoteEntity noteEntity);

    // === Like functionality ===
    
    /**
     * Like or unlike a note
     * @param username Username who likes/unlikes
     * @param noteId Note ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return NoteLikeEntity if liked, null if unliked
     */
    NoteLikeEntity likeNote(String username, String noteId, boolean isLike);
    
    /**
     * Check if a user has liked a specific note
     * @param username Username to check
     * @param noteId Note ID to check
     * @return true if liked, false otherwise
     */
    boolean hasUserLikedNote(String username, String noteId);
    
    /**
     * Get like record for a user and note
     * @param username Username
     * @param noteId Note ID
     * @return NoteLikeEntity if exists, null otherwise
     */
    NoteLikeEntity getNoteLike(String username, String noteId);
}
