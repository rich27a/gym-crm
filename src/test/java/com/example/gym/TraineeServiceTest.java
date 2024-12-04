package com.example.gym;

import com.example.gym.models.Trainee;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeRepository traineeRepository;

    private Trainee trainee;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setActive(true);
    }

//    @Test
//    @DisplayName("testing creating a new trainee")
//    public void testSaveTrainee(){
//
//        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
//
//        Trainee savedTrainee = traineeService.createTraineeProfile(trainee);
//        assertNotNull(savedTrainee);
//        assertEquals(savedTrainee.getUsername(), "John.Doe");
//        assertNotEquals(savedTrainee.getPassword(), "1234");
//    }

//    @Test
//    @DisplayName("Find a trainee by id")
//    public void testFindTraineeById() {
//        trainee.setFirstName("Juan");
//        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));
//        Optional<Trainee> foundTrainee = traineeService.selectTraineeProfile(1L);
//        assertTrue(foundTrainee.isPresent());
//        assertEquals("Juan", foundTrainee.get().getFirstName());
//    }

//    @Test
//    @DisplayName("Return optional empty when trainee is not found")
//    public void testFindTraineeByIdNotFound(){
//        when(traineeRepository.findById(2L)).thenReturn(Optional.empty());
//        Optional<Trainee> foundTrainee = traineeService.selectTraineeProfile(2L);
//        assertTrue(foundTrainee.isEmpty());
//    }
    @Test
    @DisplayName("Find trainee by username")
    public void testFindTraineeByUsername(){
        String username = "John.Doe";
        trainee.setUsername("John.Doe");
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        Optional<Trainee> traineeOptional = traineeService.findTraineeByUsername(username);
        assertEquals(traineeOptional.get().getUsername(), "John.Doe");
    }

    @Test
    @DisplayName("Find trainee by username")
    public void testFindTraineeByUsernameNotFound(){
        String username = "John.Rax";
        trainee.setUsername("John.Doe");
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
        Optional<Trainee> traineeOptional = traineeService.findTraineeByUsername(username);
        assertTrue(traineeOptional.isEmpty());
    }



}
