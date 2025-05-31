package com.oriole.ocean.dao;

import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteCommentDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addNoteComment(NoteCommentEntity noteComment) {
        mongoTemplate.save(noteComment, "note_comments");
    }

    public List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        // 创建查询条件，同时支持String和Long类型的noteId（为了兼容旧数据）
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("noteId").is(noteId),
                Criteria.where("noteId").is(Long.parseLong(noteId))
        );

        Query query = new Query(criteria);
        query.with(pageable);

        return mongoTemplate.find(query, NoteCommentEntity.class, "note_comments");
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

    public NoteCommentEntity getNoteComment(String noteId) {
        return mongoTemplate.findById(noteId, NoteCommentEntity.class, "note_comments");
    }
}
