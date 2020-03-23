package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 20:07
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {
    @Resource
    private DeptService deptService;

    /**
     * 分页查询部门列表
     * 参数： page，size
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size) {

        //1.调用service查询，参数: 当前登录用户的企业id，page，size
        PageInfo info = deptService.findAll(getLoginCompanyId(), page, size);
        //2.存入到request域中
        request.setAttribute("page", info); //page名称
        //3.跳转页面
        return "system/dept/dept-list";
    }

    /**
     * 进入保存部门页面
     *  * 查询所有部门
     *  * 前端页面构造下拉框的
     *      <select>
     *          <option>请选择</option>
     *          <c:foreach items="${deptList}" var="dept">
     *               <option value=${dept.id}>${dept.deptName}</option>
     *          </c:foreach>
     *      </select>
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.查询当前登录用户所在企业的所有部门
        List list = deptService.findAll(getLoginCompanyId());
        //2.存入request中
        request.setAttribute("deptList",list);
        //3.跳转页面
        return "system/dept/dept-add";
    }

    /**
     * 进入到修改页面
     *  1.参数：当前勾选的部门id
     *  2.业务：
     *      2.1 根据id查询部门
     *      2.2 查询所有的部门（构造下拉框）
     *      2.3
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());
       //1、判断是否具有id
        if (StringUtils.isEmpty(dept.getId())){
            //没有id，保存
            deptService.save(dept);
        }else {
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    /**
     * 进入到修改页面
     *  1.参数：当前勾选的部门id
     *  2.业务：
     *      2.1 根据id查询部门
     *      2.2 查询所有的部门（构造下拉框）
     *      2.3
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

        //1.查询所有的部门（构造下拉框）
        List list = deptService.findAll(getLoginCompanyId());
        request.setAttribute("deptList",list);
        //2.根据id查询部门
        Dept dept = deptService.findById(id);
        request.setAttribute("dept",dept);
        //3.跳转页面
        return "system/dept/dept-update";
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        //1.调用service删除
        deptService.delete(id);
        //2.重定向到列表
        return "redirect:/system/dept/list.do";
    }
}
