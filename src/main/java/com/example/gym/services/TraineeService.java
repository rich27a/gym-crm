package com.example.gym.services;

import com.example.gym.dao.TraineeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;



}
