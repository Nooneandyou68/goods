package com.goods.business.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.goods.business.mapper.*;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.*;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.SupplierVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/24 14:23
 */
@Service
public class InStockServiceImpl implements InStockService {

    @Autowired
    private InStockMapper instockMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InStockInfoMapper inStockInfoMapper;
    @Autowired
    private ProductStockMapper productStockMapper;

    private static List<Integer> kcList = null;

    /**
     * 入库单（入库记录）数据分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 13:55
     */
    @Override
    public List<InStockVO> getInStockList(Integer pageNum, Integer pageSize, InStock inStock) {
        // 构建条件
        Example example = new Example(InStock.class);
        if (inStock.getInNum() != null && !"".equals(inStock.getInNum())) {
            example.createCriteria().andEqualTo("inNum", inStock.getInNum());
            example.createCriteria().andEqualTo("status", inStock.getStatus());
        }
        if (inStock.getType() != null && inStock.getType() != 0) {
            example.createCriteria().andEqualTo("type", inStock.getType());
            example.createCriteria().andEqualTo("status", inStock.getStatus());
        }
        // 如果都不等于空
        if (inStock.getInNum() != null && !"".equals(inStock.getInNum())
                && inStock.getType() != null && inStock.getType() != 0) {
            // 根据类型查询
            example.createCriteria().andEqualTo("type", inStock.getType());
            // 根据单号查询
            example.createCriteria().andEqualTo("inNum", inStock.getInNum());
            // 根据状态查询
            example.createCriteria().andEqualTo("status", inStock.getStatus());
        } else {
            List<InStock> inStocks = this.instockMapper.selectByExample(example);
            // 遍历赋值
            List<InStockVO> collect = inStocks.stream().map(inStock1 -> {
                InStockVO inStockVO = new InStockVO();
                BeanUtils.copyProperties(inStock1, inStockVO);
                List<Supplier> supplierList = this.supplierMapper.selectAll();
                supplierList.forEach(supplier -> {
                    inStockVO.setPhone(supplier.getPhone());
                    inStockVO.setSupplierName(supplier.getName());
                    inStockVO.setAddress(supplier.getAddress());
                });
                return inStockVO;
            }).collect(Collectors.toList());
            if (ListPageUtils.page(collect, pageSize, pageNum) == null) {
                return null;
            } else {
                return ListPageUtils.page(collect, pageSize, pageNum);
            }
        }
        return null;
    }

    /**
     * 入库单（入库明细）数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 15:46
     */
    @Override
    public InStockDetailVO getInStockDetail(Long id, Integer pageNum) {
        // 初始化vo对象 存储数据
        InStockDetailVO inStockDetailVO = new InStockDetailVO();
        // 根据id获取inStock数据
        InStock inStock = this.instockMapper.selectByPrimaryKey(id);
        // 属性赋值到inStockDetailVO
        BeanUtils.copyProperties(inStock, inStockDetailVO);
        // 构建查询条件
        Example exampleInfo = new Example(InStockInfo.class);
        // 根据inNum查询InStockInfo数据
        exampleInfo.createCriteria().andEqualTo("inNum", inStock.getInNum());
        List<InStockInfo> inStockInfoList = this.inStockInfoMapper.selectByExample(exampleInfo);
        inStockInfoList.forEach(inStockInfo ->{
            // 根据inStockInfo的pNum查询Product
            Example examplePro = new Example(Product.class);
            examplePro.createCriteria().andEqualTo("pNum", inStockInfo.getPNum());
            List<Product> productList = this.productMapper.selectByExample(examplePro);
            // 查询Product数据赋值到InStockItemVO
            List<InStockItemVO> inStockItemVOS = productList.stream().map(product -> {
                Example example = new Example(ProductStock.class);
                example.createCriteria().andEqualTo("pNum", product.getPNum());
                ProductStock productStock = this.productStockMapper.selectOneByExample(example);
                InStockItemVO inStockItemVO = new InStockItemVO();
                inStockItemVO.setCount(productStock.getStock().intValue());
                BeanUtils.copyProperties(product, inStockItemVO);
                return inStockItemVO;
            }).collect(Collectors.toList());
            // 放入product数据
            inStockDetailVO.setItemVOS(inStockItemVOS);
            // 赋值总记录数
            inStockDetailVO.setTotal(inStockItemVOS.size());
        });
        // 构建查询条件
        Example example = new Example(Supplier.class);
        // 根据inStock的SupplierId查询Supplier数据
        example.createCriteria().andEqualTo("id", inStock.getSupplierId());
        List<Supplier> supplierList = this.supplierMapper.selectByExample(example);
        // 循环遍历赋值SupplierVO属性
        supplierList.stream().forEach(supplier -> {
            // supplier数据赋值到SupplierVO
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            inStockDetailVO.setSupplierVO(supplierVO);
        });
        // 返回数据
        return inStockDetailVO;
    }

