package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.UserNotifyEntity;

import java.util.Date;
import java.util.List;

public interface NoteDao extends BaseMapper<NoteEntity> {
    List<NoteEntity> getLatestNotes();
    List<NoteEntity> getNotesByKeywords(String serchString);
    void deleteNote(String noteId);
    boolean checkNoteBuilder(String noteId);
    List<NoteEntity> getNotesByTag(String tag);
    List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId);
}
