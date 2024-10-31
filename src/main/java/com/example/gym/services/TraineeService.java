package com.example.gym.services;

import com.example.gym.dao.TraineeDAO;
import com.example.gym.dtos.TraineeUserDTO;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.User;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.repositories.UserRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;

    public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }


    private static Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Transactional
    public TraineeUserDTO createTraineeProfile(TraineeUserDTO traineeUserDTO) {
        logger.debug("starting createTraineProfile...");
        User user = createUser(traineeUserDTO);
        Trainee trainee = createTrainee(traineeUserDTO, user);
        traineeUserDTO.setUserId(user.getId());
        return traineeUserDTO;
    }

    public Optional<Trainee> updateTraineeProfile(int id, String firstName, String lastName,
                                                  String dateOfBirth, String address) {
        Optional<Trainee> traineeOpt = traineeDAO.findById(id);
        if (traineeOpt.isPresent()) {
            Trainee trainee = traineeOpt.get();
//            trainee.setFirstName(firstName);
//            trainee.setLastName(lastName);
//            trainee.setAddress(address);
//            trainee.setDateOfBirth(dateOfBirth);
            traineeDAO.save(trainee);
            logger.debug("trainee with id: {}" , id + " successfully updated");
        }else {
            logger.warn("trainee with id: " + id + " not found");
        }
        return traineeOpt;
    }

    public void deleteTraineeProfile(int id) {
        traineeDAO.delete(id);
    }

    public Optional<Trainee> selectTraineeProfile(int id) {
        return traineeDAO.findById(id);
    }

    private User createUser(TraineeUserDTO traineeUserDTO){
        User user = new User();
        user.setFirstName(traineeUserDTO.getFirstName());
        user.setLastName(traineeUserDTO.getLastName());
        user.setUsername(Profile.generateUsername(traineeUserDTO.getFirstName(), traineeUserDTO.getLastName()));
        user.setPassword(Profile.generatePassword());
        user.setActive(traineeUserDTO.isActive());

        traineeUserDTO.setUsername(user.getUsername());
        traineeUserDTO.setPassword(user.getPassword());
        userRepository.save(user);
        logger.debug("trainee with id: {}" , user.getId() + "successfully created...");
        return user;
    }
    private Trainee createTrainee(TraineeUserDTO traineeUserDTO, User user){
        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(traineeUserDTO.getDateOfBirth());
        trainee.setAddress(traineeUserDTO.getAddress());
        trainee.setUser(user);
        traineeRepository.save(trainee);

        logger.debug("trainee with id: {}" , trainee.getId() + "successfully created...");
        return trainee;
    }


}
