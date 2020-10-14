package com.lovecyy.wallet.item.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端工具类
 *
 * @author ruoyi
 */
public class ServletUtils
{
    private static final Logger log= LoggerFactory.getLogger(ServletUtils.class);

    /**
     * 获取String参数
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取请求头
     * @param name
     * @return
     */
    public static String getHeader(String name)
    {
        return getRequest().getHeader(name);
    }


    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 渲染401
     * @param response
     * @param string
     * @return
     */
    public static String render401String(HttpServletResponse response, String string)
    {
         response.setStatus(HttpStatus.UNAUTHORIZED.value());
         return renderString(response,string);
    }
    public static String extractUrlParams(String uri){
        try {
            Map<String,String> map=new HashMap<>();
            String[] params = uri.split("\\?");
            if (params.length<=1){
                return null;
            }
            String pathParams = params[1];
            String[] paramPairs = pathParams.split("&");
            if(paramPairs.length<=0){
                return null;
            }
            for (String paramPair : paramPairs) {
                String[] keyValuePair = paramPair.split("=");
                map.put(keyValuePair[0],keyValuePair[1]);
            }
            return map.get("token");
        } catch (Exception e) {
            log.error("提取websocket参数异常",e);
            return null;
        }

    }


}
