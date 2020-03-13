package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Role;
import cn.itcast.service.company.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:13
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 分页
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size){

        PageInfo info = roleService.findAll(getLoginCompanyId(), page, size);

        request.setAttribute("page", info); //page名称

        return "system/role/role-list";
    }
    /**
     * 进入保存
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "system/role/role-add";
    }
    /**
     * 更新
     */
    @RequestMapping("/edit")
    public String edit(Role role) {
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());
        //1、判断是否具有id
        if (StringUtils.isEmpty(role.getId())){
            //没有id，保存
            roleService.save(role);
        }else {
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }
    /**
     * 新增或者修改
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //2.根据id查询部门
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        //3.跳转页面
        return "system/role/role-update";
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        //1.调用service删除
        roleService.delete(id);
        //2.重定向到列表
        return "redirect:/system/role/list.do";
    }
}
