package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.UserNotifyEntity;

import java.util.Date;
import java.util.List;

public interface NoteDao extends BaseMapper<NoteEntity> {
    List<NoteEntity> getLatestNotes();
    void deleteNote(int noteId, String username);
    boolean checkNoteBuilder(int noteId, String username);
    List<NoteEntity> getNotesByTag(String tag);
}
