package cn.itcast.web.controller.company;

import cn.itcast.common.entity.PageResult;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: TianTian
 * @Date: 2020/3/10 21:09
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
    @Resource
    private CompanyService companyService;

    /**
     * 1.改造controller中的list方法
     *  疑问：是否需要参数? 当前页和每页查询条数
     *  * 需要调用service查询pagebean对象
     *  * 将page对象存入到request
     *  * 修改页面
     * 2.修改service方法，完成分页查询
     *  * 查询总记录数
     *  * 通过分页的形式查询数据列表
     *  * 构造pagebean对象返回
     * 3.改造dao，添加两个查询方法
     */
    @RequiresPermissions(value = "企业管理")
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "5") Integer size) {
//        List<Company> all = companyService.findAll();
//        model.addAttribute("list",all);
        //======>传统
//        PageResult pr = companyService.findAll(page,size);
//        model.addAttribute("page",pr);
        //=======>分页插件
        PageInfo info = companyService.findPageByHelper(page, size);
        model.addAttribute("page",info);
        return "company/company-list";
    }

    /**
     * 进入新增企业页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //直接跳转到jsp页面
        return "company/company-add";
    }

    /**
     * 保存企业：
     *      参数：企业对象
     *      业务：
     *          1.调用service保存
     *          2.返回页面
     */
   /* @RequestMapping("/edit")
    public String edit(Company company) {
        //1.调用service保存
        companyService.save(company);
        //2.返回
        return "redirect:/company/list.do"; //保存之后，重定向到企业列表  /company/list.do
    }*/

    /**
     * 进入修改页面
     *      参数：企业id
     *      业务：
     *          1.根据id查询企业
     *          2.跳转到修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id, HttpServletRequest request) {
        //1.根据id查询企业
        Company company = companyService.findById(id);
        //2.企业对象保存到request中
        request.setAttribute("company",company);
        //3.跳转页面
        return "company/company-update";
    }

    /**
     * 保存企业：
     *      参数：企业对象
     *      业务：
     *          1.调用service保存
     *          2.返回页面
     * 保存或者更新企业
     *      参数：企业对象
     *      业务：
     *          1.判断当前对象的id参数
     *          1.1 如果id==null
     *              保存
     *          1.2 如果id!=null
     *              更新
     *          2.重定向到企业列表  /company/list.do
     */
    @RequestMapping("/edit")
    public String edit(Company company) {

//      if(company.getId()==null || "".equals(company.getId()))

        if(StringUtils.isEmpty(company.getId())) {
            //如果为null，调用service保存
            companyService.save(company);
        }else{
            //不为null，调用service更新
            companyService.update(company);
        }

        //2.返回
        return "redirect:/company/list.do"; //保存之后，重定向到企业列表  /company/list.do
    }
    /**
     * 根据id删除业务
     * 参数：id
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //业务：调用service删除
        companyService.delete(id);
        return "redirect:/company/list.do";
    }
}