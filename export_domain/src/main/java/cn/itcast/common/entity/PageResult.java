package cn.itcast.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 * @author Administrator
 *
 */
@Data
public class PageResult implements Serializable {

    private long total;     //总记录数
    private List list;      //当前页记录
    private int pageNum;    //当前页
    private int pageSize;   //每页的数量
    private int pages;      //总页数
    private int navigateFirstPage;   //导航条上的第一页
    private int navigateLastPage;     //导航条上的最后一页
    private int prePage;    //前一页
    private int nextPage;   //下一页
    private static int DEFAULT_PAGE_RANGE=4;

    public PageResult(){}

    /**
     * 构造方法
     * @param total  总数量
     * @param list   数据列表
     * @param page   当前页
     * @param size   每页显示条数
     */
    public PageResult(long total, List list, int page, int size) {
        super();
        this.total = total;
        this.list = list;
        this.pageNum = page;
        this.pageSize = size;
        // 计算
        this.pages = (int) (total % size == 0 ? (total/size): (total/size+1));
        // 获取显示起始页码
        calcPage(page,pages,2);
        this.prePage = page == 1 ? 1: page-1;
        this.nextPage = page == pages ? pages:page+1;
    }

    public void calcPage(int pageNum,int pageCount,int sideNum){
        int startNum = 0;
        int endNum = 0;

        if(pageCount<=sideNum){
            endNum = pageCount;
        }else{
            if((sideNum+pageNum)>=pageCount){
                endNum = pageCount;
            }else{
                endNum = sideNum+pageNum;
                if((sideNum+pageNum)<=(2*sideNum+1)){
                    if((2*sideNum+1)>=pageCount){
                        endNum = pageCount;
                    }else{
                        endNum = 2*sideNum+1;
                    }
                }else{
                    endNum = sideNum + pageNum;
                }
            }
        }
        if(pageNum<=sideNum){
            startNum = 1;
        }else{
            if((pageNum+sideNum)>=pageCount){
                if((2*sideNum+1)>=pageCount){
                    if((pageCount - 2*sideNum)>=1){
                        startNum = pageCount - 2*sideNum;
                    }else{
                        startNum = 1;
                    }
                }else{
                    startNum = pageCount - 2*sideNum;
                }
            }else{
                if((pageNum-sideNum)>=1){
                    startNum = pageNum - sideNum;
                }else{
                    startNum = 1;
                }
            }
        }
        this.navigateFirstPage = startNum;
        this.navigateLastPage = endNum;
    }
}