package com.oriole.ocean.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oriole.ocean.common.auth.AuthUser;
import com.oriole.ocean.common.enumerate.MainType;
import com.oriole.ocean.common.enumerate.NotifyAction;
import com.oriole.ocean.common.enumerate.NotifySubscriptionTargetType;
import com.oriole.ocean.common.enumerate.NotifyType;
import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentReplyEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NotifyEntity;
import com.oriole.ocean.common.service.*;
import com.oriole.ocean.common.vo.AuthUserEntity;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.dao.NoteCollectionDao;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/noteService")
public class NoteController {

    @Autowired
    NoteService noteService;
    @DubboReference
    UserBehaviorService userBehaviorService;
    @DubboReference
    NotifyService notifyService;
    @DubboReference
    NotifySubscriptionService notifySubscriptionService;

    @RequestMapping(value = "/getLatestNote", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteEntity>> getNoteByPage(
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageNO, pageSize, true);
        List<NoteEntity> noteEntityList = noteService.getLatestNotes();
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS","1",pageInfo);
    }

    @RequestMapping(value = "/getNotesByKeywords", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteEntity>> getNotesByKeywords(
            @RequestParam String searchString,
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageNO, pageSize, true);
        List<NoteEntity> noteEntityList = noteService.getNotesByKeywords(searchString);
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS","1",pageInfo);
    }

    @RequestMapping(value = "/getNoteById", method = RequestMethod.POST)
    public MsgEntity<NoteEntity> getNoteById(
            @RequestParam String noteId
    ) {
        return new MsgEntity<>("SUCCESS", "1", noteService.getNoteById(noteId));
    }

    @RequestMapping(value = "/createNote", method = RequestMethod.POST)
    public MsgEntity<NoteEntity> createNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam String content,
            @RequestParam String tag,
            @RequestParam Byte isAnon,
            @RequestParam Byte isAllowComment,
            @RequestParam String buildUsername) {
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

    @RequestMapping(value = "/deleteNote", method = RequestMethod.POST)
    public MsgEntity<String> deleteNote(
            @RequestParam() String noteID) {
        if(!noteService.deleteNote(noteID))
            return new MsgEntity<>("FAILED", "400", "Invalid Note ID");
        return new MsgEntity<>("SUCCESS", "1", "山本！お前の先人を犯してやる！");
    }

    @RequestMapping(value = "/getNoteByTag",method = RequestMethod.GET)
    public MsgEntity<PageInfo<NoteEntity>> getNoteByTag(
            @RequestParam(required = false) String tag,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize, true);
        List<NoteEntity> noteEntityList = noteService.getNotesByTag(tag);
        PageInfo<NoteEntity> pageInfo = new PageInfo<>(noteEntityList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }

    @RequestMapping(value = "/getNoteCommentByNoteId", method = RequestMethod.POST)
    public MsgEntity<PageInfo<NoteCommentEntity>> getNoteCommentByNoteId(
            @RequestParam String noteId,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize, true);
        List<NoteCommentEntity> noteCommentEntityList = noteService.getNoteCommentsByNoteId(noteId, pageNo, pageSize);
        PageInfo<NoteCommentEntity> pageInfo = new PageInfo<>(noteCommentEntityList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }

    @RequestMapping(value = "/createNoteComment", method = RequestMethod.POST)
    public MsgEntity<NoteCommentEntity> createNoteComment(
            @RequestParam String noteId,
            @RequestParam String userName,
            @RequestParam String commentContent,
            @RequestParam String replyTo,
            @RequestParam String replyToUsername
    ) {

        NoteCommentEntity noteCommentEntity = new NoteCommentEntity(noteId, userName, commentContent, replyTo, replyToUsername);

        noteCommentEntity.setReplyTo(replyTo);
        NoteCommentEntity noteComment = noteService.createNoteComment(noteCommentEntity);

        //构建用户消息事件
        NotifyEntity notifyEntity = new NotifyEntity(NotifyType.REMIND, userName);
        notifyEntity.setTargetIDAndType(noteId, MainType.NOTE);
        notifyEntity.setContent(commentContent);
        System.out.println(replyTo);
        System.out.println(noteId);
        if (Objects.equals(replyTo, noteId)) { // 直接回复帖子
            notifyEntity.setAction(NotifyAction.NEW_COMMENT);// 新评论事件不面向任何其他评论
        } else {
            notifyEntity.setAction(NotifyAction.NEW_REPLY);
        }
        notifyEntity.setCommentID(replyTo);
        // 增加用户消息订阅事件：用户需要订阅自己发布的评论或回复的动态
        List<NotifyAction> notifyActionList = new ArrayList<>();
        notifyActionList.add(NotifyAction.LIKE_COMMENT);
        notifyActionList.add(NotifyAction.NEW_REPLY);
        notifySubscriptionService.setNotifySubscription(userName, notifyActionList,
                replyTo, NotifySubscriptionTargetType.COMMENT);

        // 产生用户消息事件
        notifyService.addNotify(notifyEntity);

        return new MsgEntity<>("SUCCESS", "1", noteComment);
    }

    @RequestMapping(value = "/deleteNoteComment", method = RequestMethod.POST)
    public MsgEntity<String> deleteNoteComment(
            @RequestParam String _id) {
        if(!noteService.deleteNoteComment(_id))
            return new MsgEntity<>("FAILED", "400", "删除评论失败");
        return new MsgEntity<>("SUCCESS", "1", "评论删除成功");
    }

    @RequestMapping(value = "/favoriteNote", method = RequestMethod.POST)
    public MsgEntity<FavorEntity> favoriteNote(
            @RequestParam String username,
            @RequestParam Boolean isFavor,
            @RequestParam String noteId,
            @RequestParam(required = false) String id) {
        return new MsgEntity<>("SUCCESS", "1", noteService.favoriteNote(username, isFavor, noteId, id));
    }

    @RequestMapping(value = "/getBehaviourByUsernameAndNoteId", method = RequestMethod.POST)
    public MsgEntity<FavorEntity> getBehaviourByUsernameAndNoteId(
            @RequestParam String username,
            @RequestParam String noteId) {
        FavorEntity favorEntity = noteService.getBehaviourByUsernameAndNoteId(username, noteId);
        if (favorEntity == null) return new MsgEntity<>("SUCCESS", "2", null);
        return new MsgEntity<>("SUCCESS", "1", favorEntity);
    }

    @RequestMapping(value = "/getBehaviourByUsername", method = RequestMethod.POST)
    public MsgEntity<PageInfo<FavorEntity>> getBehaviourByUsername(
            @RequestParam String username,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        List<FavorEntity> collectionList = noteService.getBehaviourByUsername(username, pageNo, pageSize);
        PageInfo<FavorEntity> pageInfo = new PageInfo<>(collectionList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }
}
