package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:07
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    public PageInfo findAll(String loginCompanyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<Role> list = roleDao.findAll(loginCompanyId);
        return new PageInfo(list);
    }

    public List<Role> findAll(String id) {
        return roleDao.findAll(id);
    }

    public void save(Role role) {
        String id= UUID.randomUUID().toString();
        role.setId(id);
        roleDao.save(role);
    }

    public void update(Role role) {
        roleDao.update(role);
    }

    public Role findById(String id) {
        return roleDao.findById(id);
    }

    public void delete(String id) {
        roleDao.delete(id);
    }

    public List<Role> findUserById(String id) {
        return roleDao.findUserById(id);
    }

    /**
     * 需求：根用户id重新分配角色
     * 1、删除该用户下的所有角色
     * 2、添加重新更新的角色
     *
     * @param userId
     * @param roleIds
     */
    public void changeRole(String userId, String[] roleIds) {
        roleDao.deleteUserRole(userId);
        //分词
        for (String roleId : roleIds) {
            roleDao.saveUserRole(userId,roleId);
        }
    }
}
