package org.miowing.mioverify.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.miowing.mioverify.exception.UnauthorizedException;
import org.miowing.mioverify.pojo.AToken;
import org.miowing.mioverify.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Some request must be checked, the authorization of which must contain correct token.
 */
@Component
@Aspect
public class AuthAspect {
    @Autowired
    private TokenUtil tokenUtil;
    @Pointcut("@annotation(org.miowing.mioverify.annotation.AuthorizedCheck)")
    public void authPointCut() {
    }
    @Around("authPointCut()")
    public Object beforeAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = (HttpServletRequest) attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if (request != null) {
                String auth = request.getHeader("Authorization");
                if (StrUtil.isEmpty(auth)) {
                    throw new UnauthorizedException();
                }
                auth = auth.replace("{","").replace("}", "");
                String[] parts = auth.split(" ");
                if (parts.length < 2 || !"Bearer".equals(parts[0])) {
                    throw new UnauthorizedException();
                }
                AToken aToken = tokenUtil.verifyAccessToken(parts[1], null, true);
                if (aToken == null) {
                    throw new UnauthorizedException();
                }
                return joinPoint.proceed();
            }
        }
        throw new UnauthorizedException();
    }
}