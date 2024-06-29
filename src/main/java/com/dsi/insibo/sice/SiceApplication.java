package com.dsi.insibo.sice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@SpringBootApplication
public class SiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SiceApplication.class, args);
	}

	@GetMapping("/")
	public String holamundo( Model model) {
		model.addAttribute("titulo", "Inicio");
		return "home";
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SiceApplication.class);
	}
	
}
