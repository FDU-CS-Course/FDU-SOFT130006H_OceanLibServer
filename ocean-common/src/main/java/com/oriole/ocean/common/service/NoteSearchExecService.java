package com.oriole.ocean.common.service;

import com.oriole.ocean.common.po.mysql.NoteEntity;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.ArrayList;

public interface NoteSearchExecService {
    SearchHits<NoteEntity> searchNote(String keywords, Integer page, Integer rows);
    ArrayList<String> suggestTitle(String keyword, Integer rows);
}
