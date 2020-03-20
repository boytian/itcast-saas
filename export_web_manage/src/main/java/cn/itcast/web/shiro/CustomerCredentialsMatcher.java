package cn.itcast.web.shiro;

/**
 * @Author: TianTian
 * @Date: 2020/3/20 21:01
 */

import cn.itcast.common.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 继承父类：
 *  SimpleCredntialsMatcher
 */
public class CustomerCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 密码比较方法
     *      token : 用户登录输入的的用户名和密码
     *      info： 安全数据
     *                  用户对象，数据库密码，realm域名
     * 返回值
     *      true：密码一致，登录成
     *      false：密码错误，自动的抛出异常
     *
     */
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.获取用户输入的密码和邮箱
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername(); //邮箱
        String password = new String(upToken.getPassword()); //char数组
        //2.获取数据库中的密码
        String dbPassword = (String)info.getCredentials();
        //3.对用户的输入密码加密
        password = Encrypt.md5(password,email);
        //4.比较密码
        return dbPassword.equals(password);
    }
}