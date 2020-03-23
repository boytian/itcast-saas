package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 购销合同的dubbo服务
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }


    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        //默认属性
        contract.setState(0); // 0:草稿
        contract.setTotalAmount(0.0);// 总金额
        contract.setCreateTime(new Date()); // 保存的时间
        contractDao.insertSelective(contract);
    }

    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    public PageInfo findAll(ContractExample example, int page, int size) {
        //分页
        PageHelper.startPage(page,size);
        //查询列表
        List list = contractDao.selectByExample(example);
        //返回PageInfo
        return new PageInfo(list);
    }

    public void submit(String id) {
        Contract contract = contractDao.selectByPrimaryKey(id);
        contract.setState(1);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    public void cancel(String id) {
        Contract contract = contractDao.selectByPrimaryKey(id);
        contract.setState(0);
        contractDao.updateByPrimaryKeySelective(contract);
    }

}