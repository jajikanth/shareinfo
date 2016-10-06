/**
 * 
 */
package com.jitworks.shareinfo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/*import com.sun.xml.bind.v2.TODO;*/

public class LoggingFilter implements Filter {

	private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
	private static final String HEADER_X_DEVICE_ID = "x-device-id";
	private static final String MDC_KEY_CLIENT = "client";
	private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		//TODO: for NI if required.
		String client = request.getHeader(HEADER_X_DEVICE_ID);
		if ((client == null) || client.equals("")) {
			// for shareinfo
			client = request.getHeader(HEADER_X_FORWARDED_FOR);
			System.out.println("request client : " + client);
			if ((client == null) || client.equals("")) {
				client = request.getRemoteAddr();
			}
			String username = getUsername();
			if (username != null) {
				client = username + "@" + client;
			}
		}
		//MDC.put(MDC_KEY_CLIENT, client);
		logger.debug("client request: " + request.getRequestURI());
		System.out.println("client : " + request.getRequestURI());
		chain.doFilter(request, response);
		logger.debug("client request done");
		//MDC.remove(MDC_KEY_CLIENT);
	}
	
	private String getUsername() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				return (String)authentication.getName();
			}
		} catch (Exception e) {
			logger.debug("getUsername fail: " + e.getMessage());
		}
		return null;
	}

	public void destroy() {
	}

}
