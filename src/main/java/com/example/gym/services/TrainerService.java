package com.example.gym.services;

import com.example.gym.dtos.TrainerRegistrationRequestDTO;
import com.example.gym.dtos.TrainingInfoResponseDTO;
import com.example.gym.dtos.UpdateTrainerRequestDTO;
import com.example.gym.dtos.UserRegistrationResponseDTO;
import com.example.gym.exceptions.UsernameAlreadyExistsException;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.mappers.TrainingMapper;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrainingMapper trainingMapper;
    private final TrainerMapper trainerMapper;
    private final UserRepository userRepository;

    private static final SecureRandom random = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public TrainerService(TrainerRepository trainerRepository, PasswordEncoder passwordEncoder, TrainingMapper trainingMapper, TrainerMapper trainerMapper, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.trainingMapper = trainingMapper;
        this.trainerMapper = trainerMapper;
        this.userRepository = userRepository;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Transactional
    public UserRegistrationResponseDTO createTrainerProfile(TrainerRegistrationRequestDTO trainer) {
        logger.info("starting createTraineeProfile...");
        String username = generateUsername(trainer.getFirstName(),trainer.getLastName());
        String password = generatePassword();
        if(userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException("Username is already taken.");
        };
        Trainer createdTrainer = trainerMapper.toTrainerFromRequest(trainer);
        createdTrainer.setUsername(username);
        createdTrainer.setPassword(passwordEncoder.encode(password));
        createdTrainer.setActive(true);
        logger.info("saving trainee with username {}", createdTrainer.getUsername());
        UserRegistrationResponseDTO response = trainerMapper.toRegistrationResponse(trainerRepository.save(createdTrainer));
        response.setPassword(password);
        return response;
    }

    @Transactional
    public Optional<Trainer> updateTrainerProfile(String username, UpdateTrainerRequestDTO trainer) {
        logger.info("searching trainer with username: {}", username);
        return trainerRepository.findByUsername(username)
                .map(existingTrainer -> {
                    existingTrainer.setFirstName(trainer.getFirstName());
                    existingTrainer.setLastName(trainer.getLastName());
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTrainingType(Specialization.valueOf(trainer.getSpecialization()));
                    existingTrainer.setTrainingType(trainingType);
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
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<List<TrainingInfoResponseDTO>> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        logger.info("searching trainer with username: {}", username);
        return trainerRepository.findByUsername(username)
                .map(trainee -> trainee.getTrainingList().stream()
                        .filter(training ->
                                (fromDate == null || !training.getTrainingDate().isBefore(fromDate)) &&
                                        (toDate == null || !training.getTrainingDate().isAfter(toDate)) &&
                                        (traineeName == null || training.getTrainee().getUsername().equals(traineeName))
                        )
                        .map(training -> trainingMapper.toTrainingInfoResponse(training))
                        .toList()
                );
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
    public void activate(String username, Boolean isActive){
        logger.info("updating trainer with username: {}", username);
        trainerRepository.findByUsername(username)
                .map(trainer -> {
                    trainer.setActive(isActive);
                    return trainerRepository.save(trainer);
                }).orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
    }

    @Transactional
    public List<Trainer> findAllByIds(List<Long> trainerIds){
        logger.info("searching all trainers by Ids");
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
    private String generateUsername(String firstName, String lastName){
        return firstName + "." + lastName;
    }
    private String generatePassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return password.toString();
    }
}
