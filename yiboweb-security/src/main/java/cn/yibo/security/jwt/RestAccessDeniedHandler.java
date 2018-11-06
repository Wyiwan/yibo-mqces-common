/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：RestAccessDeniedHandler.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */
package cn.yibo.security.jwt;

import cn.yibo.core.protocol.ResponseTs;
import cn.yibo.core.web.exception.BizException;
import cn.yibo.security.exception.LoginFailEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  描述: 权限拒绝处理类
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseTs.outResponseException(response, new BizException(LoginFailEnum.UNDECLARED_ERROR.getCode(), LoginFailEnum.UNDECLARED_ERROR.getDesc()));
    }

}
