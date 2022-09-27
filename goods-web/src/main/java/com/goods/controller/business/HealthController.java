package com.goods.controller.business;

import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.HealthVO;
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
 * @DATE: 2022/9/26 8:52
 */
@Api(tags = "业务模块-健康打卡相关接口")
@RestController
@RequestMapping("business/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    /**
     * 健康打卡提示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 8:56
     */
    //http://www.localhost:8989/business/health/isReport
    @GetMapping("isReport")
    public ResponseBean isReport() {
        // 调用服务层
        Health health = this.healthService.isReport();
        if (health != null) {
            return ResponseBean.success(health);
        }else {
            return ResponseBean.success(false);
        }
    }

    /**
     * 立即打卡
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 8:56
     */
    // http://www.localhost:8989/business/health/report
    @PostMapping("report")
    public ResponseBean report(@RequestBody Health health) {
        // 调用服务层
        this.healthService.report(health);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 签到记录
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 10:42
     */
    //http://www.localhost:8989/business/health/history?pageSize=4&pageNum=1
    @GetMapping("history")
    public ResponseBean<Map<String, Object>> history(Integer pageNum, Integer pageSize) {
        // 调用服务层
        List<Health> healthList = this.healthService.history(pageNum, pageSize);
        // 返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", healthList.size());
        map.put("rows", healthList);
        return ResponseBean.success(map);
    }

}
