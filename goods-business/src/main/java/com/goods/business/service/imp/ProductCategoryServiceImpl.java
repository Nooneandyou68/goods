package com.goods.business.service.imp;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 14:12
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 类别列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 14:23
     */
    @Override
    public List<ProductCategoryTreeNodeVO> findProductCategoryList() {
        Example example1 = new Example(ProductCategory.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("pid", 0L);
        // 获取一级分类数据
        List<ProductCategory> productCategory1List = this.productCategoryMapper.selectByExample(example1);

        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVO1List = productCategory1List.stream().map(productCategory1 -> {
            ProductCategoryTreeNodeVO productCategoryTreeNodeVO1 = new ProductCategoryTreeNodeVO();
            // 获取二级分类数据
            Example example2 = new Example(ProductCategory.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("pid", productCategory1.getId());
            List<ProductCategory> productCategory2List = this.productCategoryMapper.selectByExample(example2);
            List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVO2List = productCategory2List.stream().map(productCategory2 -> {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO2 = new ProductCategoryTreeNodeVO();
                productCategoryTreeNodeVO2.setId(productCategory2.getId());
                productCategoryTreeNodeVO2.setName(productCategory2.getName());
                productCategoryTreeNodeVO2.setRemark(productCategory2.getRemark());
                productCategoryTreeNodeVO2.setSort(productCategory2.getSort());
                productCategoryTreeNodeVO2.setLev(2);
                productCategoryTreeNodeVO2.setPid(productCategory2.getPid());
                productCategoryTreeNodeVO2.setCreateTime(productCategory2.getCreateTime());
                productCategoryTreeNodeVO2.setModifiedTime(productCategory2.getModifiedTime());
                // 获取三级分类数据
                Example example3 = new Example(ProductCategory.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("pid", productCategory2.getId());
                // 获取三级级分类数据
                List<ProductCategory> productCategory3List = this.productCategoryMapper.selectByExample(example3);
                List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVO3List = productCategory3List.stream().map(productCategory3 -> {
                    ProductCategoryTreeNodeVO productCategoryTreeNodeVO3 = new ProductCategoryTreeNodeVO();
                    productCategoryTreeNodeVO3.setId(productCategory3.getId());
                    productCategoryTreeNodeVO3.setName(productCategory3.getName());
                    productCategoryTreeNodeVO3.setRemark(productCategory3.getRemark());
                    productCategoryTreeNodeVO3.setSort(productCategory3.getSort());
                    productCategoryTreeNodeVO3.setLev(3);
                    productCategoryTreeNodeVO3.setPid(productCategory3.getPid());
                    productCategoryTreeNodeVO3.setCreateTime(productCategory3.getCreateTime());
                    productCategoryTreeNodeVO3.setModifiedTime(productCategory3.getModifiedTime());
                    return productCategoryTreeNodeVO3;
                }).collect(Collectors.toList());
                // 赋值三级分类数据
                productCategoryTreeNodeVO2.setChildren(productCategoryTreeNodeVO3List);
                return productCategoryTreeNodeVO2;
            }).collect(Collectors.toList());
            // 属性赋值
            productCategoryTreeNodeVO1.setId(productCategory1.getId());
            productCategoryTreeNodeVO1.setName(productCategory1.getName());
            productCategoryTreeNodeVO1.setRemark(productCategory1.getRemark());
            productCategoryTreeNodeVO1.setSort(productCategory1.getSort());
            productCategoryTreeNodeVO1.setLev(1);
            productCategoryTreeNodeVO1.setPid(productCategory1.getPid());
            productCategoryTreeNodeVO1.setCreateTime(productCategory1.getCreateTime());
            productCategoryTreeNodeVO1.setModifiedTime(productCategory1.getModifiedTime());
            // 赋值二级分类数据
            productCategoryTreeNodeVO1.setChildren(productCategoryTreeNodeVO2List);
            return productCategoryTreeNodeVO1;
        }).collect(Collectors.toList());

        return productCategoryTreeNodeVO1List;
    }

    /**
     * 回显父类数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        return this.findProductCategoryList();
    }

    /**
     * 添加分类
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    @Override
    public void saveProductCategory(ProductCategory productCategory) {
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        this.productCategoryMapper.insert(productCategory);
    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 19:10
     */
    @Override
    public ProductCategory getParentCategoryById(Long categoryId) {
        return this.productCategoryMapper.selectByPrimaryKey(categoryId);
    }

    /**
     * 修改数据保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:07
     */
    @Override
    public void updateProductCategory(Long categoryId, ProductCategory productCategory) {
        Example example1 = new Example(ProductCategory.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("id", categoryId);
        this.productCategoryMapper.updateByExampleSelective(productCategory, example1);
    }

    /**
     * 删除分类数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:12
     */
    @Override
    public void deleteProductCategory(Long categoryId) {
        this.productCategoryMapper.deleteByPrimaryKey(categoryId);
    }
}
