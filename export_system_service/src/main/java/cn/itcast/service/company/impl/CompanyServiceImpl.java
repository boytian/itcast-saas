package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}