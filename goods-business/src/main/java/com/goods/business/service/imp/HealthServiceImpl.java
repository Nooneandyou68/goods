package com.goods.business.service.imp;

import com.goods.business.mapper.HealthMapper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.HealthVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/26 8:58
 */
@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private HealthMapper healthMapper;

    /**
     * 健康打卡提示
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 8:56
     */
    @Override
    public Health isReport() {
        Example example = new Example(Health.class);
        Example.Criteria criteria = example.createCriteria();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        System.out.println("startTime" + calendar.getTime());
        Date startTime =calendar.getTime();
        System.out.println(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, -1);
        System.out.println("stopTime" + calendar.getTime());
        Date stopTime = calendar.getTime();
        criteria.andBetween("createTime", startTime, stopTime);
        List<Health> healthList = this.healthMapper.selectByExample(example);
        if (healthList.size() > 0) {
            return healthList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 立即打卡
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 9:16
     */
    @Override
    public void report(Health health) {
        // 获取id
        ActiveUser principal = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        health.setCreateTime(new Date());
        // 获取用户id

        Long id = principal.getUser().getId();
        System.out.println("aLong = " + id);
        health.setUserId(id);
        this.healthMapper.insert(health);
    }

    /**
     * 签到记录
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/26 10:43
     */
    @Override
    public List<Health> history(Integer pageNum, Integer pageSize) {
        List<Health> healthList = this.healthMapper.selectAll();
        return ListPageUtils.page(healthList, pageSize, pageNum);
    }
}
