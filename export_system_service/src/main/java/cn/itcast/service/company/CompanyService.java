package cn.itcast.service.company;

/**
 * @Author: TianTian
 * @Date: 2020/3/10 20:47
 */

import cn.itcast.domain.company.Company;

import java.util.List;

/**
 * 业务层接口
 */
public interface CompanyService {

    List<Company> findAll();

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void delete(String id);
}
