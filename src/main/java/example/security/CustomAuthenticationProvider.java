package example.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class CustomAuthenticationProvider implements AuthenticationProvider {
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var customToken = authentication.getPrincipal();
		if (authentication.getPrincipal() != null) {
			return new UsernamePasswordAuthenticationToken(customToken, null,
				Arrays.asList(
					new SimpleGrantedAuthority("ROLE_ANOTHER"),
					new SimpleGrantedAuthority("ROLE_ADMIN")));
		} else {
			throw new AuthenticationException("No principal provided in authentication header") {};
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.equals(authentication);
	}
}