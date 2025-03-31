package com.oriole.ocean.common.service;

import com.oriole.ocean.common.enumerate.NotifyAction;
import com.oriole.ocean.common.enumerate.NotifySubscriptionTargetType;

import java.util.List;

public interface NotifySubscriptionService {
    void setNotifySubscription(String username, List<NotifyAction> notifyActionList, String targetID, NotifySubscriptionTargetType targetType);
}