package com.oriole.ocean.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.service.NoteSearchExecService;
import com.oriole.ocean.common.vo.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/noteSearchService")
public class NoteSearchController {
    @Autowired
    NoteSearchExecService noteSearchExecService;

    @RequestMapping(value = "/getNotesByKeywords", method = RequestMethod.POST)
    public MsgEntity<SearchHits<NoteEntity>> getNotesByKeywords(
            @RequestParam String searchString,
            @RequestParam int pageNO,
            @RequestParam int pageSize) {
        return new MsgEntity<>("SUCCESS","1",
                noteSearchExecService.searchNote(searchString,pageNO,pageSize));
    }

    @RequestMapping(value = "/suggest",method = RequestMethod.GET)
    public MsgEntity<ArrayList<String>> suggestTitle(@RequestParam String keyword, @RequestParam Integer rows) {
        ArrayList<String> suggests = noteSearchExecService.suggestTitle(keyword, rows);
        return new MsgEntity<>("SUCCESS", "1", suggests);
    }
}
