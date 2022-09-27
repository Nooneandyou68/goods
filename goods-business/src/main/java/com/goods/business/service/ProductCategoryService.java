package com.goods.business.service;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 14:12
 */
public interface ProductCategoryService {

    // 类别列表展示
    List<ProductCategoryTreeNodeVO> findProductCategoryList();

    // 分类
    List<ProductCategoryTreeNodeVO> getParentCategoryTree();

    // 保存分类
    void saveProductCategory(ProductCategory productCategory);

    // 修改数据回显
    ProductCategory getParentCategoryById(Long categoryId);

    // 修改数据保存
    void updateProductCategory(Long categoryId, ProductCategory productCategory);

    // 删除分类数据
    void deleteProductCategory(Long categoryId);
}
