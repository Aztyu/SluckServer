package com.sluck.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.sluck.server.job.impl.UserJob;
import com.sluck.server.job.interfaces.IUserJob;

public class ApiFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String token = req.getHeader("Authorization");
		if(token != null){
			chain.doFilter(request, response);
		}else{
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "The request is missing an \"Authorization\" header.");
		}
	}

	@Override
	public void destroy() {}
}
