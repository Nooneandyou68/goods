package com.goods.business.service;

import com.goods.common.model.business.Supplier;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 20:24
 */
public interface SupplierService {
    //获取物资列表
    List<Supplier> findSupplierList(Integer pageNum, Integer pageSize, Supplier supplier);

    //添加来源
    void saveSupplier(Long id,Supplier supplier);

    //修改数据回显
    Supplier getSupplierById(long id);

    //删除
    void deleteSupplier(Long id);

    //入库（进行物资来源选择）
    List<Supplier> findAll();

}
