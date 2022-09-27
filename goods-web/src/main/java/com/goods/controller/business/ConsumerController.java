package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 16:47
 */
@Api(tags = "业务模块-物资去处相关接口")
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    /**
     * 物资去处分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 16:57
     */
    //http://www.localhost:8989/business/consumer/findConsumerList?pageNum=1&pageSize=10&name=
    //http://www.localhost:8989/business/consumer/findConsumerList?pageNum=1&pageSize=10&name=aaa&address=jj&contact=aa
    @GetMapping("business/consumer/findConsumerList")
    public ResponseBean<HashMap<String, Object>> findConsumerList(Integer pageNum, Integer pageSize, Consumer consumer) {
        // 调用服务层
        List<Consumer> consumerVOList = this.consumerService.findConsumerList(pageNum, pageSize, consumer);
        // 返回数据
        HashMap<String, Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(consumerVOList)) {
            map.put("total", consumerVOList.size());
            map.put("rows", consumerVOList);
        } else {
            return ResponseBean.success(null);
        }
        return ResponseBean.success(map);
    }

    /**
     * 添加物资去处
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 16:57
     */
    //http://www.localhost:8989/consumer/add
    @PostMapping("consumer/add")
    public ResponseBean add(@RequestBody ConsumerVO consumerVO) {
        // 调用服务层
        this.consumerService.insertConsumer(consumerVO);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:16
     */
    //http://www.localhost:8989/business/consumer/edit/28
    @GetMapping("business/consumer/edit/{id}")
    public ResponseBean<ConsumerVO> edit(@PathVariable Long id) {
        // 调用服务层
        ConsumerVO consumerVO = this.consumerService.getConsumerById(id);
        // 返回数据
        return ResponseBean.success(consumerVO);
    }

    /**
     * 修改数据保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:22
     */
    //http://www.localhost:8989/business/consumer/update/28
    @PutMapping("business/consumer/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody ConsumerVO consumerVO) {
        // 调用服务层
        this.consumerService.updateConsumerById(id, consumerVO);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 删除物资去处
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:32
     */
    //http://www.localhost:8989/business/consumer/delete/28
    @GetMapping("business/consumer/delete/{id}")
    public ResponseBean deleteById(@PathVariable Long id) {
        // 调用服务层
        this.consumerService.deleteConsumerById(id);
        // 返回默认数据
        return ResponseBean.success();
    }

    /**
     * 物资出库查询已知去向
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 19:31
     */
    //http://www.localhost:8989/business/consumer/findAll
    @GetMapping("business/consumer/findAll")
    public ResponseBean<List<ConsumerVO>> findAll() {
        // 调用服务层
        List<ConsumerVO> consumerVOList = this.consumerService.findAll();
        // 返回数据
        return ResponseBean.success(consumerVOList);
    }
}
