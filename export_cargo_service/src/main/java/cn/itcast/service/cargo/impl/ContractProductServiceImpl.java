package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * ContractProduct ：购销合同货物
 */
@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    //根据id查询
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    //分页查询
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo(list);
    }

    //批量保存
    public void saveAll(List<ContractProduct> list) {
        if(list != null && list.size()>0) {
            for (ContractProduct contractProduct : list) {
                save(contractProduct);
            }
        }
    }

    /**
     * 保存货物
     *      参数：货物对象
     *            货物对象中，包含了所属的购销合同id
     */
    public void save(ContractProduct cp) {
        //1.计算保存的货物总金额
        double moeny = 0.0;
        if(cp.getCnumber() != null && cp.getPrice() !=null) {
            moeny = cp.getCnumber() * cp.getPrice() ;
        }
        cp.setAmount(moeny);
        //2.设置货物ID
        cp.setId(UUID.randomUUID().toString());
        //3.保存货物
        contractProductDao.insertSelective(cp);
        //4.根据购销合同id查询合同
        String contractId = cp.getContractId(); //货物中的购销合同id
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        //5.修改合同总金额 //用之前的加上本次货物的总金额
        contract.setTotalAmount(contract.getTotalAmount() + moeny);
        //6.修改货物批次数量 +1
        contract.setProNum(contract.getProNum() + 1);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 更新货物
     *  参数：货物对象，
     *          有一个属性contractId(当前货物所属的合同id)
     */
    public void update(ContractProduct newCp) {
        //1.计算更新之后的总金额
        double newMoney = 0.0;
        if(newCp.getCnumber() != null && newCp.getPrice() !=null) {
            newMoney = newCp.getCnumber() * newCp.getPrice() ;
        }
        newCp.setAmount(newMoney);
        //2.查询更新之前的总金额
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(newCp.getId());
        Double oldMoney = oldCp.getAmount();
        //3.更新货物
        contractProductDao.updateByPrimaryKeySelective(newCp);
        //4.根据id查询合同
        Contract contract = contractDao.selectByPrimaryKey(newCp.getContractId());
        //5.设置合同总金额
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + newMoney);
        //6.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**删除货物
     *  参数：货物id
     *      货物和附件，是一个一对多关系
     *      附件对象：ExtCproduct （contractProductId:附件所属货物id）
     */
    public void delete(String id) {
        //1.根据货物id，查询货物对象
        ContractProduct cp = contractProductDao.selectByPrimaryKey(id);
        //2.根据货物id，查询此货物的所有附件 ,
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(id) ; //根据货物id，查询此货物的附件

        List<ExtCproduct> exts = extCproductDao.selectByExample(example);
        //3.删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //4.删除附件
        //5.计算出删除货物和附件的总金额
        double deleteMoney = cp.getAmount(); //删除的货物总金额
        for (ExtCproduct ext : exts) { //循环所有附件
            extCproductDao.deleteByPrimaryKey(ext.getId());
            deleteMoney += ext.getAmount();
        }
        //6.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //7.修改合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - deleteMoney);
        //8.修改附件数和货物数
        contract.setProNum(contract.getProNum() - 1);
        contract.setExtNum(contract.getExtNum() - exts.size());
        //9.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }
}