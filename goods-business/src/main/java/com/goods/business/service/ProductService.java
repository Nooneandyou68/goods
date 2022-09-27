package com.goods.business.service;

import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 23:27
 */
public interface ProductService {

    //物资资料分页列表展示
    List<Product> findProductList(Integer pageNum, Integer pageSize,ProductVO productVO, String categorys);

    //物资资料保存
    void saveProduct(ProductVO productVO);

    //物资资料放入回收站
    void removeProduct(Long id);

    // 物资资料删除
    void deleteProduct(Long id);

    // 物资资料修改回显
    Product getProductById(Long id);

    // 物资资料修改保存
    void updateProduct(ProductVO productVO, Long id);

    // 入库单（物资来源查询）
    List<Product> findProducts(Integer pageNum, Integer pageSize, Integer status);


    // 物资库存
    List<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize);
}
