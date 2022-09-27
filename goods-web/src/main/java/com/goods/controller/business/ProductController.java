package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.common.model.business.Product;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 23:26
 */
@Api(tags = "业务模块-物资资料相关接口")
@RestController
@RequestMapping("business/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 物资资料分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    //http://www.localhost:8989/business/product/findProductList?pageNum=1&pageSize=6&name=&categoryId=&supplier=&status=0
    //name=44&categoryId=&supplier=&status=0&categorys=24,30,62
    @GetMapping("findProductList")
    public ResponseBean<HashMap<String, Object>> getProduct(Integer pageNum, Integer pageSize,
                                                            ProductVO productVO, String categorys) {
        // 调用服务层
        List<Product> productList = this.productService.findProductList(pageNum, pageSize, productVO, categorys);
        HashMap<String, Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(productList)) {
            map.put("total", productList.size());
            map.put("rows", productList);
        } else {
            return ResponseBean.success(null);
        }
        // 返回数据
        return ResponseBean.success(map);
    }

    /**
     * 物资资料保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    @PostMapping("add")
    public ResponseBean add(@RequestBody ProductVO productVO) {
        // 调用服务层
        this.productService.saveProduct(productVO);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 物资资料放入回收站
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    //http://www.localhost:8989/business/product/remove/56
    @PutMapping("remove/{id}")
    public ResponseBean remove(@PathVariable Long id) {
        // 调用服务层
        this.productService.removeProduct(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 物资资料删除
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    // http://www.localhost:8989/business/product/delete/59
    @DeleteMapping("delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        // 调用服务层
        this.productService.deleteProduct(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 物资资料修改回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    //http://www.localhost:8989/business/product/edit/60
    @GetMapping("edit/{id}")
    public ResponseBean<Product> edit(@PathVariable Long id) {
        // 调用服务层
        Product product = this.productService.getProductById(id);
        // 返回默认数据
        return ResponseBean.success(product);
    }

    /**
     * 物资资料修改保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 23:30
     */
    //http://www.localhost:8989/business/product/update/61
    @PutMapping("update/{id}")
    public ResponseBean update(@RequestBody ProductVO productVO, @PathVariable Long id) {
        // 调用服务层
        this.productService.updateProduct(productVO, id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 入库单（物资来源查询）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 13:55
     */
    //http://www.localhost:8989/business/product/findProducts?pageNum=1&pageSize=6&status=0
    @GetMapping("findProducts")
    public ResponseBean<HashMap<String, Object>> findProducts(Integer pageNum, Integer pageSize, Integer status) {
        // 调用服务层
        List<Product> productList = this.productService.findProducts(pageNum, pageSize, status);
        HashMap<String, Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(productList)) {
            map.put("total", productList.size());
            map.put("rows", productList);
        } else {
            return ResponseBean.success(null);
        }
        // 返回数据
        return ResponseBean.success(map);
    }

    /**
     * 饼图
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 15:53
     */
    //http://www.localhost:8989/business/product/findAllStocks?pageSize=9&pageNum=1
    @GetMapping("findAllStocks")
    public ResponseBean<List<ProductStockVO>> findAllStocks(Integer pageNum, Integer pageSize) {
        // 调用服务层
        List<ProductStockVO> productStockList = this.productService.findProductStocks(pageNum, pageSize);
        // 返回数据
        return ResponseBean.success(productStockList);
    }
    /**
     * 物资库存
     *@author SongBoHao
     *@date 2022/9/25 16:20
     *@param
     *@return
     */
    // http://www.localhost:8989/business/product/findProductStocks?pageSize=9&pageNum=1
    @GetMapping("findProductStocks")
    public ResponseBean<HashMap<String, Object>> findProductStocks(Integer pageNum, Integer pageSize) {
        // 调用服务层
        List<ProductStockVO> products = this.productService.findProductStocks(pageNum, pageSize);
        // 返回数据
        HashMap<String, Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(products)) {
            map.put("total", products.size());
            map.put("rows", products);
        } else {
            return ResponseBean.success(null);
        }
        return ResponseBean.success(map);
    }
}
