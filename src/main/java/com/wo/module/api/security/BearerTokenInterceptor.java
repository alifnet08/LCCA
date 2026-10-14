package com.wo.module.api.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BearerTokenInterceptor implements HandlerInterceptor {

	static final String AUTHORIZATION = "Authorization";
	static final String BEARER_PREFIX = "Bearer ";
	static final String ENV_TOKEN = "NOTARY_API_BEARER_TOKEN";
	private static final String TOKEN_PROPERTY = "notary.api.bearer.token";
	private static final Logger logger = LoggerFactory.getLogger(BearerTokenInterceptor.class);

	private final String expectedToken;

	public BearerTokenInterceptor() {
		this(resolveConfiguredToken());
	}

	public BearerTokenInterceptor(String expectedToken) {
		this.expectedToken = StringUtils.trimToEmpty(expectedToken);
		if (this.expectedToken.isEmpty()) {
			logger.error("Notary API bearer token is empty. Set {} or {} in api.properties", ENV_TOKEN, TOKEN_PROPERTY);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String presented = extractBearerToken(request.getHeader(AUTHORIZATION));
		if (!matches(presented)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setHeader("WWW-Authenticate", "Bearer");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(
					"{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Bearer token is missing or invalid\"}");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
	}

	static String extractBearerToken(String authorizationHeader) {
		if (authorizationHeader == null) {
			return null;
		}
		String header = authorizationHeader.trim();
		if (header.length() <= BEARER_PREFIX.length() || !header.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
			return null;
		}
		return header.substring(BEARER_PREFIX.length()).trim();
	}

	private boolean matches(String presented) {
		if (expectedToken.isEmpty() || StringUtils.isBlank(presented)) {
			return false;
		}
		byte[] expectedHash = sha256(expectedToken);
		byte[] presentedHash = sha256(presented);
		return MessageDigest.isEqual(expectedHash, presentedHash);
	}

	private static byte[] sha256(String value) {
		try {
			return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
		} catch (Exception exception) {
			throw new IllegalStateException("SHA-256 is not available", exception);
		}
	}

	private static String resolveConfiguredToken() {
		String fromEnv = System.getenv(ENV_TOKEN);
		if (StringUtils.isNotBlank(fromEnv)) {
			return fromEnv.trim();
		}
		InputStream input = BearerTokenInterceptor.class.getClassLoader().getResourceAsStream("api.properties");
		if (input == null) {
			return "";
		}
		try {
			Properties properties = new Properties();
			properties.load(input);
			return StringUtils.trimToEmpty(properties.getProperty(TOKEN_PROPERTY));
		} catch (IOException exception) {
			logger.error("Could not read api.properties", exception);
			return "";
		} finally {
			try {
				input.close();
			} catch (IOException exception) {
				logger.error("Could not close api.properties", exception);
			}
		}
	}
}
