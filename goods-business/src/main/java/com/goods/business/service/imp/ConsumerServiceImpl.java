package com.goods.business.service.imp;

import com.goods.business.mapper.ConsumerMapper;
import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.model.business.Supplier;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ConsumerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/25 16:50
 */
@Service
public class ConsumerServiceImpl implements ConsumerService{
    @Autowired
    private ConsumerMapper consumerMapper;

    /**
     * 物资去处分页列表展示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 16:57
     */
    @Override
    public List<Consumer> findConsumerList(Integer pageNum, Integer pageSize, Consumer consumer) {

        List<Consumer> list = this.consumerMapper.selectAll();
        List<Consumer> consumerList = ListPageUtils.page(list, pageSize, pageNum);
        if (consumer != null) {
            Example example = new Example(Supplier.class);
            Example.Criteria criteria = example.createCriteria();
            if (consumer.getName() != null && !"".equals(consumer.getName())) {
                criteria.andLike("name", "%" + consumer.getName() + "%");
            } else if (consumer.getAddress() != null && !"".equals(consumer.getAddress())) {
                criteria.andLike("address", "%" + consumer.getAddress() + "%");
            } else if (consumer.getContact() != null) {
                criteria.andLike("contact", "%" + consumer.getContact() + "%");
            } else if (consumer.getName() != null && consumer.getContact() != null && consumer.getAddress() != null) {
                criteria.andLike("name", "%" + consumer.getName() + "%");
                criteria.andLike("contact", "%" + consumer.getContact() + "%");
                criteria.andLike("address", "%" + consumer.getAddress() + "%");
            }
            List<Consumer> selectByExample = this.consumerMapper.selectByExample(example);
            if (selectByExample != null) {
                return ListPageUtils.page(selectByExample, pageSize, pageNum);
            } else {
                return null;
            }
        }
        return consumerList;
    }
    // 方法封装
    private static List<ConsumerVO> getConsumerVOList(Integer pageNum, Integer pageSize, List<Consumer> consumerList) {
        List<ConsumerVO> consumerVOList = consumerList.stream().map(consumer1 -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer1, consumerVO);
            consumerVO.setCreateTime(new Date());
            consumerVO.setModifiedTime(new Date());
            return consumerVO;
        }).collect(Collectors.toList());
        // 分页后返回数据
        return ListPageUtils.page(consumerVOList, pageSize, pageNum);
    }

    /**
     * 添加物资去处
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 16:57
     */
    @Override
    public void insertConsumer(ConsumerVO consumerVO) {
        // 创建要添加的对象
        Consumer consumer = new Consumer();
        // 属性赋值
        BeanUtils.copyProperties(consumerVO, consumer);
        // 放入时间
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        // 插入数据
        this.consumerMapper.insert(consumer);
    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:19
     */
    @Override
    public ConsumerVO getConsumerById(Long id) {
        Consumer consumer = this.consumerMapper.selectByPrimaryKey(id);
        ConsumerVO consumerVO = new ConsumerVO();
        BeanUtils.copyProperties(consumer, consumerVO);
        return consumerVO;
    }

    /**
     * 修改数据保存
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:21
     */
    @Override
    public void updateConsumerById(Long id, ConsumerVO consumerVO) {
        Consumer consumer = this.consumerMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(consumerVO, consumer);
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        this.consumerMapper.updateByPrimaryKey(consumer);
    }

    /**
     * 删除物资去处
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/25 17:33
     */
    @Override
    public void deleteConsumerById(Long id) {
        this.consumerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<ConsumerVO> findAll() {
        return this.consumerMapper.selectAll().stream().map(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            return consumerVO;
        }).collect(Collectors.toList());
    }
}
