import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试FactoryDao中的查询方法
 *      测试 selectByExample(FactoryExample example)
 *      方法作用：根据条件查询，获取数据列表
 *      参数 example 查询条件对象：包含了查询的字段和查询方式（=，like，in）
 *      如果 example == null ：查询全部
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class FactoryDaoTest {

    @Autowired
    private FactoryDao factoryDao;

    @Test
    public void testQuery() {
//      //查询条件对象
//      FactoryExample example = null;
//      List<Factory> list = factoryDao.selectByExample(example);
//      for (Factory factory : list) {
//          System.out.println(factory);
//      }

        //根据工厂名称模糊查询
        //SQL :SELECT * FROM co_factory WHERE factory_name LIKE '升%'
        //SELECT * FROM co_factory WHERE factory_name = '升华'
        //SELECT * FROM co_factory WHERE factory_name LIKE '升%' AND contacts ='刘生'
        //1.创建Example对象
        FactoryExample example = new FactoryExample();
        //2.创建Criteria对象
        FactoryExample.Criteria criteria = example.createCriteria();
        //3.向criter对象中添加查询条件
        //语法：and + 属性名 + 查询方式 (查询参数)
        //criteria.andFactoryNameLike("升%");
        //criteria.andFactoryNameEqualTo("升华");
        criteria.andFactoryNameLike("升%");
        criteria.andContactsEqualTo("刘生");

        //4.调用selectByExample方法发送查询
        List<Factory> list = factoryDao.selectByExample(example);
        for (Factory factory : list) {
            System.out.println(factory);
        }
    }

}