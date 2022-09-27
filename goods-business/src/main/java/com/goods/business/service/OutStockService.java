package com.goods.business.service;

import com.goods.common.model.business.OutStock;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 19:13
 */
public interface OutStockService {
    // 出库单（发放记录）数据分页列表展示
    List<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStock outStock);

    // 添加出库单（发放物资）
    void addOutStock(OutStockVO outStockVO);

    //查询出库单（明细）
    OutStockDetailVO getOutStockDetail(Long id, Integer pageNum);

    // 通过审核
    void publish(Long id);

    // 回收站还原
    void backById(Long id);

    // 放入回收站
    void removeById(Long id);
    // 删除
    void deleteById(Long id);
}
