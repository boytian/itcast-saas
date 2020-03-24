package cn.itcast.web.controller.cargo;

import cn.itcast.common.utils.DownloadUtil;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractProductVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @Author: TianTian
 * @Date: 2020/3/24 18:18
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    @Reference
    private ContractService contractService;

    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";
    }

    /**
     * 下载Excel
     * inputDate：船期
     * 设置内容步骤：先创建一个表，
     *              其次创建一页
     *              设置单元格列宽
     *              合并单元格
     *              创建一行
     *              创建一个单元格
     *              设置内容
     *              设置样式
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //查询所有的数据
        List<ContractProductVo> list = contractService.findByShipTime(inputDate, getLoginCompanyId());
        System.out.println(list);
        //制作excel表
        //创建一张表
        //Workbook wb = new XSSFWorkbook();
        // 百万数据处理
        Workbook wb = new SXSSFWorkbook();
        //创建一页
        Sheet sheet = wb.createSheet();
        //设置单元格的列宽，1代表第二列开始
        sheet.setColumnWidth(1, 26 * 256); //列索引，宽度*256
        sheet.setColumnWidth(2, 12 * 256); //列索引，宽度*256
        sheet.setColumnWidth(3, 30 * 256); //列索引，宽度*256
        sheet.setColumnWidth(4, 12 * 256); //列索引，宽度*256
        sheet.setColumnWidth(5, 15 * 256); //列索引，宽度*256
        sheet.setColumnWidth(6, 10 * 256); //列索引，宽度*256
        sheet.setColumnWidth(7, 10 * 256); //列索引，宽度*256
        sheet.setColumnWidth(8, 8 * 256); //列索引，宽度*256
        //合并单元格 ， 开始行，结束行，开始列，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        //3.创建大标题
        //3.1 创建行对象
        Row row = sheet.createRow(0);
        row.setHeightInPoints(36); //行高
        //3.2 创建单元格对象
        Cell cell = row.createCell(1);
        //3.3 设置内容  //输入参数   2020-01（2020-11）  --> 2020-1(2020-11)       --- 2012年8
        inputDate = inputDate.replaceAll("-0", "-").replaceAll("-", "年");
        cell.setCellValue(inputDate + "月份出货表");
        //3.4 设置单元样式
        cell.setCellStyle(bigTitle(wb));
        //4.创建小标题
        //4.1 创建行对象
        row = sheet.createRow(1);
        row.setHeightInPoints(26); //行高
        //4.2 创建单元格对象
        String [] titles = new String[]{"","客户","合同号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i <titles.length ; i++) {
            //获取行的格子
            cell = row.createCell(i);
            //4.2 设置内容
            cell.setCellValue(titles[i]);
            //设置样式
            cell.setCellStyle(title(wb));
        }
        //5.循环数据列表，构造数据行
        int index = 2;

for (int j=0;j<4000;j++) {
    for (ContractProductVo vo : list) {
        //5.1 创建行对象
        row = sheet.createRow(index);
        row.setHeightInPoints(24); //行高
        //cell.setCellStyle(text(wb)); //样式
        //5.2 创建客户单元格，设置客户内容
        cell = row.createCell(1);
        cell.setCellValue(vo.getCustomName());
        //cell.setCellStyle(text(wb)); //样式
        //5.3 "合同号",
        cell = row.createCell(2);
        cell.setCellValue(vo.getContractNo());
        //cell.setCellStyle(text(wb)); //样式
        //5.4 "货号",
        cell = row.createCell(3);
        cell.setCellValue(vo.getProductNo());
        //cell.setCellStyle(text(wb)); //样式
        //5.5 "数量",
        cell = row.createCell(4);
        cell.setCellValue(vo.getCnumber());
        //cell.setCellStyle(text(wb)); //样式
        //5.6 "工厂",
        cell = row.createCell(5);
        cell.setCellValue(vo.getFactoryName());
        //cell.setCellStyle(text(wb)); //样式
        //5.7 "工厂交期",
        cell = row.createCell(6);
        cell.setCellValue(vo.getDeliveryPeriod());
        //cell.setCellStyle(text(wb)); //样式
        //5.8 "船期",
        cell = row.createCell(7);
        cell.setCellValue(vo.getShipTime());
        //cell.setCellStyle(text(wb)); //样式
        //5.9"贸易条款"
        cell = row.createCell(8);
        cell.setCellValue(vo.getTradeTerms());
        //cell.setCellStyle(text(wb)); //样式
        index++;
    }

}
        //开始下载
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        //参数一字节数组输出流，参数二response对象，参数三：下载文件名
        new DownloadUtil().download(bos, response, "出货表.xlsx");
    }

    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        //单元格样式
        CellStyle style = wb.createCellStyle();
        //创建字体
        Font font = wb.createFont();
        font.setFontName("宋体"); //设置字体
        font.setFontHeightInPoints((short)16); //设置字号
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);             //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);       //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);             //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);       //纵向居中
        style.setBorderTop(BorderStyle.THIN);                       //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                      //左细线
        style.setBorderRight(BorderStyle.THIN);                     //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);               //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);       //纵向居中
        style.setBorderTop(BorderStyle.THIN);                       //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                      //左细线
        style.setBorderRight(BorderStyle.THIN);                     //右细线

        return style;
    }

    /**
     * 模板打印
     */
    @RequestMapping("/printExcelTemplate")
    public void printExcelTemplate(String inputDate) throws Exception {
        //i.根据船期查询数据
        List<ContractProductVo> list = contractService.findByShipTime(inputDate,getLoginCompanyId());
        System.out.println(list.size());
        //ii.模板打印的方式创建Excel
        //1.获取模板路径
        String path = session.getServletContext().getRealPath("/")+"/make/xlsprint/tOUTPRODUCT.xlsx";
        //2.通过模板创建工作簿
        Workbook wb = new XSSFWorkbook(path);
        //3.读取第一页
        Sheet sheet = wb.getSheetAt(0);
        //4.处理大标题
        //4.1 获取第一行
        Row row = sheet.getRow(0);
        //4.2 获取第二个单元格
        Cell cell = row.getCell(1);
        //4.3 设置单元格内容
        inputDate = inputDate.replaceAll("-0","-").replaceAll("-","年");
        cell.setCellValue(inputDate+"月份出货表");
        //5.处理小表题 -- 略
        //6.提取数据列表的样式
        //6.1 获取第三行
        row = sheet.getRow(2);
        //6.2 循环获取每个单元格的样式
        CellStyle css [] = new CellStyle[9];
        for (int i = 1; i < css.length; i++) {
            cell = row.getCell(i);
            css [i] = cell.getCellStyle();
        }
        //7.循环数据列表，构造数据行
        int index = 2;
        for (ContractProductVo vo : list) {
            row = sheet.createRow(index);
            //5.2 创建客户单元格，设置客户内容
            cell = row.createCell(1);
            cell.setCellValue(vo.getCustomName());
            cell.setCellStyle(css[1]); //样式
            //5.3 "合同号",
            cell = row.createCell(2);
            cell.setCellValue(vo.getContractNo());
            cell.setCellStyle(css[2]); //样式
            //5.4 "货号",
            cell = row.createCell(3);
            cell.setCellValue(vo.getProductNo());
            cell.setCellStyle(css[3]); //样式
            //5.5 "数量",
            cell = row.createCell(4);
            cell.setCellValue(vo.getCnumber());
            cell.setCellStyle(css[4]); //样式
            //5.6 "工厂",
            cell = row.createCell(5);
            cell.setCellValue(vo.getFactoryName());
            cell.setCellStyle(css[5]); //样式
            //5.7 "工厂交期",
            cell = row.createCell(6);
            cell.setCellValue(vo.getDeliveryPeriod());
            cell.setCellStyle(css[6]); //样式
            //5.8 "船期",
            cell = row.createCell(7);
            cell.setCellValue(vo.getShipTime());
            cell.setCellStyle(css[7]); //样式
            //5.9"贸易条款"
            cell = row.createCell(8);
            cell.setCellValue(vo.getTradeTerms());
            cell.setCellStyle(css[8]); //样式
            index ++;
        }


        //iii.下载Excel
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        new DownloadUtil().download(bos,response,"出货表.xlsx"); //参数一字节数组输出流，参数二response对象，参数三：下载文件名
    }
}
