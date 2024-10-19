package com.example.gym.services;

import com.example.gym.dao.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
}
