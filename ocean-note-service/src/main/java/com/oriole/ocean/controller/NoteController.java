package com.oriole.ocean.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oriole.ocean.common.auth.AuthUser;
import com.oriole.ocean.common.po.mongo.comment.CommentReplyEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.vo.AuthUserEntity;
import com.oriole.ocean.common.vo.MsgEntity;
import com.oriole.ocean.common.service.NoteService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/noteService")
public class NoteController {

    @Autowired
    NoteService noteService;

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
        return new MsgEntity<>("SUCCESS", "1", note);
    }

    @RequestMapping(value = "/deleteNote", method = RequestMethod.POST)
    public MsgEntity<String> deleteNote(
            @AuthUser AuthUserEntity authUser,
            @RequestParam() int noteID) {
        if(!noteService.deleteNote(noteID, authUser.getUsername()))
            return new MsgEntity<>("FAILED", "400", "Invalid Note ID or Wrong User");
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
            @RequestParam Long noteId,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize, true);
        List<NoteCommentEntity> noteCommentEntityList = noteService.getNoteCommentsByNoteId(noteId, pageNo, pageSize);
        PageInfo<NoteCommentEntity> pageInfo = new PageInfo<>(noteCommentEntityList);
        return new MsgEntity<>("SUCCESS", "1", pageInfo);
    }

    @RequestMapping(value = "/createNoteComment", method = RequestMethod.POST)
    public MsgEntity<NoteCommentEntity> createNoteComment(
            @RequestParam Long noteId,
            @RequestParam String userName,
            @RequestParam String commentContent,
            @RequestParam String replyTo,
            @RequestParam String replyToUsername
    ) {

        String cid = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        NoteCommentEntity noteCommentEntity = new NoteCommentEntity(noteId, userName, commentContent, replyTo, replyToUsername);

        noteCommentEntity.setReplyTo(replyTo);
        NoteCommentEntity noteComment = noteService.createNoteComment(noteCommentEntity);

        return new MsgEntity<>("SUCCESS", "1", noteComment);
    }
}
