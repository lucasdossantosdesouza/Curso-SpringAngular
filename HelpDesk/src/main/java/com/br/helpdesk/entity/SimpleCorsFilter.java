package com.br.helpdesk.entity;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {
    /** {@inheritDoc}} */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /** {@inheritDoc}} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        if("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())){
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }else{
            chain.doFilter(request, response);
        }

    }

    /** {@inheritDoc}} */
    @Override
    public void destroy() {}
}
