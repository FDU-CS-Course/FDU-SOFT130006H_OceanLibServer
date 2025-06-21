package com.oriole.ocean.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oriole.ocean.common.auth.AuthUser;
import com.oriole.ocean.common.enumerate.MainType;
import com.oriole.ocean.common.enumerate.NotifyAction;
import com.oriole.ocean.common.enumerate.NotifySubscriptionTargetType;
import com.oriole.ocean.common.enumerate.NotifyType;
import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mysql.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NoteLikeEntity;
import com.oriole.ocean.common.po.mysql.NotifyEntity;
import com.oriole.ocean.common.service.*;
import com.oriole.ocean.common.vo.AuthUserEntity;
import com.oriole.ocean.common.vo.MsgEntity;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Note controller
 * Handles HTTP requests for note operations including like functionality
 */
@RestController
@RequestMapping("/noteService")
public class NoteController {

    @Autowired
    NoteService noteService;
    @Autowired
    NoteCommentService noteCommentService;
    @DubboReference
    UserBehaviorService userBehaviorService;
    @DubboReference
    NotifyService notifyService;
    @DubboReference
    NotifySubscriptionService notifySubscriptionService;

    /**
     * Get latest notes with pagination
     * Returns notes with like status for authenticated user
     */
    @RequestMapping(value = "/getLatestNote", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteEntity>> getNoteByPage(
            @AuthUser AuthUserEntity authUser,
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageNO, pageSize, true);
        
        List<NoteEntity> noteEntityList;
        if (authUser != null) {
            // Authenticated user - include like status
            noteEntityList = noteService.getLatestNotesWithLikeStatus(authUser.getUsername());
        } else {
            // Anonymous user - without like status
            noteEntityList = noteService.getLatestNotes();
        }
        
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS","1",pageInfo);
    }

    /**
     * Search notes by keywords with pagination
     * Returns notes with like status for authenticated user
     */
    @RequestMapping(value = "/getNotesByKeywords", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteEntity>> getNotesByKeywords(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String searchString,
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageNO, pageSize, true);
        
        List<NoteEntity> noteEntityList;
        if (authUser != null) {
            // Authenticated user - include like status
            noteEntityList = noteService.getNotesByKeywordsWithLikeStatus(searchString, authUser.getUsername());
        } else {
            // Anonymous user - without like status  
            noteEntityList = noteService.getNotesByKeywords(searchString);
        }
        
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS","1",pageInfo);
    }

    /**
     * Select notes by creator name with pagination
     * Returns notes with like status for authenticated user
     */
    @RequestMapping(value = "/getMyNotes", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteEntity>> getMyNotes(
            @AuthUser AuthUserEntity authUser,
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageNO, pageSize, true);
        List<NoteEntity> noteEntityList = noteService.getNotesByUsernameWithLikeStatus(authUser.getUsername());
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS","1",pageInfo);
    }

    /**
     * Get note by ID
     * Returns note with like status for authenticated user
     */
    @RequestMapping(value = "/getNoteById", method = RequestMethod.POST)
    public MsgEntity<NoteEntity> getNoteById(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteId
    ) {
        NoteEntity note;
        if (authUser != null) {
            // Authenticated user - include like status
            note = noteService.getNoteByIdWithLikeStatus(noteId, authUser.getUsername());
        } else {
            // Anonymous user - without like status
            note = noteService.getNoteById(noteId);
        }
        
        return new MsgEntity<>("SUCCESS", "1", note);
    }

