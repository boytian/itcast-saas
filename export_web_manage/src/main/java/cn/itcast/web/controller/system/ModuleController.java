package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.company.ModuleService;
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
 * @Date: 2020/3/13 18:13
 */
@Controller
@RequestMapping("/system/module/")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * 分页
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size){

        PageInfo info = moduleService.findAll(page, size);

        request.setAttribute("page", info); //page名称

        return "system/module/module-list";
    }
    /**
     * 进入保存
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //查询所有的菜单
        List list=moduleService.findAll();
        request.setAttribute("menus",list);
        return "system/module/module-add";
    }
    /**
     * 更新
     */
    @RequestMapping("/edit")
    public String edit(Module module) {
        //1、判断是否具有id
        if (StringUtils.isEmpty(module.getId())){
            //没有id，保存
            moduleService.save(module);
        }else {
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }
    /**
     * 新增或者修改
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //2.根据id查询部门
        Module module = moduleService.findById(id);
        request.setAttribute("module",module);
        //3.跳转页面
        return "system/module/module-update";
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        //1.调用service删除
        moduleService.delete(id);
        //2.重定向到列表
        return "redirect:/system/module/list.do";
    }

}
