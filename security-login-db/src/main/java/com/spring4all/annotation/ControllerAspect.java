package com.spring4all.annotation;

import com.spring4all.entity.UserEntity;
import com.spring4all.exception.PowerException;
import com.spring4all.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

@Aspect
@Component
public class ControllerAspect {

    private final static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private UserService userService;
    /**
     * 定义切点
     */
    @Pointcut(value ="execution(public * com.spring4all.controller.*.*(..))")
    public void privilege(){}

    /**
     * 权限环绕通知
     * @param joinPoint
     * @throws Throwable
     */
    @ResponseBody
    @Around("privilege()")
    public Object isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取访问目标方法
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        //得到方法的访问权限
        final String[] methodAccess = AnnotationParse.privilegeParse(targetMethod);

        //如果该方法上没有权限注解，直接调用目标方法
        if(StringUtils.isEmpty(methodAccess)){
            return joinPoint.proceed();
        }else {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            String username = request.getHeader("username");
            UserEntity user = userService.getByUsername(username);
            if(user==null){
                throw new PowerException("登录已失效，请重新登录");
            }
            String roles = user.getRoles();
            if(!StringUtils.isEmpty(roles)){
                String[] userPower = roles.split(",");
                HashSet<String> userPowerset = new HashSet<>(Arrays.asList(userPower));
                for (int i = 0; i < methodAccess.length; i++) {
                    if(userPowerset.contains(methodAccess[i])) {
                        return joinPoint.proceed();
                    }
                }
                throw new PowerException("用户权限不匹配");
            }else{
                throw new PowerException("用户无权访问");
            }

        }
    }
}