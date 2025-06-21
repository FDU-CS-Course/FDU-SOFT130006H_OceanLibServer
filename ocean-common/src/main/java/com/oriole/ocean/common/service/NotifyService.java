package com.oriole.ocean.common.service;

import com.oriole.ocean.common.enumerate.MainType;
import com.oriole.ocean.common.po.mongo.UserBehaviorEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentEntity;
import com.oriole.ocean.common.po.mongo.comment.CommentReplyEntity;
import com.oriole.ocean.common.po.mysql.NotifyEntity;

public interface NotifyService {
    void addNotify(NotifyEntity notifyEntity);
    void addNotifyByComment(Integer bindID, MainType mainType,
                                   CommentEntity fileCommentEntity);
    void addNotifyByReply(Integer bindID, MainType mainType,
                                 String replyInCommentID, CommentReplyEntity fileCommentReplyEntity);
    void addNotifyByBehaviorRecord(UserBehaviorEntity userBehaviorEntity);
}
