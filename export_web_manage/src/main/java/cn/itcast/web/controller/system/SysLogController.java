package cn.itcast.web.controller.system;

import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Author: TianTian
 * @Date: 2020/3/20 20:19
 */
@Controller
@RequestMapping("/system/log/")
public class SysLogController extends BaseController {

    @Resource
    private SysLogService sysLogService;

    /**
     * 分页
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size) {
        //1.调用service查询，参数: 当前登录用户的企业id，page，size
        PageInfo info = sysLogService.findAll(getLoginCompanyId(),page,size);
        //2.存入到request域中
        request.setAttribute("page",info);
        //3.跳转页面
        return "system/log/log-list";
    }
}