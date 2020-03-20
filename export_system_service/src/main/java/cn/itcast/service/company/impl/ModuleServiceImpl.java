package cn.itcast.service.company.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
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
    @Resource
    private UserDao userDao ;

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

    public String []  findRoleAddModule(String id) {
        String [] moduleIds=moduleDao.findRoleAddModule(id);
        return moduleIds;
    }

    /**
     * 角色添加权限
     * 操作中间表
     * 先根据角色id删除所有之前的模块权限
     * 在添加现有的模块权限
     * @param roleid
     * @param moduleIds
     */
    public void updateRoleModule(String roleid, String moduleIds) {
        //执行删除
        moduleDao.deleteRoleModule(roleid);
        //分词关联新的权限
        String[] split = moduleIds.split(",");
        for (String s : split) {
            moduleDao.saveRoleModule(roleid,s);
        }
    }

    public List<Module> findByUserId(String id) {
        //1.根据id查询用户
        User user = userDao.findById(id);
        //2.获取用户的degree
        Integer degree = user.getDegree();
        //3.判断用户的类型
        if(degree == 0) {
            //3.1 saas管理员: 查询belong=0的所有模块
            return moduleDao.findByBelong(0);
        }else if(degree == 1) {
            //3.2 企业管理员: 查询belong=1的所有模块
            return moduleDao.findByBelong(1);
        }else {
            //3.3 企业普通员工
            return moduleDao.findByUserId(id);
        }
    }
}
