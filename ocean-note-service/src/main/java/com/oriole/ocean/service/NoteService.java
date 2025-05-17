package com.oriole.ocean.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.dao.NoteDao;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NoteService extends ServiceImpl<NoteDao, NoteEntity> {

    @Resource
    private NoteDao noteDao;

    public List<NoteEntity> getNoteByNoteType(Integer noteType){
        QueryWrapper<NoteEntity> queryWrapper = new QueryWrapper<>();
        if(noteType!=null) {
            queryWrapper.eq("note_type", noteType);
        }
        return list(queryWrapper);
    }

    public void addNote(NoteEntity note){
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
