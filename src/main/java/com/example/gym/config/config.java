package com.example.gym.config;

import com.example.gym.models.*;
import com.example.gym.utils.LineToModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@PropertySource("classpath:application.properties")
public class config {
    private static final Logger logger = LoggerFactory.getLogger(config.class);
//    @Value("${data.path}")
    private String dataPath;

    @Bean
    public Map<Integer, Trainer> trainerMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<Integer, Trainee> traineeMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<Integer, Training> trainingMap() {
        return new HashMap<>();
    }



//    @Bean
    public InitializingBean initializingBeans(Map<Integer, Trainer> trainerMap, Map<Integer, Trainee> traineeMap, Map<Integer, Training> trainingMap){
        return () -> {
            try {
                logger.info("Trying to read file: " + dataPath);
                Files.lines(Paths.get(dataPath)).forEach(line -> {
                    String[] data = line.split(",");
                    if(data[0].equalsIgnoreCase("Trainer")){
                        Trainer trainer = new Trainer();
//                        LineToModelParser.parseLineToUser(trainer, data);
//                        trainer.setSpecialization(Specialization.valueOf(data[7]));
//                        trainerMap.put(trainer.getId(), trainer);
                        logger.debug("trainer: " + trainer.getId() + " successfully saved...");
                    }else if (data[0].equalsIgnoreCase("Trainee")){
                        Trainee trainee = new Trainee();
//                        LineToModelParser.parseLineToUser(trainee, data);
//                        trainee.setDateOfBirth(data[7]);
//                        trainee.setAddress(data[8]);
//                        traineeMap.put(trainee.getId(), trainee);
                        logger.debug("trainee: " + trainee.getId() + " successfully saved...");
                    }else if(data[0].equalsIgnoreCase("Training")){
                        Training training = new Training();
                        LineToModelParser.parseLineToTraining(training, data);
//                        trainingMap.put(training.getTrainingId(), training);
//                        logger.debug("training: " + training.getTrainingId() + " successfully saved");
                    }

                });

            }catch (IOException exception){
                logger.error("Error while reading the file " + dataPath);
                exception.printStackTrace();
            }
            logger.info("trainee map " + traineeMap);
        };

    }

}
