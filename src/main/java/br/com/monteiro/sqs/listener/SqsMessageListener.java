package br.com.monteiro.sqs.listener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqsMessageListener {

    @SqsListener("${sqsQueueName}")
    public void queueListener(String message) {
        try {
            log.info(message);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
