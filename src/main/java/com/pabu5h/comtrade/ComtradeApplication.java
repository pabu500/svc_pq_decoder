package com.pabu5h.comtrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComtradeApplication {
	@Autowired
	private ComtradeModule comtradeModule;

	public static void main(String[] args) {
		SpringApplication.run(ComtradeApplication.class, args);

	}


}
