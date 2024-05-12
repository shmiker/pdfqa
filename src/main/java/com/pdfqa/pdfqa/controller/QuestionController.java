package com.pdfqa.pdfqa.controller;

import com.pdfqa.pdfqa.Question;
import com.pdfqa.pdfqa.service.PdfReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    // Swagger UI http://localhost:8080/swagger-ui/index.html

    @Autowired
    PdfReaderService pdfReaderService;

    @GetMapping("/question/{key}")
    public ResponseEntity<Question> question(@PathVariable("key") String key) {
        Question mostSimilarQuestion = pdfReaderService.findMostSimilarQuestion(key);
        return new ResponseEntity<>(mostSimilarQuestion, HttpStatus.OK);
    }
}