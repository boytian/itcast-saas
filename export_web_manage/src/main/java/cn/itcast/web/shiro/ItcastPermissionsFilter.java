package cn.itcast.web.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义shiro中的权限过滤器
 *  继承抽象父类AuthorizationFilter
 *  实现方法
 *      isAccessAllowed
 */
public class ItcastPermissionsFilter extends AuthorizationFilter {

    /**
     *  company/* = perms["企业管理","部门管理"]
     *      mappedValue = {"企业管理","部门管理"}
     *  参数：
     *      appedValue: 在xml中配置过滤器传入的参数
     *  返回值：
     *      true：表示具有权限
     *      false：没有权限（自动的跳转到指定的权限不足页面）
     *  需求：
     *      可以接受多个配置参数，多个权限之间只需要任意满足一些即可通过
     */
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //1.获取subject
        Subject subject = this.getSubject(request, response);
        //2.获取配置的权限参数
        String[] perms = (String[])((String[])mappedValue);
        //3.判断是否具有权限
        if (perms != null && perms.length > 0) {
            boolean flag = false;
            for (String perm : perms) {  //["企业管理","部门管理"]
                //匹配是否具有某项权限
                //true：具有权限，false：没有权限
                if(subject.isPermitted(perm)) {
                    flag=true;
                }
            }
            return flag;
        }else{
            //没有传入权限参数
            return true;
        }
    }
}