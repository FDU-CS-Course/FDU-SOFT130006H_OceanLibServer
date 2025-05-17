package com.oriole.ocean.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.dao.NoteTypeDao;
import com.oriole.ocean.common.po.mysql.NoteTypeEntity;
import com.oriole.ocean.service.NoteTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteTypeServiceImpl extends ServiceImpl<NoteTypeDao, NoteTypeEntity> implements NoteTypeService {
    
    @Override
    public List<NoteTypeEntity> getAllNoteType() {
        return list();
    }
} 