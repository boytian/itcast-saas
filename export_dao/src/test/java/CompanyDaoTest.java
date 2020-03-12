import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CompanyDaoTest {

    /**
     * 测试mybatis的运行环境
     */
    public static void main(String[] args) throws IOException {
        //1.加载核心配置文件
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactoryBuilder ：构建者模式
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //3.创建SqlSessionFactory ： 工厂模式
        SqlSessionFactory factory = builder.build(inputStream);
        //4.创建sqlsession ： 不是线程安全的
        SqlSession session = factory.openSession();
        //5.通过sqlsession创建dao的实现类（动态代理对象）
        CompanyDao companyDao = session.getMapper(CompanyDao.class);
        //6.查询
        List<Company> list = companyDao.findAll();
        for (Company company : list) {
            System.out.println(company);
        }
    }
}