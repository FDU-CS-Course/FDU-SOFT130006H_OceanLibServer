package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mysql.NoteEntity;

public interface NoteSearchSyncService {
    void saveOrUpdateNoteSearchInfo(NoteEntity noteEntity);
    void deleteNoteSearchInfo(String noteId);
}
