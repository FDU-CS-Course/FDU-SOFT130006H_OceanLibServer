package com.oriole.ocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.dto.NoteEntityDTO;
import com.oriole.ocean.dao.NoteDao;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NoteServiceImpl extends ServiceImpl<NoteDao, NoteEntity> implements NoteService {
    
    @Override
    public List<NoteEntity> getNoteByNoteType(Integer noteType) {
        QueryWrapper<NoteEntity> queryWrapper = new QueryWrapper<>();
        if(noteType != null) {
            queryWrapper.eq("note_type", noteType);
        }
        return list(queryWrapper);
    }

    @Override
    public NoteEntity createNote(NoteEntityDTO noteEntity) {
        NoteEntity note = new NoteEntity();
        note.setTag(noteEntity.getTag());
        note.setContent(noteEntity.getContent());
        note.setBuildUsername(noteEntity.getBuildUsername());
        note.setIsAnon(noteEntity.getIsAnon());
        note.setIsApproved(noteEntity.getIsApproved());
        note.setIsAllowComment(noteEntity.getIsAllowComment());

        note.setLikeNum(0);
        note.setCommentNum(0);
        note.setReadNum(0);
        note.setBuildDate(new Date());

        // TODO: write note into mysql; allocate noteID

        return note;
    }
} 