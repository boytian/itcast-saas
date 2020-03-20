package cn.itcast.web.aspect;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.company.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 自定义的切面类
 *  1.环绕通知的方法规则（返回值，参数，具体返回内容）
 *  2.配置环绕通知
 */
@Component
@Aspect //声明切面
public class LogAspect {

    @Resource
    private SysLogService sysLogService;

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpSession session;



    /**
     * 通过定义一个环绕通知方法
     *      返回值：Object
     *      参数：ProceedingJoinPoint pjp
     *              : 当前被增强的方法 （不能直接获取method对象）
     */
    @Around(value="execution(* cn.itcast.web.controller.*.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        //需要调用日志service，完成日志保存
        SysLog log = new SysLog();

        //操作时间
        log.setTime(new Date());
        Object obj = session.getAttribute("loginUser");
        if(obj != null) {
            User user = (User) obj;
            //当前操作人：通过session
            log.setUserName(user.getUserName());
            //企业id
            log.setCompanyId(user.getCompanyId());
            //企业名称
            log.setCompanyName(user.getCompanyName());
        }
        //操作人ip地址: 通过request
        log.setIp(request.getRemoteAddr());

        //1.获取方法标记对象
        MethodSignature ms = (MethodSignature)pjp.getSignature();
        //2.获取method对象
        Method method = ms.getMethod();
        //3.获取method对象的方法名称
        String methodName = method.getName();
        log.setMethod(methodName);
        //4.获取注解
        if(method.isAnnotationPresent(RequestMapping.class)) {
            //存在此注解
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            //注解上的name属性 此方法的中文描述
            String action = mapping.name();
            log.setAction(action);
        }

        sysLogService.save(log);
        return pjp.proceed();
    }
}