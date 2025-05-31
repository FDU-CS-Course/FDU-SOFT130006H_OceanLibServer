package com.oriole.ocean.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mongo.FavorEntity;
import com.oriole.ocean.common.po.mongo.UserBehaviorEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.service.NoteSearchSyncService;
import com.oriole.ocean.common.service.NoteService;
import com.oriole.ocean.dao.NoteCollectionDao;
import com.oriole.ocean.dao.NoteCommentDao;
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

    @Resource
    private NoteCollectionDao noteCollectionDao;

    @Resource
    private NoteCommentDao noteCommentDao;

    @Autowired
    private NoteSearchSyncService noteSearchSyncService;

    public boolean isNoteCreator(String noteId, String username) {
        return noteDao.getNoteById(noteId).getBuildUsername().equals(username);
    }

    public void deleteNote(String noteID){
        noteSearchSyncService.deleteNoteSearchInfo(noteID);
        noteDao.deleteNote(noteID);
    }

    public NoteEntity getNoteById(String noteID) {
        return noteDao.getNoteById(noteID);
    }

    public FavorEntity favoriteNote(String username, boolean isFavor, String noteID, String id){

        if (isFavor) {
            FavorEntity favorEntity = new FavorEntity();
            favorEntity.setUsername(username);
            favorEntity.setNoteId(noteID);
            noteCollectionDao.save(favorEntity);
            return noteCollectionDao.findByUsernameAndNoteId(username, noteID);
        }
        else {
            FavorEntity favorEntity = new FavorEntity();
            favorEntity.setUsername(username);
            favorEntity.setNoteId(noteID);
            favorEntity.setId(id);
            noteCollectionDao.delete(favorEntity);
            return null;
        }
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

        save(noteEntity);
        noteSearchSyncService.saveOrUpdateNoteSearchInfo(noteEntity);

        return noteEntity;
    }

    public NoteCommentEntity createNoteComment(NoteCommentEntity noteCommentEntity) {
        noteCommentEntity.setLikeNum(0);
        noteCommentEntity.setCreateTime(new Date());

        noteCommentDao.addNoteComment(noteCommentEntity);

        return noteCommentEntity;
    }

    public List<NoteCommentEntity> getNoteCommentsByNoteId(String noteId, int pageNo, int pageSize) {
        return noteCommentDao.getNoteCommentsByNoteId(noteId, pageNo, pageSize);
    }

    public boolean isCommentCreator(String commentId, String username) {
        return noteCommentDao.getNoteComment(commentId).getNoteCommentBuildUsername().equals(username);
    }

    public void deleteNoteComment(String commentId) {
        noteCommentDao.deleteNoteComment(commentId);
    }

    public FavorEntity getBehaviourByUsernameAndNoteId(String username, String noteId) {
        return noteCollectionDao.findByUsernameAndNoteId(username, noteId);
    }

    public List<FavorEntity> getBehaviourByUsername(String username, int pageNo, int pageSize) {
        return noteCollectionDao.getBehaviourByUsername(username, pageNo, pageSize);
    }

} 