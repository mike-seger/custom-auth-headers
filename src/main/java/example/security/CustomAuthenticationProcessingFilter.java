package example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	public CustomAuthenticationProcessingFilter(RequestMatcher requestMatcher,
			AuthenticationManager authenticationManager) {
		super(requestMatcher);
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		var header = request.getHeader("remote-user");
		var token = new PreAuthenticatedAuthenticationToken(header, null);
		return getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	private final ObjectMapper mapper =
		new ObjectMapper()
			.findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

	@Data @AllArgsConstructor
	public static class ErrorResponse {
		LocalDateTime timeStamp; int status; String error; String path; }

	@Override
	protected void unsuccessfulAuthentication(
		HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		mapper.writeValue(response.getWriter(),new ErrorResponse(
			LocalDateTime.now(), HttpServletResponse.SC_UNAUTHORIZED,
			exception.getMessage(), request.getServletPath()
		));
		response.flushBuffer();
	}
}
