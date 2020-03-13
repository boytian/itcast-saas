package cn.itcast.service.company.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.service.company.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:09
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;

    public PageInfo findAll(int page, int size) {

        PageHelper.startPage(page,size);
        List<Module> all = moduleDao.findAll();
        return new PageInfo(all);
    }

    public List findAll() {
        return moduleDao.findAll();
    }

    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    public void update(Module module) {
        moduleDao.update(module);
    }

    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    public void delete(String id) {
        moduleDao.delete(id);
    }
}
