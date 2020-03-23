package cn.itcast.web.controller.cargo;

import cn.itcast.common.utils.UploadUtils;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/**
 * 购销合同的控制
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    /**
     * 分页查询数据列表
     */
    @Reference
    private FactoryService factoryService;


    /**
     * 分页查询数据列表
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       String contractId) {
        //根据购销合同id，查询购销合同下的所有货物
        ContractProductExample example = new ContractProductExample();
        ContractProductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        PageInfo pageInfo = contractProductService.findAll(example, page, size);
        request.setAttribute("page",pageInfo);
        //查询所有的生产厂家，构造下拉框
        //查询所有ctype=货物的厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(fe);
        request.setAttribute("factoryList",factoryList);
        //在展示列表的时候，为了展示所属的购销合同id
        request.setAttribute("contractId",contractId);
        //页面跳转
        return "cargo/product/product-list";
    }


    /**
     * 文件上传
     *      对象：MultipartFile（上传文件）
     *          文件的名称
     *          文件的byte数组
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        //1.设置租户数据（基本信息）
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        //2.判断参数中是否具有id
        if(StringUtils.isEmpty(contractProduct.getId())) {
            //判断是否选择了文件
            if(!productPhoto.isEmpty()) {
                //将文件上传到七牛云，获取访问路径
                String url = new UploadUtils().upload(productPhoto.getBytes());
                //将访问路径设置到contractProduct
                contractProduct.setProductImage(url);
            }
            //2.1 如果没有id，保存
            contractProductService.save(contractProduct);
        }else{
            //判断是否选择了文件
            if(!productPhoto.isEmpty()) {
                //将文件上传到七牛云，获取访问路径
                String url = new UploadUtils().upload(productPhoto.getBytes());
                //将访问路径设置到contractProduct
                contractProduct.setProductImage(url);
            }
            //2.2 有id，更新
            contractProductService.update(contractProduct);
        }
        //3.重定向到列表
        return "redirect:/cargo/contract/list.do";
    }

    //进入更新页面
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //查询所有的生产厂家
        //查询所有ctype=货物的厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(fe);
        request.setAttribute("factoryList",factoryList);
        //根据id查询货物
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);
        //跳转页面
        return "cargo/product/product-update";
    }

    /**
     * 删除货物
     *  参数1：货物id
     *  参数2：购销合同id
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId) {
        //业务
        contractProductService.delete(id);
        //当删除成功之后，重定向到列表
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 进入上传货物页面
     *  参数：contractId （购销合同id）
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }


    /**
     * 批量上传货物：解析excel，构造货物对象，保存到数据库
     * 参数：
     *  购销合同id      ：contractId
     *  上传的excel文件 ：file
     * 业务：
     *  解析Excel中的所有数据
     *  构造货物对象
     *  调用service保存
     * 跳转页面：
     *
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {
        //i.解析Excel，创建货物list数组
        List<ContractProduct> list = new ArrayList();
        //1.通过上传的文件，创建工作簿对象
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        //2.获取第一页
        Sheet sheet = wb.getSheetAt(0);
        //3.循环获取每一行
        for(int i=1; i<sheet.getLastRowNum() +1 ;i++) {
            Row row = sheet.getRow(i);
            Object [] objs = new Object[10];   //objs[0]=null , object[1]=升华，object[2]=货号，object[3]=刷量，
            for(int j=1;j<row.getLastCellNum(); j++) {
                //4.循环获取每行中的每一个单元格
                Cell cell = row.getCell(j);
                //5.获取每个单元格中的数据
                objs[j] = getCellValue(cell);
            }
            //6.根据每行中的所有数据，构造货物对象
            //具有常规属性
            ContractProduct cp = new ContractProduct(objs,getLoginCompanyId(),getLoginCompanyName());
            //货物所属的购销合同，设置购销合同id
            cp.setContractId(contractId);
            list.add(cp);
        }

        //ii.调用service批量保存货物数据
        contractProductService.saveAll(list);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    //解析每个单元格的数据 : 当前单元格中的数据
    public  Object getCellValue(Cell cell) {
        Object obj = null;
        CellType cellType = cell.getCellType(); //获取单元格数据类型
        switch (cellType) {
            case STRING: { //字符串单元
                obj = cell.getStringCellValue();
                break;
            }
            //excel默认将日志也理解为数字
            case NUMERIC:{ //数字单元格
                if(DateUtil.isCellDateFormatted(cell)) { //日期
                    obj = cell.getDateCellValue();
                }else {
                    obj = cell.getNumericCellValue();
                }
                break;
            }
            case BOOLEAN:{ //boolean
                obj = cell.getBooleanCellValue();
                break;
            }
            default:{
                break;
            }
        }

        return obj;
    }
}