package com.goods.business.service.imp;

import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @PROJECT_NAME: goods
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/9/23 20:25
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<Supplier> findSupplierList(Integer pageNum, Integer pageSize, Supplier supplier) {
        List<Supplier> list = this.supplierMapper.selectAll();
        List<Supplier> supplierList = ListPageUtils.page(list, pageSize, pageNum);
        if (supplier != null) {
            Example example = new Example(Supplier.class);
            Example.Criteria criteria = example.createCriteria();
            if (supplier.getName() != null && !"".equals(supplier.getName())) {
                criteria.andLike("name", "%" + supplier.getName() + "%");
            } else if (supplier.getAddress() != null && !"".equals(supplier.getAddress())) {
                criteria.andLike("address", "%" + supplier.getAddress() + "%");
            } else if (supplier.getContact() != null) {
                criteria.andLike("contact", "%" + supplier.getContact() + "%");
            } else if (supplier.getName() != null && supplier.getContact() != null && supplier.getAddress() != null) {
                criteria.andLike("name", "%" + supplier.getName() + "%");
                criteria.andLike("contact", "%" + supplier.getContact() + "%");
                criteria.andLike("address", "%" + supplier.getAddress() + "%");
            }
            List<Supplier> selectByExample = this.supplierMapper.selectByExample(example);
            if (selectByExample != null) {
                return ListPageUtils.page(selectByExample, pageSize, pageNum);
            } else {
                return null;
            }
        }
        return supplierList;
    }

    /**
     * 添加来源
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 22:37
     */
    @Override
    public void saveSupplier(Long id, Supplier supplier) {
        if (id == null) {
            supplier.setCreateTime(new Date());
            supplier.setModifiedTime(new Date());
            this.supplierMapper.insert(supplier);
        } else {
            Example example = new Example(Supplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", id);
            this.supplierMapper.updateByExample(supplier, example);
        }

    }

    /**
     * 修改数据回显
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    @Override
    public Supplier getSupplierById(long id) {
        return this.supplierMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除数据
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/23 20:17
     */
    @Override
    public void deleteSupplier(Long id) {
        this.supplierMapper.deleteByPrimaryKey(id);
    }

    /**
     * 入库（进行物资来源选择）
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/9/24 18:46
     */
    @Override
    public List<Supplier> findAll() {
        return this.supplierMapper.selectAll();
    }
}
