package com.nino.engineer.web.interceptor;

import com.nino.engineer.web.constants.Constants;
import com.nino.engineer.web.context.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by ss on 18/3/14.
 */
@Component
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        log.info("-------------- requestPath is {}", path);
        if (Constants.ignoreUrl.contains(path)) return true;
        Integer userId;
        if ((userId = (Integer)request.getSession().getAttribute("u_id")) == null || request.getSession().getAttribute("username") == null) {
            log.info("用户未登录");
            response.sendRedirect("/login");
            return false;
        }
        ThreadLocalContext.setUserId(userId);
        String projectId = request.getParameter("p_id");
        if (StringUtils.isNotBlank(projectId))
            ThreadLocalContext.setProjectId(Integer.parseInt(projectId));
        return true;
    }
}
