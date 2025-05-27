package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import java.util.List;

public interface NoteService {

    boolean deleteNote(String noteID);

    FavorEntity favoriteNote(String username, boolean isFavor, String noteID, String id);

    NoteEntity getNoteById(String noteID);

    boolean deleteNoteComment(String commentId);

    FavorEntity getBehaviourByUsernameAndNoteId(String username, String noteId);

    List<FavorEntity> getBehaviourByUsername(String username, int pageNo, int pageSize);

    List<NoteEntity> getLatestNotes();

    List<NoteEntity> getNotesByKeywords(String serchString);

    List<NoteEntity> getNotesByTag(String tag);

    List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId, int pageNo, int pageSize);

    NoteEntity createNote(NoteEntity noteEntity);

    NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity);
}
