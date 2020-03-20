package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    int delete(String moduleId);

    //添加用户
    int save(Module module);

    //更新用户
    int update(Module module);

    //查询全部
    List<Module> findAll();

    String [] findRoleAddModule(String id);

    void deleteRoleModule(String roleid);

    void saveRoleModule(@Param("roleid") String roleid, @Param("s")String s);

    //根据belong查询
    List<Module> findByBelong(int belong);

    //根据用户id，多表查询用户可操作的菜单
    List<Module> findByUserId(String id);
}