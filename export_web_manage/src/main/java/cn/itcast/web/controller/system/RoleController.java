package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TianTian
 * @Date: 2020/3/13 18:13
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private ModuleService moduleService;

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
    /**
     * 进入角色模块页面
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid) {
        //1.调用service根据id查询角色，为那个角色设置权限
        Role byId = roleService.findById(roleid);
        //2.跳转页面
        request.setAttribute("role",byId);
        return "system/role/role-module";
    }
    /**
     * 根据角色id查询所拥有的模块权限:
     *      查询所有模块信息，
     *      每个模块转化成map集合
     *
     */
    @RequestMapping("/initModuleData")
    public @ResponseBody List<Map> getZtreeNodes(String id){
        List<Map> list=new ArrayList<Map>();
        //查询所有的模块
        List<Module> modules = moduleService.findAll();
        //查询该id拥有的模块
        String [] moduleId =moduleService.findRoleAddModule(id);
        for (Module module : modules) {
            Map map=new HashMap();
            for (String module1 : moduleId) {
                if (module.getId().equals(module1)){
                    map.put("checked",true);
                }
            }
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            list.add(map);
        }
        return list;
    }

    /**
     * 角色id，和模块id集合
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds){
        moduleService.updateRoleModule(roleid,moduleIds);
        return "redirect:/system/role/roleModule.do?roleid="+roleid;
    }



}
