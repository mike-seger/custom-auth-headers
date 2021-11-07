package example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomAuthenticationProvider customTokenAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable()
			.csrf().disable()
			.formLogin().disable()
			.logout().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authenticationProvider(customTokenAuthenticationProvider)
			.addFilterBefore(getFilter(), AnonymousAuthenticationFilter.class).authorizeRequests()
			.requestMatchers(getRequestMatchers()).authenticated();
	}

	private RequestMatcher getRequestMatchers() {
		return new OrRequestMatcher(new AntPathRequestMatcher("/**"));
	}

	private Filter getFilter() throws Exception {
		return new CustomAuthenticationProcessingFilter(getRequestMatchers(), authenticationManager());
	}
}