    /**
     * Create a new note
     */
    @RequestMapping(value = "/createNote", method = RequestMethod.POST)
    public MsgEntity<NoteEntity> createNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String content,
            @RequestParam String tag,
            @RequestParam Byte isAnon,
            @RequestParam Byte isAllowComment) {
        String buildUsername = authUser.getUsername();

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setBuildUsername(buildUsername);
        noteEntity.setContent(content);
        noteEntity.setTag(tag);
        noteEntity.setIsAnon(isAnon);
        noteEntity.setIsAllowComment(isAllowComment);
        NoteEntity note = noteService.createNote(noteEntity);

        // 增加用户消息订阅事件：用户需要订阅自己发布的评论或回复的动态
        List<NotifyAction> notifyActionList = new ArrayList<>();
        notifyActionList.add(NotifyAction.LIKE_COMMENT);
        notifyActionList.add(NotifyAction.NEW_COMMENT);
        notifySubscriptionService.setNotifySubscription(buildUsername, notifyActionList,
                note.getId(), NotifySubscriptionTargetType.COMMENT);

        return new MsgEntity<>("SUCCESS", "1", note);
    }

    /**
     * Delete a note
     */
    @RequestMapping(value = "/deleteNote", method = RequestMethod.POST)
    public MsgEntity<String> deleteNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteID) {
        if(!noteService.isNoteCreator(noteID, authUser.getUsername())) {
            if(authUser.isAdmin()) {
                //TODO: Record administrator operation
            } else {
                return new MsgEntity<>("FAILED", "400", "Unauthorized Operation");
            }
        }
        noteService.deleteNote(noteID);
        return new MsgEntity<>("SUCCESS", "1", "Note deleted successfully");
    }

    /**
     * Get notes by tag with pagination
     * Returns notes with like status for authenticated user
     */
    @RequestMapping(value = "/getNoteByTag",method = RequestMethod.GET)
    public MsgEntity<PageInfo<NoteEntity>> getNoteByTag(
            @AuthUser AuthUserEntity authUser,
            @RequestParam(required = false) String tag,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize, true);
        
        List<NoteEntity> noteEntityList;
        if (authUser != null) {
            // Authenticated user - include like status
            noteEntityList = noteService.getNotesByTagWithLikeStatus(tag, authUser.getUsername());
        } else {
            // Anonymous user - without like status
            noteEntityList = noteService.getNotesByTag(tag);
        }
        
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }

    // === Like functionality APIs ===
    
    /**
     * Like or unlike a note
     * @param authUser Authenticated user
     * @param noteId Note ID to like/unlike
     * @param isLike true to like, false to unlike
     * @return Success message with like status
     */
    @RequestMapping(value = "/likeNote", method = RequestMethod.POST)
    public MsgEntity<NoteLikeEntity> likeNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteId,
            @RequestParam Boolean isLike) {
        
        String username = authUser.getUsername();
        
            NoteLikeEntity result = noteService.likeNote(username, noteId, isLike);
            
            if (isLike) {
                return new MsgEntity<>("SUCCESS", "1", result);
            } else {
                return new MsgEntity<>("SUCCESS", "1", null);
            }
    }
    
    /**
     * Check if current user has liked a specific note
     * @param authUser Authenticated user
     * @param noteId Note ID to check
     * @return Like status
     */
    @RequestMapping(value = "/checkNoteLikeStatus", method = RequestMethod.POST)
    public MsgEntity<Boolean> checkNoteLikeStatus(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteId) {
        
        String username = authUser.getUsername();
        boolean hasLiked = noteService.hasUserLikedNote(username, noteId);
        
        return new MsgEntity<>("SUCCESS", "1", hasLiked);
    }

    // === Original comment and favorite APIs ===

    /**
     * Get note comments by note ID
     */
    @RequestMapping(value = "/getNoteCommentByNoteId", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteCommentEntity>> getNoteCommentByNoteId(
            @RequestParam String noteId,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize, true);
        List<NoteCommentEntity> noteCommentEntityList = noteCommentService.getNoteCommentsByNoteIdWithLikeStatus(noteId);
        PageInfo<NoteCommentEntity> pageInfo = new PageInfo<>(noteCommentEntityList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }

    /**
     * Create a note comment
     */
    @RequestMapping(value = "/createNoteComment", method = RequestMethod.POST)
    public MsgEntity<NoteCommentEntity> createNoteComment(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteId,
            @RequestParam String content,
            @RequestParam String replyId,
            @RequestParam String replyUsername
    ) {
        String userName = authUser.getUsername();

        NoteCommentEntity noteCommentEntity = new NoteCommentEntity(noteId, userName, content, replyId, replyUsername);

        noteCommentEntity.setReplyId(replyId);
        NoteCommentEntity noteComment = noteCommentService.createNoteComment(noteCommentEntity);

        //构建用户消息事件
        NotifyEntity notifyEntity = new NotifyEntity(NotifyType.REMIND, userName);
        notifyEntity.setTargetIDAndType(noteId, MainType.NOTE);
        notifyEntity.setContent(content);
        System.out.println(replyId);
        System.out.println(noteId);
        if (Objects.equals(replyId, noteId)) { // 直接回复帖子
            notifyEntity.setAction(NotifyAction.NEW_COMMENT);// 新评论事件不面向任何其他评论
        } else {
            notifyEntity.setAction(NotifyAction.NEW_REPLY);
        }
        notifyEntity.setCommentID(replyId);
        // 增加用户消息订阅事件：用户需要订阅自己发布的评论或回复的动态
        List<NotifyAction> notifyActionList = new ArrayList<>();
        notifyActionList.add(NotifyAction.LIKE_COMMENT);
        notifyActionList.add(NotifyAction.NEW_REPLY);
        notifySubscriptionService.setNotifySubscription(userName, notifyActionList,
                replyId, NotifySubscriptionTargetType.COMMENT);

        // 产生用户消息事件
        notifyService.addNotify(notifyEntity);

        return new MsgEntity<>("SUCCESS", "1", noteComment);
    }

    /**
     * Delete a note comment
     */
    @RequestMapping(value = "/deleteNoteComment", method = RequestMethod.POST)
    public MsgEntity<String> deleteNoteComment(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String _id) {
        if(noteCommentService.isCommentCreator(_id, authUser.getUsername())) {
            noteCommentService.deleteNoteComment(_id);
            return new MsgEntity<>("SUCCESS", "1", "评论删除成功");
        }
        NoteCommentEntity noteComment = noteCommentService.getNoteComment(_id);
        if(noteService.isNoteCreator(noteComment.getNoteId(), authUser.getUsername())) {
            noteCommentService.deleteNoteComment(_id);
            return new MsgEntity<>("SUCCESS", "1", "帖主删除评论成功");
        }
        if(authUser.isAdmin()) {
            noteCommentService.deleteNoteComment(_id);
            return new MsgEntity<>("SUCCESS", "1", "管理员删除评论成功");
        }
        return new MsgEntity<>("FAILED", "400", "删除评论失败");
    }

    // 收藏功能
    @RequestMapping(value = "/favoriteNote", method = RequestMethod.POST)
    public MsgEntity<FavorEntity> favoriteNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam Boolean isFavor,
            @RequestParam String noteId,
            @RequestParam(required = false) String id) {
        return new MsgEntity<>("SUCCESS", "1", noteService.favoriteNote(authUser.getUsername(), isFavor, noteId, id));
    }

    @RequestMapping(value = "/getBehaviourByUsernameAndNoteId", method = RequestMethod.POST)
    public MsgEntity<FavorEntity> getBehaviourByUsernameAndNoteId(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String noteId) {
        FavorEntity favorEntity = noteService.getBehaviourByUsernameAndNoteId(authUser.getUsername(), noteId);
        if (favorEntity == null) return new MsgEntity<>("SUCCESS", "2", null);
        return new MsgEntity<>("SUCCESS", "1", favorEntity);
    }

    @RequestMapping(value = "/getBehaviourByUsername", method = RequestMethod.POST)
    public MsgEntity<PageInfo<FavorEntity>> getBehaviourByUsername(
            @AuthUser AuthUserEntity authUser,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        List<FavorEntity> collectionList = noteService.getBehaviourByUsername(authUser.getUsername(), pageNo, pageSize);
        PageInfo<FavorEntity> pageInfo = new PageInfo<>(collectionList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }
}
