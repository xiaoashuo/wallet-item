package com.lovecyy.wallet.item.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.lovecyy.wallet.item.common.constants.StatusCode;
import com.lovecyy.wallet.item.common.constants.SystemConstants;
import com.lovecyy.wallet.item.common.utils.ServletUtils;
import com.lovecyy.wallet.item.model.dto.R;
import com.lovecyy.wallet.item.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private Cache loginCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(SystemConstants.TOKEN_HEADER);
        if (StrUtil.isEmpty(token)){
            response.setStatus(StatusCode.LOGIN_TOKEN_EXCEPTION);
            R result = R.fail(StatusCode.LOGIN_TOKEN_EXCEPTION, "请携带token,重新登录");
            ServletUtils.renderString(response, JSONUtil.toJsonStr(result));
            return false;
        }
        String userDtoStr  = (String) loginCache.asMap().get(token);
        if (StrUtil.isEmpty(userDtoStr)){
            response.setStatus(StatusCode.LOGIN_TOKEN_EXPIRE);
            R result = R.fail(StatusCode.LOGIN_TOKEN_EXPIRE, "token已过期,重新登录");
            ServletUtils.renderString(response, JSONUtil.toJsonStr(result));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
