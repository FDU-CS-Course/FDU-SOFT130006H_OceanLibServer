package com.oriole.ocean.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.dao.NoteDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteService extends ServiceImpl<NoteDao, NoteEntity> {
    public Page<NoteEntity> getNoteByNoteType(Integer noteType, Page<NoteEntity> page){
        QueryWrapper<NoteEntity> queryWrapper = new QueryWrapper<>();
        if(noteType!=null) {
            queryWrapper.eq("note_type", noteType);
        }
        return page(page, queryWrapper);
    }
}
