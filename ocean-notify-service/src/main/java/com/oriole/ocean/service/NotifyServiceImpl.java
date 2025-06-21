package com.oriole.ocean.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.enumerate.*;
import com.oriole.ocean.common.po.mongo.UserBehaviorEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentReplyEntity;
import com.oriole.ocean.common.po.mongo.comment.NoteCommentEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.po.mysql.NotifySubscriptionEntity;
import com.oriole.ocean.common.po.mysql.UserNotifyEntity;
import com.oriole.ocean.common.service.NotifyService;
import com.oriole.ocean.dao.NotifyDao;
import com.oriole.ocean.common.po.mysql.NotifyEntity;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@DubboService
public class NotifyServiceImpl extends ServiceImpl<NotifyDao, NotifyEntity> implements NotifyService {

    public void addNotify(NotifyEntity notifyEntity){
        save(notifyEntity);
    }

    public void addNotifyByComment(Integer bindID, MainType mainType,
                                   CommentEntity fileCommentEntity){
        NotifyEntity notifyEntity = new NotifyEntity(NotifyType.REMIND, fileCommentEntity.getCommentBuildUsername());
        notifyEntity.setTargetID(bindID.toString());
        notifyEntity.setTargetType(mainType);
        notifyEntity.setContent(fileCommentEntity.getCommentContent());
        notifyEntity.setAction(NotifyAction.NEW_COMMENT);
        addNotify(notifyEntity);
    }

    public void addNotifyByReply(Integer bindID, MainType mainType,
                                 String replyInCommentID, CommentReplyEntity fileCommentReplyEntity){
        NotifyEntity notifyEntity = new NotifyEntity(NotifyType.REMIND, fileCommentReplyEntity.getReplyBuildUsername());
        notifyEntity.setTargetID(bindID.toString());
        notifyEntity.setTargetType(mainType);
        notifyEntity.setCommentID(replyInCommentID);
        notifyEntity.setContent(fileCommentReplyEntity.getCommentContent());
        notifyEntity.setAction(NotifyAction.NEW_REPLY);
        addNotify(notifyEntity);
    }

    // 根据用户行为生产消息
    public void addNotifyByBehaviorRecord(UserBehaviorEntity userBehaviorEntity) {
        // 收藏、每日签到、阅读等行为都不需要记录为通知行为
        boolean add_or_remove = true;
        NotifyEntity notifyEntity = new NotifyEntity(NotifyType.REMIND,userBehaviorEntity.getDoUsername());
        switch (userBehaviorEntity.getBehaviorType()) {
            case DO_DOWNLOAD:
                notifyEntity.setAction(NotifyAction.DOWNLOAD);
                break;
            case DO_LIKE:
                add_or_remove = true;
                notifyEntity.setAction(NotifyAction.LIKE);
                notifyEntity.setContent(userBehaviorEntity.getDoUsername() + "点赞了你的帖子");
                break;
            case DO_DISLIKE:
                add_or_remove = false;
                notifyEntity.setAction(NotifyAction.LIKE);
                break;
            case DO_COMMENT_LIKE:
                add_or_remove = true;
                notifyEntity.setAction(NotifyAction.LIKE_COMMENT);
                notifyEntity.setCommentID((String) userBehaviorEntity.getExtraInfo(BehaviorExtraInfo.COMMENT_ID));
                notifyEntity.setContent(userBehaviorEntity.getDoUsername() + "点赞了你的评论");
                break;
            case DO_COMMENT_DISLIKE:
                add_or_remove = false;
                notifyEntity.setAction(NotifyAction.LIKE_COMMENT);
                notifyEntity.setCommentID((String) userBehaviorEntity.getExtraInfo(BehaviorExtraInfo.COMMENT_ID));
                break;
            default:
                return;
        }
        notifyEntity.setUserBehaviorID(userBehaviorEntity.getId());
        notifyEntity.setTargetIDAndType(String.valueOf(userBehaviorEntity.getBindID()),userBehaviorEntity.getType());
        if(add_or_remove)
            addNotify(notifyEntity);
        else
            removeNotify(notifyEntity);
    }

    private void removeNotify(NotifyEntity notifyEntity) {
        NotifyDao notifyDao = getBaseMapper();
        notifyDao.removeNotify(notifyEntity);
    }

    // 查询指定时间之后产生的所有消息
    public List<NotifyEntity> getAllNotifyAfterTime(Date date) {
        QueryWrapper<NotifyEntity> queryWrapper = new QueryWrapper<>();
        if (date != null) {
            queryWrapper.gt("build_date", date);
        }
        return list(queryWrapper);
    }

}
