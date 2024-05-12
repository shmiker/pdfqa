package com.pdfqa.pdfqa;

import com.pdfqa.pdfqa.service.PdfReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfqaApplication implements CommandLineRunner {

	private static final String FILE_PATH = "src/main/resources/QAfile.pdf";

	@Autowired
	private PdfReaderService pdfReaderService;

	public static void main(String[] args) {
		SpringApplication.run(PdfqaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		pdfReaderService.mapContent(FILE_PATH);
	}
}
