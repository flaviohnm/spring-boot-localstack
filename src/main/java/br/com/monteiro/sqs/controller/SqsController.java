package br.com.monteiro.sqs.controller;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SqsController {

    @Value("${sqsQueueName}")
    private String queueName;

    @Autowired
    private SqsTemplate sqsTemplate;

    @GetMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestParam("message") String message) {
        sqsTemplate.send(queueName,message);
        return ResponseEntity.ok().build();
    }

}
