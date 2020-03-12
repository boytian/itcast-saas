package cn.itcast.dao.system;

import cn.itcast.domain.system.User;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 21:49
 */
public interface UserDao {
    List<User> findAll(String loginCompanyId);

    void save(User user);

    void update(User user);

    User findById(String id);

    void delete(String id);
}
