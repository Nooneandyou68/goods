package com.goods.business.service.imp;

import com.goods.business.mapper.InStockInfoMapper;
import com.goods.business.mapper.InStockMapper;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.service.ProductService;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 23:28
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductStockMapper productStockMapper;

    /**
     * 物资资料分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:31
     */
    @Override
    public List<Product> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO, String categorys) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(categorys) && !"".equals(categorys)) {
            String[] split = categorys.split(",");
            Long[] categoryIds = new Long[split.length];
            if (split != null && split.length > 0) {
                categoryIds[0] = Long.parseLong(split[0]);
                if (categoryIds.length > 1) {
                    categoryIds[1] = Long.parseLong(split[1]);
                }
                if (categoryIds.length > 2) {
                    categoryIds[2] = Long.parseLong(split[2]);
                }
                productVO.setCategoryKeys(categoryIds);
            }
        }
        // 判断是否为空
        if (!StringUtils.isEmpty(productVO)) {
            //判断name是否为空
            if (productVO.getName() != null && !"".equals(productVO.getName())) {
                criteria.andEqualTo("name", productVO.getName());
                //判断状态是否为空
            } else if (productVO.getStatus() != null && productVO.getStatus() != 0) {
                criteria.andEqualTo("status",  productVO.getStatus().toString());
                // 判断是否根据分类id搜索
            } else if (productVO.getCategoryKeys() != null && productVO.getCategoryKeys().length > 0) {
                if (productVO.getCategoryKeys()[0] != null && productVO.getCategoryKeys()[0] != null) {
                    criteria.andEqualTo("oneCategoryId", productVO.getCategoryKeys()[0]);
                } else if (productVO.getCategoryKeys()[1] != null && productVO.getCategoryKeys()[1] != null) {
                    example.createCriteria().andEqualTo("twoCategoryId", productVO.getCategoryKeys()[1]);
                } else if (productVO.getCategoryKeys()[2] != null && productVO.getCategoryKeys()[2] != null) {
                    criteria.andEqualTo("threeCategoryId", productVO.getCategoryKeys()[2]);
                }
                //如果条件都有
            } else if (productVO.getName() != null && !"".equals(productVO.getName()) &&
                    productVO.getStatus() != null && productVO.getStatus() != 0
                    && productVO.getCategoryKeys() != null && productVO.getCategoryKeys().length > 0) {
                example.createCriteria().andLike("name", "%" + productVO.getName() + "%");
                criteria.andLike("status", "%" + productVO.getStatus().toString() + "%");
                if (productVO.getCategoryKeys()[0] != null && productVO.getCategoryKeys()[0] != null) {
                    criteria.andEqualTo("oneCategoryId", productVO.getCategoryKeys()[0]);
                } else if (productVO.getCategoryKeys()[1] != null && productVO.getCategoryKeys()[1] != null) {
                    criteria.andEqualTo("twoCategoryId", productVO.getCategoryKeys()[1]);
                } else if (productVO.getCategoryKeys()[2] != null && productVO.getCategoryKeys()[2] != null) {
                    criteria.andEqualTo("threeCategoryId", productVO.getCategoryKeys()[2]);
                }
            }
                List<Product> selectByExample = this.productMapper.selectByExample(example);
                if (!CollectionUtils.isEmpty(selectByExample)) {
                    return ListPageUtils.page(selectByExample, pageSize, pageNum);
                } else {
                    return null;
            }
        }

        return this.productMapper.selectAll();
    }
    /**
     * 物资资料保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    @Override
    public void saveProduct(ProductVO productVO) {
        productVO.setPNum(UUID.randomUUID().toString());
        productVO.setCreateTime(new Date());
        productVO.setModifiedTime(new Date());
        productVO.setStatus(0);
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        Long categoryKey1 = productVO.getCategoryKeys()[0];
        Long categoryKey2 = productVO.getCategoryKeys()[1];
        Long categoryKey3 = productVO.getCategoryKeys()[2];
        product.setOneCategoryId(categoryKey1);
        product.setTwoCategoryId(categoryKey2);
        product.setThreeCategoryId(categoryKey3);
        this.productMapper.insert(product);
    }

    /**
     * 物资资料删除
     * 逻辑删除
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 9:43
     */
    @Override
    public void removeProduct(Long id) {
        // 根据id先查询
        Product product = this.productMapper.selectByPrimaryKey(id);
        product.setStatus(1);
        // 修改
        this.productMapper.updateByPrimaryKey(product);
    }

    /**
     * 物资资料删除
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    @Override
    public void deleteProduct(Long id) {
        this.productMapper.deleteByPrimaryKey(id);
    }

    /**
     * 物资资料修改回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    @Override
    public Product getProductById(Long id) {
        return this.productMapper.selectByPrimaryKey(id);
    }

    /**
     * 物资资料修改保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    @Override
    public void updateProduct(ProductVO productVO, Long id) {
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        Long categoryKey1 = productVO.getCategoryKeys()[0];
        Long categoryKey2 = productVO.getCategoryKeys()[1];
        Long categoryKey3 = productVO.getCategoryKeys()[2];
        product.setId(id);
        product.setOneCategoryId(categoryKey1);
        product.setTwoCategoryId(categoryKey2);
        product.setThreeCategoryId(categoryKey3);
        this.productMapper.updateByPrimaryKey(product);
    }

    /**
     * 入库单（物资来源查询）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 18:38
     */
    @Override
    public List<Product> findProducts(Integer pageNum, Integer pageSize, Integer status) {
        Example example = new Example(Product.class);
        example.createCriteria().andEqualTo("status", status);
        List<Product> products = this.productMapper.selectByExample(example);
        return ListPageUtils.page(products, pageSize, pageNum);
    }

    /**
     * 物资库存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 16:20
     */
    @Override
    public List<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize) {
        // 查询所有商品
        List<Product> products = this.productMapper.selectAll();
        // 流转化需要返回的集合
        List<ProductStockVO> productStockVOList = products.stream().map(product -> {
            ProductStockVO productStockVO = new ProductStockVO();
            BeanUtils.copyProperties(product, productStockVO);
            // 构建查询条件
            Example example = new Example(ProductStock.class);
            // 根据商品编号查询数据
            example.createCriteria().andEqualTo("pNum", product.getPNum());
            // 遍历获取库存数据
            List<ProductStock> productStocks = this.productStockMapper.selectByExample(example);
            productStocks.forEach(productStock -> {
                productStockVO.setStock(productStock.getStock());
            });
            return productStockVO;
        }).collect(Collectors.toList());
        return ListPageUtils.page(productStockVOList, pageSize, pageNum);
    }
}
