package com.oriole.ocean.service;

import com.oriole.ocean.common.po.mysql.NoteTypeEntity;
import java.util.List;

public interface NoteTypeService {
    /**
     * 获取所有笔记类型
     * @return 笔记类型列表
     */
    List<NoteTypeEntity> getAllNoteType();
}
