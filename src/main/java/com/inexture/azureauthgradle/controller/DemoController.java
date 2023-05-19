package com.inexture.azureauthgradle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	
	@GetMapping("/")
	public ResponseEntity<String> welcome(){
		return new ResponseEntity<String>("Welcome", HttpStatus.OK);
	}
	
	@GetMapping("/home")
	public ResponseEntity<String> homepage(){
		return new ResponseEntity<String>("Welcome to homepage", HttpStatus.OK);
	}

}
