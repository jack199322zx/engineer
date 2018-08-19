package com.nino.engineer.aop;

import com.nino.engineer.common.annotation.Logical;
import com.nino.engineer.common.annotation.StaffType;
import com.nino.engineer.common.annotation.SysPermission;
import com.nino.engineer.dao.PermissionsDetailedDao;
import com.nino.engineer.exception.NoPermissionException;
import com.nino.engineer.web.context.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ss
 * @date 2018/8/17 14:11
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class SysPermissionAspect {

    @Autowired
    PermissionsDetailedDao dao;

    @Pointcut("@annotation(com.nino.engineer.common.annotation.SysPermission)")
    public void sysAspect() {
    }

    @Around("sysAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("--------- signatureName is {}", signature.getName());
        Method method = signature.getMethod();
        SysPermission sysPermission = getAnnotation(method, SysPermission.class);
        if (sysPermission != null) {
            StaffType[] staffType = sysPermission.value();
            Logical logical = sysPermission.logical();
            int j_id = dao.judgePermission(ThreadLocalContext.getUserId());
            List<StaffType> staffTypes = Arrays.asList(staffType);
            List<String> pIds = staffTypes.stream().map(item -> item.getType()).collect(Collectors.toList());
            if (staffTypes.contains(StaffType.ALL)) {
                // 所有权限均可通过
                return resolveObj(joinPoint);
            }else if (logical == Logical.OR) {
                if (!pIds.contains(String.valueOf(j_id))) {
                    log.warn("不具备该权限");
                    throw new NoPermissionException();
                }
            }
        }
        return resolveObj(joinPoint);
    }

    private Object resolveObj(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException("encounter error in joinPoint.proceed()");
        }
        return obj;
    }

    // 如果方法和类上同时有注解，以方法上的注解为准
    private <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        if (method.isAnnotationPresent(annotationClass))
            return method.getAnnotation(annotationClass);
        Class<?> clz = method.getDeclaringClass();
        if (clz.isAnnotationPresent(annotationClass))
            return clz.getAnnotation(annotationClass);
        return null;
    }

}