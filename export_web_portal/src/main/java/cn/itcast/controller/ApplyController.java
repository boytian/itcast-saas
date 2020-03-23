package cn.itcast.controller;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

    @Reference
    private CompanyService companyService;

    /**
     * 企业申请
     *      1.将页面传递的参数保存到数据库
     *      2.当前方法参数；企业对象
     *      3.页面需要返回json字符串数据
     *  返回值：
     *      返回1：操作成功
     *      返回非1：操作失败
     */
    @RequestMapping("/apply")
    public @ResponseBody String apply(Company company) {
        try {
            companyService.save(company);
            return "1";
        }catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

}