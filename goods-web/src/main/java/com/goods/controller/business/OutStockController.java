package com.goods.controller.business;

import com.goods.business.service.OutStockService;
import com.goods.common.model.business.OutStock;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 19:09
 */
@Api(tags = "业务模块-物资去处相关接口")
@RestController
@RequestMapping("business/outStock")
public class OutStockController {

    @Autowired
    private OutStockService outStockService;

    /**
     * 出库单（发放记录）数据分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:13
     */
    //http://www.localhost:8989/business/outStock/findOutStockList?pageNum=1&pageSize=10&status=0
    @GetMapping("findOutStockList")
    public ResponseBean findOutStockList(Integer pageNum, Integer pageSize, OutStock outStock) {
        // 调用服务层
        List<OutStockVO> outStockVOS = this.outStockService.findOutStockList(pageNum, pageSize, outStock);
        HashMap<String, Object> map = new HashMap<>();
        if (outStockVOS == null) {
            map.put("total", 0);
            map.put("rows", null);
            return ResponseBean.success(null);
        } else {
            map.put("total", outStockVOS.size());
            map.put("rows", outStockVOS);
            return ResponseBean.success(map);
        }
    }

    /**
     * 添加出库单（发放物资）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:13
     */
    //http://www.localhost:8989/business/outStock/addOutStock
    @PostMapping("addOutStock")
    public ResponseBean addOutStock(@RequestBody OutStockVO outStockVO) {
        // 调用服务层
        this.outStockService.addOutStock(outStockVO);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 查询出库单（明细）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:13
     */
    // http://www.localhost:8989/business/outStock/detail/17?pageNum=1
    @GetMapping("detail/{id}")
    public ResponseBean<OutStockDetailVO> detail(@PathVariable Long id, Integer pageNum) {
        // 调用服务层
        OutStockDetailVO outStockDetailVO = this.outStockService.getOutStockDetail(id, pageNum);
        // 返回记录
        return ResponseBean.success(outStockDetailVO);
    }

    /**
     * 通过审核
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 13:15
     */
    //publish/127
    @PutMapping("publish/{id}")
    public ResponseBean publish(@PathVariable Long id) {
        // 调用服务层
        this.outStockService.publish(id);
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
    @PutMapping("back/{id}")
    public ResponseBean back(@PathVariable Long id) {
        // 调用服务层
        this.outStockService.backById(id);
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
    @PutMapping("remove/{id}")
    public ResponseBean remove(@PathVariable Long id) {
        // 调用服务层
        this.outStockService.removeById(id);
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
    @GetMapping("delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        // 调用服务层
        this.outStockService.deleteById(id);
        // 返回默认数据
        return ResponseBean.success();
    }
}
