package example.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RestApi {
	@Data @AllArgsConstructor public static class Response { String message; }

	@GetMapping(value = "/api/greeting/{input}")
	@PreAuthorize("hasRole('ROLE_ANOTHER')")
	public Response sendMessage(@PathVariable String input, Principal principal) {
		return new Response("Successfully sent: "+input + " by: "+principal);
	}

	@GetMapping(value = "/api/notallowed/{input}")
	@PreAuthorize("hasRole('ROLE_ALLOWED')")
	public Response notAllowed(@PathVariable String input, Principal principal) {
		return new Response("Successfully sent: "+input + " by: "+principal);
	}
}
