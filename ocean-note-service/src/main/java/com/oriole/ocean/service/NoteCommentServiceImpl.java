package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.enumerate.NotifyAction;
import com.oriole.ocean.common.enumerate.NotifySubscriptionTargetType;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentLikeEntity;
import com.oriole.ocean.common.service.NoteCommentService;
import com.oriole.ocean.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Note service implementation
 * Implements business logic for note operations including like functionality
 */
@Service
@Transactional
public class NoteCommentServiceImpl extends ServiceImpl<NoteCommentDao, NoteCommentEntity> implements NoteCommentService {

    @Resource
    private NoteCommentDao noteCommentDao;

    @Resource
    private NoteCommentLikeDao noteCommentLikeDao;

    /**
     * Check if user is the creator of a comment
     * @param commentId Comment ID
     * @param username Username to check
     * @return true if user is creator, false otherwise
     */
    public boolean isCommentCreator(String commentId, String username) {
        NoteCommentEntity comment = noteCommentDao.getNoteComment(commentId);
        return comment != null && comment.getBuildUsername().equals(username);
    }

    /**
     * Create a new note comment
     * @param noteCommentEntity Note comment entity to create
     * @return Created note comment entity
     */
    public NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity) {
        noteCommentEntity.setLikeNum(0);
        noteCommentEntity.setBuildDate(new Date());

        noteCommentDao.addNoteComment(noteCommentEntity);


        return noteCommentEntity;
    }

    /**
     * Get note comments by note ID (for anonymous users)
     * @param noteId Note ID
     * @return List of note comments
     */
    public List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId) {
        return noteCommentDao.getNoteCommentsByNoteId(noteId);
    }

    /**
     * Get note comments by note ID with like status (for authenticated users)
     * @param noteId Note ID
     * @param username Current user's username
     * @return List of note comments with like status
     */
    public List<NoteCommentEntity> getNoteCommentsByNoteIdWithLikeStatus(String noteId, String username) {
        return noteCommentDao.getNoteCommentsByNoteIdWithLikeStatus(noteId, username);
    }

    /**
     * Get note comments by comment ID
     * @param commentId comment ID
     * @return note comment
     */
    public NoteCommentEntity getNoteComment(String commentId) {
        return noteCommentDao.getNoteComment(commentId);
    }

    /**
     * Delete a note comment
     * @param commentId Comment ID
     */
    public void deleteNoteComment(String commentId) {
        noteCommentDao.deleteNoteComment(commentId);
    }


    // === Like functionality implementation ===

    /**
     * Like or unlike a note
     * @param username Username who likes/unlikes
     * @param commentId Note ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return NoteLikeEntity if liked, null if unliked
     */
    @Override
    public NoteCommentLikeEntity likeNoteComment(String username, String commentId, boolean isLike) {
        if (isLike) {
            // Check if already liked
            NoteCommentLikeEntity existingLike = noteCommentLikeDao.findNoteCommentLike(username, commentId);
            if (existingLike != null) {
                return existingLike; // Already liked, return existing record
            }

            // Create new like record
            NoteCommentLikeEntity noteCommentLikeEntity = new NoteCommentLikeEntity(username, commentId);
            noteCommentLikeDao.addNoteCommentLike(noteCommentLikeEntity);
            return null;
        } else {
            // Remove like record
            noteCommentLikeDao.removeNoteCommentLike(username, commentId);
            return null;
        }
    }

    /**
     * Check if a user has liked a specific note
     * @param username Username to check
     * @param commentId Note ID to check
     * @return true if liked, false otherwise
     */
    @Override
    public boolean hasUserLikedNoteComment(String username, String commentId) {
        return noteCommentLikeDao.hasUserLikedNoteComment(username, commentId);
    }

    /**
     * Get like record for a user and note
     * @param username Username
     * @param commentId Note ID
     * @return NoteLikeEntity if exists, null otherwise
     */
    @Override
    public NoteCommentLikeEntity getNoteCommentLike(String username, String commentId) {
        return noteCommentLikeDao.findNoteCommentLike(username, commentId);
    }
} 