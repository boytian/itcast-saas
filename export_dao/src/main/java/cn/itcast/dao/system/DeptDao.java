package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 20:15
 */
public interface DeptDao {
    //根据企业id查询全部
    List findAll(String companyId);

    //根据id查询部门
    Dept findById(String id);

    void save(Dept dept);

    void update(Dept dept);

    void delete(String id);
}
