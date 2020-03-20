package cn.itcast.web.shiro;

/**
 * @Author: TianTian
 * @Date: 2020/3/20 21:01
 */

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.company.ModuleService;
import cn.itcast.service.company.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm域
 *  继承AuthorizingRealm
 */
public class AuthRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private ModuleService moduleService;

    /**
     * 授权方法：提供数据
     *      参数：PrincipalCollection (安全数据集合)
     *              只有一个：用户对象User
     *      返回值：AuthorizationInfo（授权数据）
     *                  * 需要当前登录用户的所有权限字符串数组
     *      本质：
     *              根据当前用户查询，此用户可操作的所有模块名称
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.获取安全数据
        User user = (User) principals.getPrimaryPrincipal();
        //2.查询当前用户可操作的所有模块
        List<Module> list = moduleService.findByUserId(user.getId());
        //3.获取模块名称，构造名称集合,shiro中需要一个模块名称的set集合
        Set<String> names = new HashSet<String>();
        for (Module module : list) {
            names.add(module.getName());
        }
        //4.构造返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(names);
        return info;
    }

    /**
     * 认证方法
     *      * 查询安全数据（用户对象）
     *      参数：AuthenticationToken（接口）
     *           UsernamePasswordToken
     *           登录输入的用户名和密码
     *      返回值：
     *         AuthenticationInfo
     *
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.将传递的参数转化为邮箱和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername(); //邮箱
        String password = new String(upToken.getPassword()); //char数组
        //2.根据邮箱查询用户
        User user = userService.findByEmail(email);
        //判断用户是否存在
        if(user != null) {
            //3.如果用户存在，构造返回值AuthenticationInfo

            /**
             * 安全数据的封装对象
             *      1.安全数据（用户对象）
             *      2.密码（随意），为了方便后续操作，存入数据库中的密码
             *      3.realm域名称，约定俗成（当前类名）
             */
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }else{
            //4.如果用户不存在
            return null;
        }

    }
}