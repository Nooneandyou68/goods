package com.goods.controller.business;

import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 20:16
 */
@Api(tags = "业务模块-物资来源相关接口")
@RestController
@RequestMapping("business/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 获取物资列表
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    //http://www.localhost:8989/business/supplier/findSupplierList?pageNum=1&pageSize=10&name=
    @GetMapping("findSupplierList")
    public ResponseBean<Map<String, Object>> findSupplierList(Integer pageNum, Integer pageSize, Supplier supplier) {
        // 调用服务层
        List<Supplier> list = this.supplierService.findSupplierList(pageNum, pageSize, supplier);
        Map<String, Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            // 封装到map返回
            map.put("total", list.size());
            map.put("rows", list);
            // 返回数据
        } else {
            ResponseBean.success(null);
        }
        return ResponseBean.success(map);
    }

    /**
     * 添加来源
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    //http://www.localhost:8989/business/supplier/add
    @PostMapping("add")
    public ResponseBean add(@RequestBody Supplier supplier) {
        // 调用服务层
        this.supplierService.saveSupplier(null, supplier);
        // 返回数据
        return ResponseBean.success();
    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    //: http://www.localhost:8989/business/supplier/edit/25
    @GetMapping("edit/{id}")
    public ResponseBean<Supplier> edit(@PathVariable long id) {
        // 调用服务层方法
        Supplier supplier = this.supplierService.getSupplierById(id);
        // 返回数据
        return ResponseBean.success(supplier);
    }

    /**
     * 修改数据保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    //supplier/update/25
    @PutMapping("update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody Supplier supplier) {
        // 调用服务层
        this.supplierService.saveSupplier(id, supplier);
        // 返回数据
        return ResponseBean.success();
    }

    /**
     * 删除数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    // http://www.localhost:8989/business/supplier/delete/26
    @DeleteMapping("delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        // 调用服务层
        this.supplierService.deleteSupplier(id);
        // 返回数据
        return ResponseBean.success();
    }

    /**
     * 入库（进行物资来源选择）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    //http://www.localhost:8989/business/supplier/findAll
    @GetMapping("findAll")
    public ResponseBean findAll() {
        //调用服务层
        List<Supplier> supplierList = this.supplierService.findAll();
        // 返回数据
        return ResponseBean.success(supplierList);
    }
}
