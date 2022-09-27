package com.goods.controller.business;

import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.InStock;
import com.goods.common.model.business.InStockInfo;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/24 13:53
 */
@Api(tags = "业务模块-入库记录相关接口")
@RestController
@RequestMapping("business/inStock")
public class InStockController {

    @Autowired
    private InStockService inStockService;

    /**
     * 入库单（入库记录）数据分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 13:55
     */
    //http://www.localhost:8989/business/inStock/findInStockList?pageNum=1&pageSize=10&status=0
    @GetMapping("findInStockList")
    public ResponseBean<Map<String, Object>> getInStockList(Integer pageNum, Integer pageSize, InStock inStock) {
        // 调用服务层
        List<InStockVO> inStockVOList = this.inStockService.getInStockList(pageNum, pageSize, inStock);
        // 返回数据
        HashMap<String, Object> map = new HashMap<>();
        if (inStockVOList == null) {
            map.put("total", 0);
            map.put("rows", null);
            return ResponseBean.success(null);
        } else {
            map.put("total", inStockVOList.size());
            map.put("rows", inStockVOList);
            return ResponseBean.success(map);
        }
    }

    /**
     * 入库单（入库明细）数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 13:55
     */
    // http://www.localhost:8989/business/inStock/detail/117?pageNum=1
    @GetMapping("detail/{id}")
    public ResponseBean<InStockDetailVO> detail(@PathVariable Long id, Integer pageNum) {
        // 调用服务层
        InStockDetailVO inStockVO = this.inStockService.getInStockDetail(id, pageNum);
        // 返回数据
        return ResponseBean.success(inStockVO);
    }

    /**
     * 入库单（保存）数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 18:48
     */
    //http://www.localhost:8989/business/inStock/addIntoStock
    @PostMapping("addIntoStock")
    public ResponseBean addIntoStock(@RequestBody InStockVO inStockVO) {
        // 调用服务层
        this.inStockService.addIntoStock(inStockVO);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 放入回收站
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 19:40
     */
    //http://www.localhost:8989/business/inStock/remove/117
    @PutMapping("remove/{id}")
    public ResponseBean remove(@PathVariable Long id) {
        // 调用服务层
        this.inStockService.removeById(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 回收站还原
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 12:52
     */
    //http://www.localhost:8989/business/inStock/back/123
    @PutMapping("back/{id}")
    public ResponseBean back(@PathVariable Long id) {
        // 调用服务层
        this.inStockService.backById(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 删除
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 12:59
     */
    // http://www.localhost:8989/business/inStock/delete/123
    @GetMapping("delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        // 调用服务层
        this.inStockService.deleteById(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 通过审核
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 13:15
     */
    //ttp://www.localhost:8989/business/inStock/publish/127
    @PutMapping("publish/{id}")
    public ResponseBean publish(@PathVariable Long id) {
        // 调用服务层
        this.inStockService.publish(id);
        // 返回默认数据
        return ResponseBean.success();
    }
}
