package com.lovecyy.wallet.item.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lovecyy.wallet.item.common.constants.SystemConstants;

/**
 * 为生成token等
 */
public class JWTUtil {

    public static String generateToken(String str){
        return DigestUtil.md5Hex(str);
    }


    public static String getToken(){
        return ServletUtils.getHeader(SystemConstants.TOKEN_HEADER);
    }

    /**
     * 效验token
     * @param token
     * @return
     */
    public static boolean verifyToken(String token){
        if (StrUtil.isEmpty(token)){
            return false;
        }
        return true;
    }

}
