package cn.itcast.service.company.impl;

import cn.itcast.common.entity.PageResult;
import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyDao companyDao;

    public List<Company> findAll() {
        List<Company> companyList = companyDao.findAll();
        return companyList;
    }

    /*分页查询*/
    public PageResult findAll(Integer page, Integer size) {
        //查询总记录数
        long total=companyDao.findCount();
        //分页形式查询数据库表
        List list = companyDao.findPage((page-1)*size,size);
        //构造对象返回
        return new PageResult(total,list,page,size);
    }

    //保存企业
    public void save(Company company) {
        //1.设置id （独一无二）
        company.setId(UUID.randomUUID().toString());
        //2.调用dao保存
        companyDao.save(company);
    }

    //根据id查询企业
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    //更新
    public void update(Company company) {
        companyDao.update(company);
    }

    public void delete(String id) {
        companyDao.delete(id);
    }

    public PageInfo findPageByHelper(Integer page, Integer size) {
        //设置分页参数
        PageHelper.startPage(page, size);
        //查询全部
        List<Company> all = companyDao.findAll();
        //构造返回值
        return new PageInfo(all);
    }
}