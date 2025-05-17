package com.oriole.ocean.service;

import com.oriole.ocean.common.dto.NoteEntityDTO;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import java.util.List;

public interface NoteService {
    /**
     * 根据笔记类型获取笔记列表
     * @param noteType 笔记类型
     * @return 笔记列表
     */
    List<NoteEntity> getNoteByNoteType(Integer noteType);

    /**
     * 创建新笔记
     * @param noteEntity 笔记信息
     * @return 创建的笔记实体
     */
    NoteEntity createNote(NoteEntityDTO noteEntity);
}
