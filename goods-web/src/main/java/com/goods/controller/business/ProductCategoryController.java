package com.goods.controller.business;

import com.goods.business.service.ProductCategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 14:10
 */
@Api(tags = "业务模块-物资类别相关接口")
@RestController
@RequestMapping("business/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 类别列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 14:23
     */
    //http://www.localhost:8989/business/productCategory/categoryTree?pageNum=1&pageSize=5
    @GetMapping("categoryTree")
    public ResponseBean<Map<String, Object>> getProductCategory(Integer pageNum, Integer pageSize) {
        // 调用服务层
        List<ProductCategoryTreeNodeVO> productCategoryList = this.productCategoryService.findProductCategoryList();
        Map<String, Object> map = new HashMap<>();
        if (pageNum != null && pageSize != null && pageNum != 0 && pageSize != 0) {
            List<ProductCategoryTreeNodeVO> page = ListPageUtils.page(productCategoryList, pageSize, pageNum);
            map.put("total", productCategoryList.size());
            map.put("rows", page);
        } else {
            map.put("total", productCategoryList.size());
            map.put("rows", productCategoryList);
        }
        // 返回数据
        return ResponseBean.success(map);
    }

    /**
     * 添加分类数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    // http://192.168.31.37:8989/business/productCategory/getParentCategoryTree
    @GetMapping("getParentCategoryTree")
    public ResponseBean<List<ProductCategoryTreeNodeVO>> getParentCategoryTree() {
        // 调用服务层
        List<ProductCategoryTreeNodeVO> parentCategoryTree = this.productCategoryService.getParentCategoryTree();
        // 返回数据
        return ResponseBean.success(parentCategoryTree);
    }

    /**
     * 保存分类
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    // http://192.168.31.37:8989/business/productCategory/getParentCategoryTree
    @PostMapping("add")
    public ResponseBean add(@RequestBody ProductCategory productCategory) {
        // 调用服务层
        this.productCategoryService.saveProductCategory(productCategory);
        // 返回数据
        return ResponseBean.success();
    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    // http://www.localhost:8989/business/productCategory/edit/80
    @GetMapping("edit/{categoryId}")
    public ResponseBean<ProductCategory> edit(@PathVariable Long categoryId) {
        // 调用服务层
        ProductCategory productCategory = this.productCategoryService.getParentCategoryById(categoryId);
        // 返回数据
        return ResponseBean.success(productCategory);
    }

    /**
     * 修改数据保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    @PutMapping("update/{categoryId}")
    public ResponseBean update(@PathVariable Long categoryId, @RequestBody ProductCategory productCategory) {
        // 调用服务层
        this.productCategoryService.updateProductCategory(categoryId, productCategory);
        // 返回数据
        return ResponseBean.success();
    }

    /**
     * 删除分类数据
     *@author SongBoHao
     *@date 2022/9/23 20:12
     *@param
     *@return
     */
    //http://www.localhost:8989/business/productCategory/delete/80
    @DeleteMapping("delete/{categoryId}")
    public ResponseBean delete(@PathVariable Long categoryId) {
        // 调用服务层
        this.productCategoryService.deleteProductCategory(categoryId);
        // 返回数据
        return ResponseBean.success();
    }

}



