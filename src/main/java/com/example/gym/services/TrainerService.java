package com.example.gym.services;

import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(TrainerRepository trainerRepository, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Transactional
    public Trainer createTrainerProfile(Trainer trainer) {
        logger.info("starting createTraineeProfile...");
        String username = Profile.generateUsername(trainer.getFirstName(),trainer.getLastName());
        String password = Profile.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        logger.info("saving trainee with id {}", trainer.getId());
        return trainerRepository.save(trainer);
    }

    @Transactional
    public Optional<Trainer> updateTrainerProfile(Trainer trainer) {
        logger.info("searching trainer with id: {}", trainer.getId());
        return trainerRepository.findById(trainer.getId())
                .map(existingTrainer -> {
                    existingTrainer.setFirstName(trainer.getFirstName());
                    existingTrainer.setLastName(trainer.getLastName());
                    existingTrainer.setTrainingType(trainer.getTrainingType());
                    existingTrainer.setUsername(Profile.generateUsername(existingTrainer.getFirstName(), existingTrainer.getLastName()));
                    return trainerRepository.save(existingTrainer);
                });
    }

    @Transactional
    public Optional<Trainer> findTrainerByUsername(String username){
        logger.info("searching for trainer with username {}", username);
        return trainerRepository.findByUsername(username);
    }
    @Transactional
    public boolean deleteTrainerByUsername(String username) {
        logger.info("searching trainer with username: {}", username);

        return trainerRepository.findByUsername(username)
                .map(trainer -> {
                    trainerRepository.delete(trainer);
                    logger.info("Trainer with username {} successfully deleted", username);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        logger.info("searching trainer with username: {}", username);
        return trainerRepository.findByUsername(username)
                .map(trainee -> trainee.getTrainingList().stream()
                        .filter(training ->
                                (fromDate == null || !training.getTrainingDate().isBefore(fromDate)) &&
                                        (toDate == null || !training.getTrainingDate().isAfter(toDate)) &&
                                        (traineeName == null || training.getTrainee().getUsername().equals(traineeName))
                        )
                        .collect(Collectors.toList())
                )
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + username));
    }

    @Transactional
    public List<Trainer> getUnassignedTrainersFromTraineeUsername(String traineeUsername) {
        logger.info("searching unassigned trainee: {}", traineeUsername);

        return trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
    }
    @Transactional
    public Boolean changePassword(String username, String newPassword, String oldPassword){
        logger.info("updating trainer with username: {}", username);

        return trainerRepository.findByUsername(username).map(
                trainee -> {
                    if (passwordEncoder.matches(oldPassword, trainee.getPassword())) {
                        trainee.setPassword(passwordEncoder.encode(newPassword));
                        trainerRepository.save(trainee);
                        return true;
                    }else {
                        return false;
                    }
                }
        ).orElseGet(() -> false);
    }
    @Transactional
    public void activate(Long id){
        logger.info("updating trainer with id: {}", id);
        trainerRepository.findById(id)
                .map(trainer -> {
                    trainer.setActive(!trainer.isActive());
                    return trainerRepository.save(trainer);
                }).orElseThrow(() -> new RuntimeException("Trainer not found"));
    }

    @Transactional
    public List<Trainer> findAllByIds(List<Long> trainerIds){
        logger.info("searching all trainers by Ids ");

        return trainerRepository.findAllById(trainerIds);
    }
    @Transactional
    public Optional<Trainer> selectTrainerProfile(Long id) {
        logger.info("searching trainer with id: {}", id);
        return trainerRepository.findById(id);
    }

    @Transactional
    public List<Trainer> getTrainers(){
        logger.info("searching all trainers");
        return trainerRepository.findAll();
    }
}
