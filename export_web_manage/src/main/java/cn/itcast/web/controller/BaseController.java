package cn.itcast.web.controller;

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


    //获取当前登录用户的企业id
    protected String getLoginCompanyId() {
        return "1" ;//模拟当前登录用户所属的企业id
    }

    //获取当前登录用户的企业名称
    protected String getLoginCompanyName() {
        return "传智播客" ;//模拟当前登录用户所属的企业名称
    }
}