package com.ysheep.rpc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/11/  22:14:00
 */
@Data
public class InvokeMessage implements Serializable {
    //接口名,服务名
    private String className;
    //方法名
    private String methodName;
    //参数类型
    private Class<?>[] paramTypes;
    //参数值
    private Object[] paramValues;
}
