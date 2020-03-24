package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {


    @Reference
    private ContractService contractService;

    /**
     * 分页查询
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size) {

        //1.调用service查询
        //i.实例化example对象
        ContractExample example = new ContractExample();
        //ii.根据example创建Criteria对象
        ContractExample.Criteria criteria = example.createCriteria();
        //iii.设置查询条件   and + 属性 + 查询方式（参数）
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //查询所有状态=1的购销合同
        criteria.andStateEqualTo(1);

        //配置排序  ORDER BY create_time DESC
        example.setOrderByClause("create_time DESC");

        //iiii.发起调用
        PageInfo info = contractService.findAll(example, page, size);
        //2.将数据保存到request
        request.setAttribute("page",info);
        //3.跳转页面
        return "cargo/export/export-contractList";
    }
}