package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;


    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue ="10") int size,
                       String contractId,String contractProductId){

        //1、根据货物id，查询此货物的附件
        ExtCproductExample example  = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(contractProductId);
        request.setAttribute("page",extCproductService.findAll(example ,page,size));
        //2、查询所有生产附件的厂商列表
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria criteria1 = fe.createCriteria();
        criteria1.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(fe);
        request.setAttribute("factoryList",factoryList);
        //3.同时传入货物和合同的id
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        //4.跳转页面
        return "cargo/extc/extc-list";
    }
    /**
     * 保存或者修改
     *      参数 extCproduct（附件对象）
     *              包含 contractId 合同
     *              包含 contractProductId 所属货物id
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct  extCproduct) throws Exception {
        //1.设置租户数据（基本信息）
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());

        //2.判断参数中是否具有id
        if(StringUtils.isEmpty(extCproduct.getId())) {
            //2.1 如果没有id，保存
            extCproductService.save(extCproduct);
        }else{
            //2.2 有id，更新
            extCproductService.update(extCproduct);
        }
        //3.重定向到列表
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }
    //进入更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId) {
        //1.根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);
        //2.查询生产厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(fe);
        request.setAttribute("factoryList",factoryList);

        //3.为了页面方便操作
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        //跳转页面
        return "cargo/extc/extc-update";
    }
    /**
     * 删除附件
     *  参数1：货物id（删除）
     *  参数2：购销合同id : contractId
     *  参数3：货物id    : contractProductId
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId ) {
        //调用service删除
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }

}