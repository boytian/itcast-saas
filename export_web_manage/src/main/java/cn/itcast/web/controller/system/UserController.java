package cn.itcast.web.controller.system;

import cn.itcast.common.utils.Encrypt;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.company.DeptService;
import cn.itcast.service.company.RoleService;
import cn.itcast.service.company.UserService;
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
 * @Date: 2020/3/12 20:10
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private DeptService deptService;

    /**
     * 查询用户列表
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public  String list(@RequestParam(defaultValue ="1") int page,@RequestParam(defaultValue = "10") int size){
        PageInfo info=userService.finAll(page,size,getLoginCompanyId());
        request.setAttribute("page",info);
        return "system/user/user-list";
    }
    /**
     * 进入保存页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //查询部门进行回显
        List all = deptService.findAll(getLoginCompanyId());
        request.setAttribute("deptList",all);
        return "system/user/user-add";
    }

    /**
     * 保存用户
     */
    @RequestMapping("/edit")
    public String edit(User user){
        //设置企业参数
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());
        if (StringUtils.isEmpty(user.getId())){
            //执行保存
            String password = Encrypt.md5(user.getPassword(), user.getEmail());
            user.setPassword(password);
            userService.save(user);
        }else {
            //执行修改
            userService.update(user);
        }
        return "redirect:/system/user/list.do";
    }

    /**
     * 更新前先查询进进行数据回显
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //根据id查询用户
        User user=userService.findById(id);
        request.setAttribute("user",user);
        //回显所在部门，先查询，在页面回显
        List all = deptService.findAll(getLoginCompanyId());
        request.setAttribute("deptList",all);
        return "system/user/user-update";
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        userService.delete(id);
        return "redirect:/system/user/list.do";
    }

    @Resource
    private RoleService roleService;
    /**
     * 用户分配角色，跳转分配页面
     */
    @RequestMapping("/roleList")
    public String roleList(@RequestParam("id") String id){
        //查询是谁要分配角色
        User user = userService.findById(id);
        request.setAttribute("user",user);
        //有什么可以分配角色
        List<Role> roleList = roleService.findAll(getLoginCompanyId());
        request.setAttribute("roleList",roleList);
        //当前用户具有什么角色
        List<Role> list=roleService.findUserById(id);
        //构造用户角色id字符串
        String userRoleStr="";
        for (Role role : list) {
            userRoleStr+=role.getId();
        }
        request.setAttribute("userRoleStr",userRoleStr);
        return "/system/user/user-role";
    }

    /**
     * 保存用户的角色
     * @param userid
     * @param roleIds
     * @return
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userid,String[] roleIds){
        //业务
        roleService.changeRole(userid,roleIds);
        return "redirect:/system/user/list.do";
    }
}
