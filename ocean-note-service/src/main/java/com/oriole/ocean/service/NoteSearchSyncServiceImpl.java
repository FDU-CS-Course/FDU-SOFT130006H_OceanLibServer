package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.es.NoteSearchEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.service.NoteSearchSyncService;
import com.oriole.ocean.dao.NoteSearchDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DubboService
@Transactional
public class NoteSearchSyncServiceImpl extends ServiceImpl<NoteSearchDao, NoteSearchEntity> implements NoteSearchSyncService {

    @Override
    public void saveOrUpdateNoteSearchInfo(NoteEntity noteEntity) {
        saveOrUpdate(convertToSearchEntity(noteEntity));
    }

    @Override
    public void deleteNoteSearchInfo(String noteId) {
        removeById(noteId);
    }

    static private NoteSearchEntity convertToSearchEntity(NoteEntity noteEntity) {
        NoteSearchEntity noteSearchEntity = new NoteSearchEntity();
        BeanUtils.copyProperties(noteEntity, noteSearchEntity);
        return noteSearchEntity;
    }
}
