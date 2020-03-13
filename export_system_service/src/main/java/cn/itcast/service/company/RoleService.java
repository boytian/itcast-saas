package cn.itcast.service.company;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:07
 */
public interface RoleService {
    PageInfo findAll(String loginCompanyId, int page, int size);

   List<Role> findAll(String id);

    void save(Role role);

    void update(Role role);

    Role findById(String id);

    void delete(String id);

    List<Role> findUserById(String id);

    void changeRole(String userid, String[] roleIds);
}
