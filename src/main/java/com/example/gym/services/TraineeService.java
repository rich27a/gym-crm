package com.example.gym.services;

import com.example.gym.dao.TraineeDAO;
import com.example.gym.models.Trainee;
import com.example.gym.utils.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;
    private static Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public Trainee createTraineeProfile(String firstName, String lastName,
                                        String dateOfBirth, String address) {
        String username = Profile.generateUsername(firstName, lastName);
        String password = Profile.generatePassword();
        UUID uuid = UUID.randomUUID();
        int id = uuid.hashCode();
        logger.info("generating id: " + id);
        Trainee trainee = new Trainee(id, firstName, lastName, username, password,
                true, dateOfBirth, address);
        traineeDAO.save(trainee);
        logger.info("trainee with id: " + id + "successfully created...");
        return trainee;
    }

    public Optional<Trainee> updateTraineeProfile(int id, String firstName, String lastName,
                                                  String dateOfBirth, String address) {
        Optional<Trainee> traineeOpt = traineeDAO.findById(id);
        if (traineeOpt.isPresent()) {
            Trainee trainee = traineeOpt.get();
            trainee.setFirstName(firstName);
            trainee.setLastName(lastName);
            trainee.setAddress(address);
            trainee.setDateOfBirth(dateOfBirth);
            traineeDAO.save(trainee);
            logger.info("trainee with id: " + id + " successfully updated");
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

}
