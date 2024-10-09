package com.pabu5h.comtrade;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ComtradeApplication {
	@Autowired
	private ComtradeReader comtradeReader;

	public static void main(String[] args) {
		SpringApplication.run(ComtradeApplication.class, args);

	}
	@PostConstruct
	public void init() {
		try {
			comtradeReader.readComtradeConfig("src/main/resources/test.cfg", "src/main/resources/test.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
