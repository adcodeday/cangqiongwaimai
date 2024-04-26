 package com.Lu.aspect;

import com.Lu.annocation.AutoFill;
import com.Lu.constant.AutoFillConstant;
import com.Lu.context.BaseContext;
import com.Lu.enumeration.OperationType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

 /*
 自定义切面类，实现自动填充逻辑
  */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.Lu.mapper.*.*(..)) && @annotation(com.Lu.annocation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，匹配切入点表达式
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充");
        //获取当前mapper注解参数类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//获得方法签名
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);//获得方法签名上的注解
        OperationType operationType = autoFill.value();//获得数据库操作类型
        //获取到被拦截方法的参数
        Object[] args = joinPoint.getArgs();//获得参数
        if(args==null||args.length==0){//安全一下
            return;
        }
        Object entity = args[0];
        //准备赋值的数据，通过threadlocal
        Long id= BaseContext.getCurrentId();
        LocalDateTime localDateTime=LocalDateTime.now();
        //根据不同的操作类型，为当前的属性进行复制
        if(operationType==OperationType.INSERT){
            //四个公共字段赋值
            try {
                Method setCreatTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreatUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreatTime.invoke(entity,localDateTime);
                setCreatUser.invoke(entity,id);
                setUpdateTime.invoke(entity,localDateTime);
                setUpdateUser.invoke(entity,id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType==OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity,localDateTime);
                setUpdateUser.invoke(entity,id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
