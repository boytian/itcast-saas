package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 购销合同的控制
 */
/**
 * 购销合同的控制
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size) {

        //1.调用service查询
        //i.实例化example对象
        ContractExample example = new ContractExample();
        //ii.根据example创建Criteria对象
        ContractExample.Criteria criteria = example.createCriteria();
        //iii.设置查询条件   and + 属性 + 查询方式（参数）
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        //为了完成细粒度权限控制，根据当前登录用户的不同级别，添加不同查询条件
        User user = getLoginUser();
        Integer degree = user.getDegree();
        if (degree == 4) { //4-普通员工 , 根据创建人查询
            criteria.andCreateByEqualTo(user.getId()); //查询当前登录用户
        }else if (degree == 3){ //3-管理本部门 ， 根据创建人部门查询
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }else if (degree == 2){ //2-管理所有下属部门和人员
            criteria.andCreateDeptLike(user.getDeptId() + "%" );
        }
        //配置排序  ORDER BY create_time DESC
        example.setOrderByClause("create_time DESC");

        //iiii.发起调用
        PageInfo info = contractService.findAll(example, page, size);
        //2.将数据保存到request
        request.setAttribute("page",info);
        //3.跳转页面
        return "cargo/contract/contract-list";
    }

    //进入新增页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    //进入更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //根据id查询
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        //跳转页面
        return "cargo/contract/contract-update";
    }

    //保存或者修改
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        //1.设置租户数据（基本信息）
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        //2.判断参数中是否具有id
        if(StringUtils.isEmpty(contract.getId())) {

            //2.1 如果没有id，保存
            User loginUser = getLoginUser();
            contract.setCreateBy(loginUser.getId());
            contract.setCreateDept(loginUser.getDeptId());
            contractService.save(contract);
        }else{
            //2.2 有id，更新
            contractService.update(contract);
        }
        //3.重定向到列表
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 根据id删除购销合同，同时要删除下面相关的货物和附件这里省略了，
     * 参数：id
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //业务：调用service删除
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 提交
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //业务：调用service更新
        contractService.submit(id);
        return "redirect:/cargo/contract/list.do";
    }


    @RequestMapping("/cancel")
    public String cancel(String id){
        //业务：调用service更新
        contractService.cancel(id);
        return "redirect:/cargo/contract/list.do";
    }
}