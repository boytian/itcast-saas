package cn.itcast.web.execption;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomExecptionResolver implements HandlerExceptionResolver {
    /**
     * 异常处理方法
     *  对所有的异常统一拦截：一旦出现异常进入到此方法
     *      * 跳转到一个经过美化的错误页面
     *      * 携带一些隐晦的错误信息
     */
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg","对不起，我错啦~");
        mv.addObject("ex",ex);
        return mv;
    }
}