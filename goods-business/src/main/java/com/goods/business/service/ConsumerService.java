package com.goods.business.service;

import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 16:50
 */
public interface ConsumerService {
    //物资去处分页列表展示
    List<Consumer> findConsumerList(Integer pageNum, Integer pageSize, Consumer consumer);

    // 添加物资去处
    void insertConsumer(ConsumerVO consumerVO);

    // 修改数据回显
    ConsumerVO getConsumerById(Long id);

    // 修改数据保存
    void updateConsumerById(Long id, ConsumerVO consumerVO);

    // 删除物资去处
    void deleteConsumerById(Long id);

    //物资出库查询已知去向
    List<ConsumerVO> findAll();

}
