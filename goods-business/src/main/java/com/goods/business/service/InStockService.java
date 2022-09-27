package com.goods.business.service;

import com.goods.common.model.business.InStock;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/24 14:23
 */
public interface InStockService {
    // 入库单（入库记录）数据分页列表展示
    List<InStockVO> getInStockList(Integer pageNum, Integer pageSize, InStock inStock);

    // 入库单（入库明细）数据
    InStockDetailVO getInStockDetail(Long id, Integer pageNum);

    // 放入回收站
    void removeById(Long id);

    // 入库单（保存）数据
    void addIntoStock(InStockVO inStockVO);

    // 回收站还原
    void backById(Long id);

    // 删除
    void deleteById(Long id);

    // 通过审核
    void publish(Long id);
}
