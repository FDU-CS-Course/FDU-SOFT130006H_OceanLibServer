package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import java.util.List;

public interface NoteService {

    boolean deleteNote(int noteID, String username);

    List<NoteEntity> getLatestNotes();

    List<NoteEntity> getNotesByKeywords(String serchString);

    List<NoteEntity> getNotesByTag(String tag);

    List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId, int pageNo, int pageSize);

    NoteEntity createNote(NoteEntity noteEntity);

    NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity);
}
