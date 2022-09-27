package com.goods.business.service;

import com.goods.common.model.business.Health;
import com.goods.common.vo.business.HealthVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/26 8:58
 */
public interface HealthService {
    // 健康打卡提示
    Health isReport();

    // 立即打卡
    void report(Health health);

    // 签到记录
    List<Health> history(Integer pageNum, Integer pageSize);
}