    /**
     * 入库单（保存）数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 18:48
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addIntoStock(InStockVO inStockVO) {
        // 获取用户名
        ActiveUser principal = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        // inStock
        InStock inStock = new InStock();
        BeanUtils.copyProperties(inStockVO, inStock);
        inStock.setOperator(principal.getUser().getUsername());
        inStock.setStatus(2);
        inStock.setInNum(UUID.randomUUID().toString().replace("-", ""));
        inStock.setCreateTime(new Date());
        inStock.setModified(new Date());
        // inStock
        InStockInfo inStockInfo = new InStockInfo();
        inStockInfo.setInNum(inStock.getInNum());
        inStockInfo.setCreateTime(new Date());
        inStockInfo.setModifiedTime(new Date());
        // 获取前端传输的productId，productNumber
        List<Object> products = inStockVO.getProducts();
        this.kcList= new ArrayList<>();
        for (Object product1 : products) {
            // 循环转换格式
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(product1));
            Integer productId = ((Integer) jsonObject.get("productId"));
            Integer number = (Integer) jsonObject.get("productNumber");
            kcList.add(number);
            // 根据id查询数据
            Example example = new Example(Product.class);
            example.createCriteria().andEqualTo("id", productId);
            Product product = this.productMapper.selectOneByExample(example);
            // 根据库存号查找库存
            Example example1 = new Example(ProductStock.class);
            example1.createCriteria().andEqualTo("pNum", product.getPNum());
            ProductStock productStock = productStockMapper.selectOneByExample(example1);
            // 赋值
            inStockInfo.setPNum(productStock.getPNum());
            productStock.setStock(productStock.getStock() + number);
            this.productStockMapper.updateByPrimaryKey(productStock);
            inStockInfo.setProductNumber(productStock.getStock().intValue());
            this.inStockInfoMapper.insert(inStockInfo);

        }
        // 根据info表入库编号查询数据
        Example example2 = new Example(InStockInfo.class);
        example2.createCriteria().andEqualTo("inNum", inStockInfo.getInNum());
        List<InStockInfo> infoList = this.inStockInfoMapper.selectByExample(example2);
        // 循环修改数据
        Integer count = 0;
        for (InStockInfo stockInfo : infoList) {
            count += stockInfo.getProductNumber();
            inStock.setProductNumber(count);
        }
        // 如果supplierId为空 新建一条记录
        if (inStock.getSupplierId() == null) {
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(inStockVO,supplier);
            supplier.setCreateTime(new Date());
            supplier.setModifiedTime(new Date());
            this.supplierMapper.insert(supplier);
            inStock.setSupplierId(supplier.getId());
        }
        // 插入数据
        this.instockMapper.insert(inStock);

    }

    /**
     * 放入回收站
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 19:40
     */
    @Override
    public void removeById(Long id) {
        // 根据id查询数据
        InStock inStock = this.instockMapper.selectByPrimaryKey(id);
        inStock.setStatus(1);
        inStock.setModified(new Date());
        // 根据查询出的数据修改
        this.instockMapper.updateByPrimaryKey(inStock);
    }

    /**
     * 回收站还原
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 12:52
     */
    @Override
    public void backById(Long id) {
        // 根据id查询数据
        InStock inStock = this.instockMapper.selectByPrimaryKey(id);
        // 设置状态正常入库
        inStock.setStatus(0);
        // 修改状态
        this.instockMapper.updateByPrimaryKey(inStock);
    }

    /**
     * 删除
     *@author SongBoHao
     *@date 2022/9/25 13:00
     *@param
     *@return
     */
    @Override
    public void deleteById(Long id) {
        InStock inStock = this.instockMapper.selectByPrimaryKey(id);
        // 根据id删除数据
        Example example = new Example(InStockInfo.class);
        example.createCriteria().andEqualTo("inNum", inStock.getInNum());
        // 减少库存
        List<InStockInfo> inStockInfoList = this.inStockInfoMapper.selectByExample(example);
        for (InStockInfo inStockInfo : inStockInfoList) {
            Example example1 = new Example(ProductStock.class);
            example1.createCriteria().andEqualTo("pNum", inStockInfo.getPNum());
            List<ProductStock> productStocks = this.productStockMapper.selectByExample(example1);
            for (ProductStock productStock : productStocks) {
                for (Integer kc : this.kcList) {
                    productStock.setStock(productStock.getStock() - kc.longValue());
                    System.out.println("kc = " + kc);
                    this.productStockMapper.updateByPrimaryKey(productStock);
                }
            }
        }
        this.instockMapper.deleteByPrimaryKey(id);
        this.inStockInfoMapper.deleteByExample(example);
    }

    /**
     * 通过审核
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 13:17
     */
    @Override
    public void publish(Long id) {
        // 根据id查询数据
        InStock inStock = this.instockMapper.selectByPrimaryKey(id);
        // 设置状态正常入库
        inStock.setStatus(0);
        // 修改数据
        this.instockMapper.updateByPrimaryKey(inStock);
    }
}
