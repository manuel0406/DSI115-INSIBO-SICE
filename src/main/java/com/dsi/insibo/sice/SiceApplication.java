package com.dsi.insibo.sice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SpringBootApplication
public class SiceApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SiceApplication.class, args);
	}

	@GetMapping("/")
	public String holamundo( Model model) {
		model.addAttribute("titulo", "Inicio");
		return "home";
	}	
	
	@Autowired
	private SessionRegistry sessionRegistry;
	@GetMapping("/session")
	public ResponseEntity<?> getDetailResponseEntity() {
		String sessionId="";
		User userObject = null;
		List<Object>sessions = sessionRegistry.getAllPrincipals();

		for(Object session :sessions){
			if (session instanceof User) {
				userObject = (User) session;
			}
			// Usuario a recuperar y no incluimos las sesiones expiradas
			List <SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
			for(SessionInformation sessionInformation : sessionInformations){
				sessionId = sessionInformation.getSessionId();
			}

		}

		Map<String, Object> response= new HashMap<>();	
		response.put("response", "Hello World");
		response.put("Session ID", sessionId);
		response.put("sessionUser", userObject);

		return ResponseEntity.ok(response);
	}
}
