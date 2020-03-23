package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 20:08
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptDao deptDao;

    //分页查询
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页参数
        PageHelper.startPage(page,size);
        //2.查询数据列表 ,根据企业id查询某个企业下的所有部门
        List list = deptDao.findAll(companyId);
        //3.构造返回值PageInfo
        return new PageInfo(list);
    }
    //查询所有
    public List findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    public void save(Dept dept) {
        //1.设置主键id
        dept.setId(UUID.randomUUID().toString());
        //2.调用dao保存
        deptDao.save(dept);
    }

    //更新
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    //根据id查询
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    public void delete(String id) {
        deptDao.delete(id);
    }
}
