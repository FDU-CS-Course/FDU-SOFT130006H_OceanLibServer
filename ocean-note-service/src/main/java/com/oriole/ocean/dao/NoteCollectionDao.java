package com.oriole.ocean.dao;

import com.oriole.ocean.common.po.mongo.FavorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteCollectionDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    // 根据用户名和笔记ID查找收藏（用于删除时获取完整对象）
    public FavorEntity findByUsernameAndNoteId(String username, String noteId) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("username").is(username),
                Criteria.where("noteId").is(noteId)
        );

        Query query = new Query(criteria);

        List<FavorEntity> res = mongoTemplate.find(query, FavorEntity.class, "note_favor");
        if (res.isEmpty()) return null;
        return res.iterator().next();
    }

    public List<FavorEntity> getBehaviourByUsername(String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("username").is(username)
        );

        Query query = new Query(criteria);
        query.with(pageable);

        return mongoTemplate.find(query, FavorEntity.class, "note_favor");
    }

    public void save(FavorEntity favorEntity) {
        mongoTemplate.save(favorEntity, "note_favor");
    }

    public void delete(FavorEntity favorEntity) {
        mongoTemplate.remove(favorEntity, "note_favor");
    }
}
