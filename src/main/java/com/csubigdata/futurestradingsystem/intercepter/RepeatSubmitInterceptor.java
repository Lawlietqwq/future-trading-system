package com.csubigdata.futurestradingsystem.intercepter;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.RepeatSubmit;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 重复提交拦截器
 * HandlerInterceptor接口是Spring框架中用于拦截请求处理的接口。它允许在请求到达控制器方法之前和之后执行自定义的逻辑，以进行预处理和后处理操作。
 */
@Component
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    /**
     * 请求承诺书
     */
    private static final String REPEAT_PARAMETERS = "repeatParameters";
    /**
     * 请求时间
     */
    private static final String REPEAT_TIME = "repeatTime";
    /**
     * 请求间隔小于10s才处理
     */
    private static final int REPEAT_TIME_INTERVAL = 10;

    @Override
    //在请求到达控制器方法之前调用, 返回值表示是否允许继续处理请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            RepeatSubmit repeatSubmit = handlerMethod.getMethodAnnotation(RepeatSubmit.class);
            if (repeatSubmit == null) {
                return true;
            }
            String parameterMap = JsonUtil.objToJson(request.getParameterMap());
            Map<String, Object> nowData = new HashMap<>(8);
            nowData.put(REPEAT_PARAMETERS, parameterMap);
            long nowTime = System.currentTimeMillis();
            nowData.put(REPEAT_TIME, nowTime);

            String requestURI = request.getRequestURI();

            HttpSession session = request.getSession();
            Object repeatData = session.getAttribute("repeatData");
            if (repeatData != null) {
                Map<String, Object> sessionData = (Map<String, Object>) repeatData;
                if (sessionData.containsKey(requestURI)) {
                    Map<String, Object> oldData = (Map<String, Object>) sessionData.get(requestURI);
                    long oldTime = (Long) oldData.get(REPEAT_TIME);
                    String oldParameterMap = (String) oldData.get(REPEAT_PARAMETERS);
                    if (parameterMap.equals(oldParameterMap) && (nowTime - oldTime) / 1000 < REPEAT_TIME_INTERVAL) {
                        log.warn("请勿重复提交");
                        CommonException.fail(ResultTypeEnum.REPEAT_ERROR);
                        return false;
                    }
                }
            }
            Map<String, Object> newSessionData = new HashMap<>();
            newSessionData.put(requestURI, nowData);
            session.setAttribute("repeatData", newSessionData);
        }
        return true;
    }

    @Override
    //在请求被处理后, 但在响应被发送之前调用, 可以用于修改响应内容
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    //在整个请求处理完成后调用,包括视图渲染完成,通常用于资源清理或日志记录
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}


