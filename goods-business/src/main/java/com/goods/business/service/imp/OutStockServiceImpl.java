package com.goods.business.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.goods.business.mapper.*;
import com.goods.business.service.OutStockService;
import com.goods.common.model.business.*;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 19:14
 */
@Service
public class OutStockServiceImpl implements OutStockService {
    @Autowired
    private OutStockMapper outStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OutStockInfoMapper outStockInfoMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private ConsumerMapper consumerMapper;

    private List<Integer> kcList = null;
    /**
     * 出库单（发放记录）数据分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:15
     */
    @Override
    public List<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStock outStock) {
        // 构建条件
        Example example = new Example(OutStock.class);
        if (outStock.getOutNum() != null && !"".equals(outStock.getOutNum())) {
            example.createCriteria().andEqualTo("outNum", outStock.getOutNum());
            example.createCriteria().andEqualTo("status", outStock.getOutNum());
        }
        if (outStock.getType() != null && outStock.getType() != 0) {
            example.createCriteria().andEqualTo("type", outStock.getType());
            example.createCriteria().andEqualTo("status", outStock.getType());
        }
        // 如果都不等于空
        if (outStock.getOutNum() != null && !"".equals(outStock.getOutNum())
                && outStock.getType() != null && outStock.getType() != 0) {
            // 根据类型查询
            example.createCriteria().andEqualTo("type", outStock.getType());
            // 根据单号查询
            example.createCriteria().andEqualTo("outNum", outStock.getOutNum());
            // 根据状态查询
            example.createCriteria().andEqualTo("status", outStock.getStatus());
        } else {
            List<OutStock> inStocks = this.outStockMapper.selectByExample(example);
            // 遍历赋值
            List<OutStockVO> collect = inStocks.stream().map(inStock1 -> {
                OutStockVO outStockVO = new OutStockVO();
                BeanUtils.copyProperties(inStock1, outStockVO);
                List<Consumer> consumerList = this.consumerMapper.selectAll();
                consumerList.forEach(supplier -> {
                    outStockVO.setPhone(supplier.getPhone());
                    outStockVO.setName(supplier.getName());
                    outStockVO.setAddress(supplier.getAddress());
                });
                return outStockVO;
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
     * 添加出库单（发放物资）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:42
     */
    @Override
    public void addOutStock(OutStockVO outStockVO) {
        // 获取用户名
        ActiveUser principal = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        // outStock
        OutStock outStock = new OutStock();
        BeanUtils.copyProperties(outStockVO, outStock);
        outStock.setOperator(principal.getUser().getUsername());
        outStock.setStatus(2);
        outStock.setOutNum(UUID.randomUUID().toString().replace("-", ""));
        outStock.setCreateTime(new Date());
        // inStock
        OutStockInfo outStockInfo = new OutStockInfo();
        outStockInfo.setOutNum(outStock.getOutNum());
        outStockInfo.setCreateTime(new Date());
        // 获取前端传输的productId，productNumber
        List<Object> products = outStockVO.getProducts();
        this.kcList= new ArrayList<>();
        for (Object product : products) {
            // 循环转换格式
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(product));
            Integer productId = ((Integer) jsonObject.get("productId"));
            Integer number = (Integer) jsonObject.get("productNumber");
            kcList.add(number);
            // 根据id查询数据
            Example example = new Example(Product.class);
            example.createCriteria().andEqualTo("id", productId);
            Product product1 = this.productMapper.selectOneByExample(example);
            // 根据库存号查找库存
            Example example1 = new Example(ProductStock.class);
            example1.createCriteria().andEqualTo("pNum", product1.getPNum());
            ProductStock productStock = productStockMapper.selectOneByExample(example1);
            // 赋值
            outStockInfo.setPNum(productStock.getPNum());
            productStock.setStock(productStock.getStock() - number);
            this.productStockMapper.updateByPrimaryKey(productStock);
            outStockInfo.setProductNumber(productStock.getStock().intValue());
            this.outStockInfoMapper.insert(outStockInfo);

        }
        // 根据info表入库编号查询数据
        Example example2 = new Example(OutStockInfo.class);
        example2.createCriteria().andEqualTo("outNum", outStockInfo.getOutNum());
        List<OutStockInfo> outStockInfoList = this.outStockInfoMapper.selectByExample(example2);
        // 循环修改数据

        for (OutStockInfo outStockInfo1 : outStockInfoList) {
            outStockInfo.setProductNumber(outStockInfo1.getProductNumber());
        }
        if (outStock.getConsumerId() == null) {
            new Consumer();
            Consumer consumer = new Consumer();
            BeanUtils.copyProperties(outStockVO,consumer);
            consumer.setCreateTime(new Date());
            consumer.setModifiedTime(new Date());
            this.consumerMapper.insert(consumer);
            outStock.setConsumerId(consumer.getId());
        }
        // 插入数据
        this.outStockMapper.insert(outStock);
    }

    /**
     * 查询出库单（明细）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 21:18
     */
    @Override
    public OutStockDetailVO getOutStockDetail(Long id, Integer pageNum) {
        // 初始化vo对象 存储数据
        OutStockDetailVO outStockDetailVO = new OutStockDetailVO();
        // 根据id获取inStock数据
        OutStock outStock = this.outStockMapper.selectByPrimaryKey(id);
        // 属性赋值到inStockDetailVO
        BeanUtils.copyProperties(outStock, outStockDetailVO);
        // 构建查询条件
        Example exampleInfo = new Example(OutStockInfo.class);
        // 根据inNum查询InStockInfo数据
        exampleInfo.createCriteria().andEqualTo("outNum", outStock.getOutNum());
        List<OutStockInfo> outStockInfoList = this.outStockInfoMapper.selectByExample(exampleInfo);
        outStockInfoList.forEach(outStockInfo ->{
            // 根据inStockInfo的pNum查询Product
            Example examplePro = new Example(Product.class);
            examplePro.createCriteria().andEqualTo("pNum", outStockInfo.getPNum());
            List<Product> productList = this.productMapper.selectByExample(examplePro);
            // 查询Product数据赋值到InStockItemVO
            List<OutStockItemVO> inStockItemVOS = productList.stream().map(product -> {
                Example example = new Example(ProductStock.class);
                example.createCriteria().andEqualTo("pNum", product.getPNum());
                ProductStock productStock = this.productStockMapper.selectOneByExample(example);
                OutStockItemVO outStockItemVO = new OutStockItemVO();
                outStockItemVO.setCount(productStock.getStock().intValue());
                BeanUtils.copyProperties(product, outStockItemVO);
                return outStockItemVO;
            }).collect(Collectors.toList());
            // 放入product数据
            outStockDetailVO.setItemVOS(inStockItemVOS);
            // 赋值总记录数
            outStockDetailVO.setTotal(inStockItemVOS.size());
        });
        // 构建查询条件
        Example example = new Example(Consumer.class);
        // 根据inStock的SupplierId查询Supplier数据
        example.createCriteria().andEqualTo("id", outStock.getConsumerId());
        List<Consumer> consumerList = this.consumerMapper.selectByExample(example);
        // 循环遍历赋值SupplierVO属性
        consumerList.stream().forEach(consumer -> {
            // supplier数据赋值到SupplierVO
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            outStockDetailVO.setConsumerVO(consumerVO);
        });
        // 返回数据
        return outStockDetailVO;
    }

    /**
     * 通过审核
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 13:15
     */
    @Override
    public void publish(Long id) {
        // 根据id查询数据
        OutStock outStock = this.outStockMapper.selectByPrimaryKey(id);
        // 设置状态正常入库
        outStock.setStatus(0);
        // 修改数据
        this.outStockMapper.updateByPrimaryKey(outStock);
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
        OutStock outStock = this.outStockMapper.selectByPrimaryKey(id);
        // 根据id删除数据
        Example example = new Example(OutStockInfo.class);
        example.createCriteria().andEqualTo("outNum", outStock.getOutNum());
        // 减少库存
        List<OutStockInfo> outStockInfoList = this.outStockInfoMapper.selectByExample(example);
        for (OutStockInfo outStockInfo : outStockInfoList) {
            Example example1 = new Example(ProductStock.class);
            example1.createCriteria().andEqualTo("pNum", outStockInfo.getPNum());
            List<ProductStock> productStocks = this.productStockMapper.selectByExample(example1);
            for (ProductStock productStock : productStocks) {
                for (Integer kc : this.kcList) {
                    productStock.setStock(productStock.getStock() + kc.longValue());
                    System.out.println("kc = " + kc);
                    this.productStockMapper.updateByPrimaryKey(productStock);
                }
            }
        }
        this.outStockMapper.deleteByPrimaryKey(id);
        this.outStockInfoMapper.deleteByExample(example);
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
        OutStock outStock = this.outStockMapper.selectByPrimaryKey(id);
        // 设置状态正常入库
        outStock.setStatus(0);
        // 修改状态
        this.outStockMapper.updateByPrimaryKey(outStock);
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
        OutStock outStock = this.outStockMapper.selectByPrimaryKey(id);
        outStock.setStatus(1);
        // 根据查询出的数据修改
        this.outStockMapper.updateByPrimaryKey(outStock);
    }
}
