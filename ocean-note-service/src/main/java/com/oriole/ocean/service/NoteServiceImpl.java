package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;
import com.oriole.ocean.common.service.NoteService;
import com.oriole.ocean.common.service.NotifyService;
import com.oriole.ocean.dao.NoteCollectionDao;
import com.oriole.ocean.dao.NoteDao;
import com.oriole.ocean.dao.NoteLikeDao;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Note service implementation
 * Implements business logic for note operations including like functionality
 */
@Service
@Transactional
public class NoteServiceImpl extends ServiceImpl<NoteDao, NoteEntity> implements NoteService {
    @Resource
    private NoteDao noteDao;

    @Resource
    private NoteCollectionDao noteCollectionDao;
    
    @Resource
    private NoteLikeDao noteLikeDao;

    /**
     * Check if user is the creator of a note
     * @param noteId Note ID
     * @param username Username to check
     * @return true if user is creator, false otherwise
     */
    public boolean isNoteCreator(String noteId, String username) {
        NoteEntity note = noteDao.getNoteById(noteId);
        return note != null && note.getBuildUsername().equals(username);
    }

    /**
     * Delete a note (soft delete)
     * @param noteID Note ID to delete
     */
    public void deleteNote(String noteID){
        noteDao.deleteNote(noteID);
    }

    /**
     * Get note by ID
     * @param noteID Note ID
     * @return Note entity
     */
    public NoteEntity getNoteById(String noteID) {
        return noteDao.getNoteById(noteID);
    }
    
    /**
     * Get note by ID with like status for current user
     * @param noteID Note ID
     * @param username Current user's username
     * @return Note entity with like status
     */
    public NoteEntity getNoteByIdWithLikeStatus(String noteID, String username) {
        return noteDao.getNoteByIdWithLikeStatus(noteID, username);
    }

    /**
     * Favorite/unfavorite a note
     * @param username Username
     * @param isFavor Whether to favor or unfavor
     * @param noteID Note ID
     * @param id Existing favor record ID (for unfavor operation)
     * @return Favor entity or null
     */
    public FavorEntity favoriteNote(String username, boolean isFavor, String noteID, String id){
        if (isFavor) {
            FavorEntity favorEntity = new FavorEntity();
            favorEntity.setUsername(username);
            favorEntity.setNoteId(noteID);
            noteCollectionDao.save(favorEntity);
            return noteCollectionDao.findByUsernameAndNoteId(username, noteID);
        }
        else {
            FavorEntity favorEntity = new FavorEntity();
            favorEntity.setUsername(username);
            favorEntity.setNoteId(noteID);
            favorEntity.setId(id);
            noteCollectionDao.delete(favorEntity);
            return null;
        }
    }

    /**
     * Get latest notes
     * @return List of latest notes
     */
    public List<NoteEntity> getLatestNotes() {
        return noteDao.getLatestNotes();
    }
    
    /**
     * Get latest notes with like status for current user
     * @param username Current user's username
     * @return List of latest notes with like status
     */
    public List<NoteEntity> getLatestNotesWithLikeStatus(String username) {
        return noteDao.getLatestNotesWithLikeStatus(username);
    }

    /**
     * Get notes by creator name
     * @param username Current user's name
     * @return list of notes created by that user
     */
    public List<NoteEntity> getNotesByUsernameWithLikeStatus(String username) {
        return noteDao.getNotesByMyNameWithLikeStatus(username);
    }

    /**
     * Get notes by keywords
     * @param searchString Search keywords
     * @return List of matching notes
     */
    public List<NoteEntity> getNotesByKeywords(String searchString) {
        return noteDao.getNotesByKeywords(searchString);
    }
    
    /**
     * Get notes by keywords with like status for current user
     * @param searchString Search keywords
     * @param username Current user's username
     * @return List of matching notes with like status
     */
    public List<NoteEntity> getNotesByKeywordsWithLikeStatus(String searchString, String username) {
        return noteDao.getNotesByKeywordsWithLikeStatus(searchString, username);
    }

    /**
     * Get notes by tag
     * @param tag Note tag
     * @return List of notes with specified tag
     */
    public List<NoteEntity> getNotesByTag(String tag) {
        return noteDao.getNotesByTag(tag);
    }
    
    /**
     * Get notes by tag with like status for current user
     * @param tag Note tag
     * @param username Current user's username
     * @return List of notes with specified tag and like status
     */
    public List<NoteEntity> getNotesByTagWithLikeStatus(String tag, String username) {
        return noteDao.getNotesByTagWithLikeStatus(tag, username);
    }

    /**
     * Create a new note
     * @param noteEntity Note entity to create
     * @return Created note entity
     */
    public NoteEntity createNote(NoteEntity noteEntity){
        noteEntity.setBuildDate(new Date());
        noteEntity.setRefreshDate(new Date());
        noteEntity.setCommentNum(0);
        noteEntity.setLikeNum(0);
        noteEntity.setReadNum(0);
        noteEntity.setIsDeleted((byte) 0);

        save(noteEntity);

        return noteEntity;
    }

    /**
     * Get user behavior (favorites) for a specific note
     * @param username Username
     * @param noteId Note ID
     * @return Favor entity or null
     */
    public FavorEntity getBehaviourByUsernameAndNoteId(String username, String noteId) {
        return noteCollectionDao.findByUsernameAndNoteId(username, noteId);
    }

    /**
     * Get user behaviors (favorites) list
     * @param username Username
     * @param pageNo Page number
     * @param pageSize Page size
     * @return List of favor entities
     */
    public List<FavorEntity> getBehaviourByUsername(String username, int pageNo, int pageSize) {
        return noteCollectionDao.getBehaviourByUsername(username, pageNo, pageSize);
    }

    // === Like functionality implementation ===
    
    /**
     * Like or unlike a note
     * @param username Username who likes/unlikes
     * @param noteId Note ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return NoteLikeEntity if liked, null if unliked
     */
    @Override
    public NoteLikeEntity likeNote(String username, String noteId, boolean isLike) {
        if (isLike) {
            // Check if already liked
            NoteLikeEntity existingLike = noteLikeDao.findNoteLike(username, noteId);
            if (existingLike != null) {
                return existingLike; // Already liked, return existing record
            }
            
            // Create new like record
            NoteLikeEntity noteLikeEntity = new NoteLikeEntity(username, noteId);
            noteLikeDao.addNoteLike(noteLikeEntity);
            return noteLikeEntity;
        } else {
            // Remove like record
            noteLikeDao.removeNoteLike(username, noteId);
            return null;
        }
    }
    
    /**
     * Check if a user has liked a specific note
     * @param username Username to check
     * @param noteId Note ID to check
     * @return true if liked, false otherwise
     */
    @Override
    public boolean hasUserLikedNote(String username, String noteId) {
        return noteLikeDao.hasUserLikedNote(username, noteId);
    }
    
    /**
     * Get like record for a user and note
     * @param username Username
     * @param noteId Note ID
     * @return NoteLikeEntity if exists, null otherwise
     */
    @Override
    public NoteLikeEntity getNoteLike(String username, String noteId) {
        return noteLikeDao.findNoteLike(username, noteId);
    }
} 