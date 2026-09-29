package com.whiteopen.web.util.gzip;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter previously provided by the unpublished artifact
 * com.whiteopen.web:gzip-util:0.0.1.
 */
public class GZIPFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String acceptEncoding = httpRequest.getHeader("accept-encoding");
		if (acceptEncoding != null && acceptEncoding.indexOf("gzip") != -1) {
			GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(httpResponse);
			chain.doFilter(request, wrappedResponse);
			wrappedResponse.finishResponse();
			return;
		}
		chain.doFilter(request, response);
	}
}
