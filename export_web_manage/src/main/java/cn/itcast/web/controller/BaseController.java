package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: TianTian
 * @Date: 2020/3/12 21:36
 */
//抽取公共父类
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;


    /**
     * 获取当前登录用户
     */
    protected User getLoginUser() {
        return (User) session.getAttribute("loginUser");
    }

    //获取当前登录用户的企业id
    protected String getLoginCompanyId() {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser!=null){
            User user=(User)loginUser;
            return user.getCompanyId();
        }
        return "" ;//模拟当前登录用户所属的企业id
    }

    //获取当前登录用户的企业名称
    protected String getLoginCompanyName() {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser!=null){
            User user=(User)loginUser;
            return user.getCompanyName();
        }
        return "" ;//模拟当前登录用户所属的企业名称
    }
}