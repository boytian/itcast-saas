package cn.itcast.service.company.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.company.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 21:48
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public PageInfo finAll(int page, int size, String loginCompanyId) {
       //1、设置分页参数
        PageHelper.startPage(page,size);
        List<User> list= userDao.findAll(loginCompanyId);
        return new PageInfo(list);
    }

    public void save(User user) {
        user.setId(UUID.randomUUID().toString());
        userDao.save(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public User findById(String id) {
        return userDao.findById(id);
    }

    public void delete(String id) {
        userDao.delete(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
