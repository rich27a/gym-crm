package com.example.gym.services;

import com.example.gym.client.WorkloadServiceClient;
import com.example.gym.dtos.WorkloadRequest;
import com.example.gym.messaging.jms.producer.WorkloadProducer;
import com.example.gym.models.ActionType;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.utils.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class WorkloadNotificationService {


    private final WorkloadServiceClient workloadClient;
    private final JwtUtil jwtUtil;
    private final WorkloadProducer workloadProducer;

    public WorkloadNotificationService(WorkloadServiceClient workloadClient, JwtUtil jwtUtil, WorkloadProducer workloadProducer) {
        this.workloadClient = workloadClient;
        this.jwtUtil = jwtUtil;
        this.workloadProducer = workloadProducer;
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "workloadServiceFallback")
    @Retry(name = "workloadService")
    public void notifyTrainingAdded(Training training, String transactionId) {
        Trainer trainer = training.getTrainer();

        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        log.info("[Transaction: {}] Notifying workload service about new training for trainer: {}",
                transactionId, trainer.getUsername());

        WorkloadRequest request = buildWorkloadRequest(training, trainer, ActionType.ADD);
        String token = "Bearer " + jwtUtil.generateToken(trainer.getUsername());

        workloadProducer.sendWorkloadNotification(training, ActionType.ADD, transactionId);

        log.info("[Transaction: {}] Successfully notified workload service", transactionId);
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "workloadServiceFallback")
    @Retry(name = "workloadService")
    public void notifyTrainingDeleted(Training training, String transactionId) {
        Trainer trainer = training.getTrainer();

        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        log.info("[Transaction: {}] Notifying workload service about deleted training for trainer: {}",
                transactionId, trainer.getUsername());

        WorkloadRequest request = buildWorkloadRequest(training, trainer, ActionType.DELETE);
        String token = "Bearer " + jwtUtil.generateToken(trainer.getUsername());

        workloadClient.processWorkload(request, token, transactionId);

        log.info("[Transaction: {}] Successfully notified workload service", transactionId);
    }

    private WorkloadRequest buildWorkloadRequest(Training training, Trainer trainer, ActionType actionType) {
        return WorkloadRequest.builder()
                .trainerUsername(trainer.getUsername())
                .trainerFirstName(trainer.getFirstName())
                .trainerLastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(actionType)
                .build();
    }

    public void workloadServiceFallback(Training training, String transactionId, Exception ex) {
        log.error("[Transaction: {}] Failed to notify workload service. Error: {}",
                transactionId, ex.getMessage());
        saveForRetry(training, transactionId, ex);
    }

    private void saveForRetry(Training training, String transactionId, Exception ex) {
        log.info("[Transaction: {}] Saved failed notification for retry", transactionId);
    }
}
