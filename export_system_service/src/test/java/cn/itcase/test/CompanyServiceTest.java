package cn.itcase.test;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/10 20:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-service.xml")
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Test
    public void testFindAll(){
        List<Company> all = companyService.findAll();
        for (Company company : all) {
            System.out.println(company);
        }
    }
}
