package cn.itcast.web.controller;


import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class LoginController extends BaseController{

    @Resource
    private UserService userService;
    @Resource
    private ModuleService moduleService;
    /**
     * 传统的用户登录
     * @param email
     * @param password
     * @return
     */
	/*@RequestMapping("/login")
	public String login(String email,String password) {
	    //判断是否为空,为空就重新登陆
        if (StringUtils.isEmpty(email) ||StringUtils.isEmpty(password)){
            return "forward:/login.jsp";
        }
        //不为空调用service查询是否存在
       User loginUser=userService.findByEmail(email);
        //密码进行加密和数据库对比
        password = Encrypt.md5(password, email);

        //不存在，重新登录
        if (loginUser == null || !loginUser.getPassword().equals(password)) {
            request.setAttribute("error","用户名或密码错误");
            return "forward:/login.jsp";
        }
        //存在
        //把用户存入session跳转页面
        session.setAttribute("loginUser",loginUser);
        //4.用户登录成功之后，查询可操作的菜单模块数据
        List<Module> list = moduleService.findByUserId(loginUser.getId());   //参数可以是userId，也可以是登录的用户对象
        session.setAttribute("modules",list);
		return "home/main";
	}*/
    /**
     * 基于shiro的用户登录
     */
    @RequestMapping("/login")
    public String login(String email,String password) {
        //1.判断邮箱和密码是否为null,如果为null重新登录
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return "forward:/login.jsp";
        }
        try {
            //2.获取工具类subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用subject的login方法，通过shiro登录
            UsernamePasswordToken upToken = new UsernamePasswordToken(email,password); //用户名和密码的封装对象
            subject.login(upToken);
            //4.正常执行，shiro正常执行认证流程，shiro会将用户对象自动的保存到shiro的内存中
            //用户对象
            User loginUser = (User)subject.getPrincipal() ;//获取安全数据（用户对象）
            session.setAttribute("loginUser",loginUser);
            //可操作的所有模块
            List<Module> list = moduleService.findByUserId(loginUser.getId());
            session.setAttribute("modules",list);
            return "home/main";
        }catch (Exception e) {
            //5.抛出异常
            e.printStackTrace();
            request.setAttribute("error","用户名或者密码错误");
            return "forward:/login.jsp";
        }
    }

   /* //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }*/
   //退出
   @RequestMapping(value = "/logout",name="用户登出")
   public String logout(){
       //清空shiro中的内存数据，调用shiro的方法登出
       SecurityUtils.getSubject().logout();
       return "forward:login.jsp";
   }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
