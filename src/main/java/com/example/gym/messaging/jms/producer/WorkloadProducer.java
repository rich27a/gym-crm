package com.example.gym.messaging.jms.producer;


import com.example.gym.constants.JmsConstants;
import com.example.gym.messaging.jms.model.WorkloadMessage;
import com.example.gym.models.ActionType;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
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

    public void sendWorkloadNotification(Training training, ActionType actionType, String transactionId) {
        Trainer trainer = training.getTrainer();

        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        log.info("[Transaction: {}] Sending workload message for trainer: {} with action: {}",
                transactionId, trainer.getUsername(), actionType);

        WorkloadMessage message = buildWorkloadMessage(training, trainer, actionType);

        final String finalTransactionId = transactionId;
        jmsTemplate.convertAndSend(JmsConstants.WORKLOAD_QUEUE, message, jmsMessage -> {
            jmsMessage.setStringProperty(JmsConstants.TRANSACTION_ID_PROPERTY, finalTransactionId);
            return jmsMessage;
        });

        log.info("[Transaction: {}] Successfully sent workload message to queue", transactionId);
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
}
