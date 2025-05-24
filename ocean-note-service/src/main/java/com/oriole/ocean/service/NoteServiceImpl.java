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

    public boolean deleteNote(String noteID){
        if(!noteDao.checkNoteBuilder(noteID)) {
            return false;
        }

        noteDao.deleteNote(noteID);
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

    public List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        
        // 创建查询条件，同时支持String和Long类型的noteId（为了兼容旧数据）
        Criteria criteria = new Criteria().orOperator(
            Criteria.where("noteId").is(noteId),
            Criteria.where("noteId").is(tryParseLong(noteId))
        );
        
        Query query = new Query(criteria);
        query.with(pageable);

        return mongoTemplate.find(query, NoteCommentEntity.class, "note_comments");
    }
    
    /**
     * 尝试将字符串转换为Long，如果失败则返回null
     */
    private Long tryParseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean deleteNoteComment(String commentId) {
        try {
            Query query = new Query(Criteria.where("_id").is(commentId));
            mongoTemplate.remove(query, NoteCommentEntity.class, "note_comments");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 