package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dao层接口
 */

public interface CompanyDao {

    /**
     * 查询所有的企业列表
     */
    List<Company> findAll();

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void delete(String id);

    /**
     * 查询总页数
     * @return
     */
    long findCount();

    //limit 分页查询列表
    List findPage(@Param("beg") int beg, @Param("end") int size);
}