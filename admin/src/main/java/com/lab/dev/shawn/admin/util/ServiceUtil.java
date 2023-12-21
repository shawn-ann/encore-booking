package com.lab.dev.shawn.admin.util;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.entity.BaseEntity;

import java.time.LocalDateTime;

public class ServiceUtil {
    public static void handleCreate(BaseEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateDate(now);
        entity.setStatus(BaseStatus.ACTIVE);
        entity.setUpdateDate(now);
        entity.setVersion(0);
    }
}
