package com.example.gym.messaging.jms.producer;


import com.example.gym.constants.JmsConstants;
import com.example.gym.messaging.jms.model.WorkloadMessage;
import com.example.gym.models.ActionType;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class WorkloadProducer {

    private final JmsTemplate jmsTemplate;
    private final JwtUtil jwtUtil;

    public WorkloadProducer(JmsTemplate jmsTemplate, JwtUtil jwtUtil) {
        this.jmsTemplate = jmsTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Value("${gym.activemq.workload.queue}")
    private String workloadQueue;
    @Value("${gym.activemq.workload.dlq}")
    private String workloadDlq;



    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendWorkloadNotification(Training training, ActionType actionType, String transactionId) {
        Trainer trainer = training.getTrainer();

        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        log.info("[Transaction: {}] Sending workload message for trainer: {} with action: {}",
                transactionId, trainer.getUsername(), actionType);

        WorkloadMessage message = buildWorkloadMessage(training, trainer, actionType);

        final String finalTransactionId = transactionId;

        try {
            log.info("Sending message to queue: {} with ID: {}",
                    workloadQueue, finalTransactionId);
            jmsTemplate.convertAndSend(workloadQueue, message, jmsMessage -> {
                jmsMessage.setStringProperty(JmsConstants.TRANSACTION_ID_PROPERTY, finalTransactionId);
                return jmsMessage;
            });

            log.info("Message sent successfully to {}", workloadQueue);
        } catch (JmsException e) {
            log.error("Sending to DLQ [Transaction: {}]", finalTransactionId);
            sendToDeadLetterQueue(message, finalTransactionId, "Unexpected error: " + e.getMessage());
            throw new MessagingException("Message moved to DLQ", e);
        }
    }

    private WorkloadMessage buildWorkloadMessage(Training training, Trainer trainer, ActionType actionType) {
        return WorkloadMessage.builder()
                .trainerUsername(trainer.getUsername())
                .trainerFirstName(trainer.getFirstName())
                .trainerLastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(actionType)
                .authToken("Bearer " + jwtUtil.generateToken(trainer.getUsername()))
                .build();
    }

    private void sendToDeadLetterQueue(WorkloadMessage message, String transactionId, String errorReason) {
        try {
            jmsTemplate.convertAndSend(workloadDlq, message, jmsMessage -> {
                jmsMessage.setStringProperty(JmsConstants.TRANSACTION_ID_PROPERTY, transactionId);
                jmsMessage.setStringProperty("errorReason", errorReason);
                jmsMessage.setStringProperty("originalTimestamp", String.valueOf(System.currentTimeMillis()));
                return jmsMessage;
            });
            log.info("[Transaction: {}] Message sent to dead letter queue", transactionId);
        } catch (Exception e) {
            log.error("[Transaction: {}] Failed to send message to dead letter queue: {}",
                    transactionId, e.getMessage(), e);
        }
    }
}
