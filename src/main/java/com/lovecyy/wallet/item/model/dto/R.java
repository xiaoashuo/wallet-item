package com.lovecyy.wallet.item.model.dto;

import cn.hutool.json.JSONObject;
import com.lovecyy.wallet.item.common.enums.ResultCodes;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
@Data
public class R implements Serializable {

    private Integer code;
    private String msg;
    private Object data;

    public static R ok(){
        return createResult(CommonResponse.SUCCESS,null);
    }
    public static R ok(String msg){
        return createResult(CommonResponse.SUCCESS.getCode(),msg,null);
    }
    public static R ok(int code,String msg){
        return createResult(code,msg,null);
    }

    public static R ok(int code,String msg,Object data){
        return createResult(code,msg,data);
    }
    public static R ok(CommonResponse response,Object data){
        return createResult(response,data);
    }
    public static R ok(Object data){
        return createResult(CommonResponse.SUCCESS,data);
    }
    public static R ok(ResultCodes resultCodes){
        return createResult(resultCodes.getCode(),resultCodes.getMsg(),null);
    }
    public static R fail(){
        return createResult(CommonResponse.FAIL,null);
    }
    public static R fail(ResultCodes resultCodes){
        return createResult(resultCodes.getCode(),resultCodes.getMsg(),null);
    }
    public static R fail(String msg){
        return createResult(CommonResponse.FAIL.getCode(),msg,null);
    }
    public static R fail(int code,String msg){
        return createResult(code,msg,null);
    }
    public static R fail(int code,String msg,Object data){
        return createResult(code,msg,data);
    }
    private static R createResult(CommonResponse response,Object data){
        R r = new R();
        r.setCode(response.getCode());
        r.setMsg(response.getMsg());
        r.setData(data);
        return r;
    }

    private static R createResult(Integer code,String msg,Object data){
        R r = new R();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    @Getter
    public enum CommonResponse{
        SUCCESS(200,"Success"),
        FAIL(500,"fail");
        int code;
        String msg;
        CommonResponse(int code,String msg){
            this.code=code;
            this.msg=msg;
        }
    }


}
