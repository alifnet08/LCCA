package com.wo.module.api.notary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wo.module.api.security.BearerTokenInterceptor;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.service.NotaryService;

public class NotaryContactRestControllerTest {

	private static final String TOKEN = "test-token";

	private final List<Object[]> updateCalls = new ArrayList<Object[]>();
	private Notary contactResult;
	private NotaryContactRestController controller;
	private BearerTokenInterceptor interceptor;

	@Before
	public void setUp() {
		updateCalls.clear();
		contactResult = null;
		NotaryService notaryService = (NotaryService) Proxy.newProxyInstance(
				NotaryService.class.getClassLoader(),
				new Class<?>[] { NotaryService.class },
				(proxy, method, args) -> {
					if ("updateEmailByPhone".equals(method.getName())) {
						updateCalls.add(args);
						return contactResult;
					}
					return defaultValue(method.getReturnType());
				});
		controller = new NotaryContactRestController(notaryService);
		interceptor = new BearerTokenInterceptor(TOKEN);
	}

	@Test
	public void updatesPhoneAndEmailWhenBearerTokenMatches() throws Exception {
		RecordingResponse response = new RecordingResponse();
		Assert.assertTrue(interceptor.preHandle(requestWithHeader("Bearer " + TOKEN), response.asServletResponse(), new Object()));

		Notary notary = new Notary();
		notary.setNotaryId(15L);
		notary.setNotaryName("Notaris Contoh");
		notary.setPhoneNo("0215550101");
		notary.setEmail("notaris@example.com");
		notary.setLastUpdateBy("API");
		notary.setLastUpdateDate(new Timestamp(new Date().getTime()));
		contactResult = notary;

		NotaryContactResponse body = controller.updateContact(request("0215550101", "notaris@example.com"));

		Assert.assertEquals(Long.valueOf(15L), body.getNotaryId());
		Assert.assertEquals("Notaris Contoh", body.getNotaryName());
		Assert.assertEquals("0215550101", body.getPhoneNo());
		Assert.assertEquals("notaris@example.com", body.getEmail());
		Assert.assertEquals("API", body.getLastUpdateBy());
		Assert.assertNotNull(body.getLastUpdateDate());
		Assert.assertEquals(1, updateCalls.size());
		Assert.assertEquals("0215550101", updateCalls.get(0)[0]);
		Assert.assertEquals("notaris@example.com", updateCalls.get(0)[1]);
	}

	@Test
	public void rejectsMissingBearerToken() throws Exception {
		RecordingResponse response = new RecordingResponse();
		Assert.assertFalse(interceptor.preHandle(requestWithHeader(null), response.asServletResponse(), new Object()));
		Assert.assertEquals(401, response.status);
		Assert.assertTrue(response.body.toString().contains("Bearer token is missing or invalid"));
		Assert.assertTrue(updateCalls.isEmpty());
	}

	@Test
	public void rejectsWrongBearerToken() throws Exception {
		RecordingResponse response = new RecordingResponse();
		Assert.assertFalse(interceptor.preHandle(requestWithHeader("Bearer wrong-token"), response.asServletResponse(), new Object()));
		Assert.assertEquals(401, response.status);
		Assert.assertTrue(updateCalls.isEmpty());
	}

	@Test
	public void returnsNotFoundWhenPhoneDoesNotMatch() {
		try {
			controller.updateContact(request("0210000000", "notaris@example.com"));
			Assert.fail("Expected notary to be missing");
		} catch (NotaryContactNotFoundException exception) {
			Assert.assertEquals("No notary was found for phone number 0210000000", exception.getMessage());
		}
	}

	@Test
	public void rejectsInvalidEmail() {
		try {
			controller.updateContact(request("0215550101", "bukan-email"));
			Assert.fail("Expected invalid email");
		} catch (NotaryContactValidationException exception) {
			Assert.assertEquals("email format is invalid", exception.getMessage());
		}
		Assert.assertTrue(updateCalls.isEmpty());
	}

	@Test
	public void validatorTrimsContactFields() {
		NotaryContactUpdateRequest request = request(" 021-555 ", " Notaris@Example.com ");

		Assert.assertNull(NotaryContactValidator.validate(request));
		Assert.assertEquals("021-555", request.getPhoneNo());
		Assert.assertEquals("Notaris@Example.com", request.getEmail());
	}

	private static NotaryContactUpdateRequest request(String phoneNo, String email) {
		NotaryContactUpdateRequest request = new NotaryContactUpdateRequest();
		request.setPhoneNo(phoneNo);
		request.setEmail(email);
		return request;
	}

	private static HttpServletRequest requestWithHeader(String authorization) {
		return (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(),
				new Class<?>[] { HttpServletRequest.class },
				(proxy, method, args) -> {
					if ("getHeader".equals(method.getName())) {
						return "Authorization".equals(args[0]) ? authorization : null;
					}
					if ("setCharacterEncoding".equals(method.getName())) {
						return null;
					}
					return defaultValue(method.getReturnType());
				});
	}

	private static Object defaultValue(Class<?> returnType) {
		if (!returnType.isPrimitive()) {
			return null;
		}
		if (boolean.class.equals(returnType)) {
			return false;
		}
		if (long.class.equals(returnType)) {
			return 0L;
		}
		if (int.class.equals(returnType)) {
			return 0;
		}
		if (short.class.equals(returnType)) {
			return (short) 0;
		}
		if (byte.class.equals(returnType)) {
			return (byte) 0;
		}
		if (double.class.equals(returnType)) {
			return 0d;
		}
		if (float.class.equals(returnType)) {
			return 0f;
		}
		return '\0';
	}

	private static final class RecordingResponse {
		private int status;
		private final StringWriter body = new StringWriter();
		private final PrintWriter writer = new PrintWriter(body, true);

		private HttpServletResponse asServletResponse() {
			return (HttpServletResponse) Proxy.newProxyInstance(
					HttpServletResponse.class.getClassLoader(),
					new Class<?>[] { HttpServletResponse.class },
					(proxy, method, args) -> {
						if ("setStatus".equals(method.getName())) {
							status = ((Integer) args[0]).intValue();
							return null;
						}
						if ("getWriter".equals(method.getName())) {
							return writer;
						}
						return defaultValue(method.getReturnType());
					});
		}
	}
}
