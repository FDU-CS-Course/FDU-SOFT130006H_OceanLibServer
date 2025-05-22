package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mongo.UserBehaviorEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.service.NoteService;
import com.oriole.ocean.dao.NoteDao;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    private void addNote(NoteEntity note){
        save(note);
    }

    private void addNoteComment(NoteCommentEntity noteComment){
        mongoTemplate.save(noteComment, "note_comments");
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

    public NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity) {
        noteCommentEntity.setLikeNum(0);
        noteCommentEntity.setCreateTime(new Date());

        addNoteComment(noteCommentEntity);

        return noteCommentEntity;
    }

    public List<NoteCommentEntity> getNoteCommentsByNoteId(Long noteId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Query query = new Query();
        query.addCriteria(Criteria.where("noteId").is(noteId));
        query.with(pageable);

        System.out.println(mongoTemplate.find(query, NoteCommentEntity.class));
        return mongoTemplate.find(query, NoteCommentEntity.class, "note_comments");
    }
} 