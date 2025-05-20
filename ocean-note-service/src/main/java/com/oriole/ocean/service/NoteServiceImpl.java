package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.service.NoteService;
import com.oriole.ocean.dao.NoteDao;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NoteServiceImpl extends ServiceImpl<NoteDao, NoteEntity> implements NoteService {
    @Resource
    private NoteDao noteDao;

    private void addNote(NoteEntity note){
        save(note);
    }

    public boolean deleteNote(int noteID, String username){
        if(!noteDao.checkNoteBuilder(noteID, username)) {
            return false;
        }
        noteDao.deleteNote(noteID, username);
        return true;
    }

    public List<NoteEntity> getLatestNotes() {
        return noteDao.getLatestNotes();
    }

    public List<NoteEntity> getNotesByKeywords(String serchString) {
        return noteDao.getNotesByKeywords(serchString);
    }

    public List<NoteEntity> getNotesByTag(String tag) {
        return noteDao.getNotesByTag(tag);
    }

    public NoteEntity createNote(NoteEntity noteEntity){
        noteEntity.setBuildDate(new Date());
        noteEntity.setRefreshDate(new Date());
        noteEntity.setCommentNum(0);
        noteEntity.setLikeNum(0);
        noteEntity.setReadNum(0);
        noteEntity.setIsDeleted((byte) 0);

        addNote(noteEntity);

        return noteEntity;
    }
} 