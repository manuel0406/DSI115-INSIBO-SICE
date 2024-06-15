package com.dsi.insibo.sice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@SpringBootApplication
public class SiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiceApplication.class, args);
	}

	@GetMapping("/home")
	public String holamundo( Model model) {
		model.addAttribute("titulo", "Inicio");
		return "home";
	}
	
}
