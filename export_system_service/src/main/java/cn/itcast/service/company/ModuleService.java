package cn.itcast.service.company;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:09
 */
public interface ModuleService {
    PageInfo findAll(int page, int size);
    List findAll();

    void save(Module module);

    void update(Module module);

    Module findById(String id);

    void delete(String id);
}