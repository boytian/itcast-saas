package cn.itcast.service.company;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 21:48
 */
public interface UserService {
    PageInfo finAll(int page, int size, String loginCompanyId);

    void save(User user);

    void update(User user);

    User findById(String id);

    void delete(String id);
}
