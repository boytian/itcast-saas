package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;

import java.util.List;

public interface FactoryDao {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Factory selectByPrimaryKey(String id);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 全属性保存 ： 对实体类对象中不等于空的属性发送保存语句
     *      User
     *          name  ： zhangsan
     *          age   :  null
     *
     *      sql : insert into user (name) values ("zhangsan");
     */
    int insert(Factory record);

    /**
     * 可选属性保存
     */
    int insertSelective(Factory record);

    int updateByPrimaryKey(Factory record);

    int updateByPrimaryKeySelective(Factory record);

    List<Factory> selectByExample(FactoryExample example);

}