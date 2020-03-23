package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 20:08
 */
public interface DeptService {

    PageInfo findAll(String companyId, int page, int size);

    public List findAll(String companyId);

    void save(Dept dept);

    void update(Dept dept);

    Dept findById(String id);

    void delete(String id);
}
