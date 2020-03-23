package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    /**
     * 保存
     * 参数：extCproduct （附件）
     * 包含 contractId 合同
     * 包含 contractProductId 所属货物id
     */
    public void save(ExtCproduct extCproduct) {
        //1、计算保存附件的总金额
        double money = 0.0;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null) {
            money = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        extCproduct.setAmount(money);
        //设置附件的id
        extCproduct.setId(UUID.randomUUID().toString());
        //保存附件
        extCproductDao.insertSelective(extCproduct);
        //根据id查询购销合同，修改购销合同的总金额
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() + money);
        //合同附件加以
        contract.setExtNum(contract.getExtNum() + 1);
        //7.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }


    /**
     * 更新
     *
     * @param extCproduct
     */
    public void update(ExtCproduct extCproduct) {
        //计算更新后的总金额
        double money = 0.0;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null) {
            money = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        extCproduct.setAmount(money);
        //2.查询更新之前附件金额
        ExtCproduct oldExt = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldMoney = oldExt.getAmount();
        //更新
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        //4.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //5.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + money);
        //6.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除附件
     *  参数：附件ID
     */
    public void delete(String id) {
        //1.根据id查询附件（获取删除的金额）
        ExtCproduct ext = extCproductDao.selectByPrimaryKey(id);
        Double money = ext.getAmount();
        //2.删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //3.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(ext.getContractId());
        //4.设置购销合同总金额
        contract.setTotalAmount(contract.getTotalAmount() - money);
        contract.setExtNum(contract.getExtNum() - 1);
        //5.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }


    /**
     * 分页查询
     *
     * @param example
     * @param page
     * @param size
     * @return
     */
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo(list);
    }
}