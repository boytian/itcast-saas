package cn.itcast.service.company;

/**
 * @Author: TianTian
 * @Date: 2020/3/10 20:47
 */

import cn.itcast.common.entity.PageResult;
import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 业务层接口
 */
public interface CompanyService {

    List<Company> findAll();

    PageResult findAll(Integer page, Integer size);

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void delete(String id);

    PageInfo findPageByHelper(Integer page, Integer size);
}
