package com.example.gym;

import com.example.gym.dtos.TraineeUserDTO;
import com.example.gym.models.Trainee;
import com.example.gym.models.User;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.repositories.UserRepository;
import com.example.gym.services.TraineeService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserRepository userRepository;





    @Test
    @DisplayName("testing creating a new trainee")
    public void testSaveTrainee(){
        TraineeUserDTO traineeUserDTO = new TraineeUserDTO("John", "Doe", "", "1234", true, null, "test", 1l);
        User user = new User();
        user.setId(1L);
        user.setFirstName(traineeUserDTO.getFirstName());
        user.setLastName(traineeUserDTO.getLastName());

        Trainee trainee = new Trainee();
        trainee.setId(2L);
        trainee.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        TraineeUserDTO savedTraineeUserDTO = traineeService.createTraineeProfile(traineeUserDTO);
        assertNotNull(savedTraineeUserDTO);
        assertEquals(savedTraineeUserDTO.getUsername(), "John.Doe");
        assertNotEquals(savedTraineeUserDTO.getPassword(), "1234");
    }

    @Test
    @DisplayName("Find a trainee by id")
    public void testFindTraineeById() {
        Trainee trainee = new Trainee();
//        trainee.setId(1);
//        trainee.setFirstName("Juan");
//        trainee.setLastName("Perez");

//        when(traineeDAO.findById(1)).thenReturn(Optional.of(trainee));
//
        Optional<Trainee> foundTrainee = traineeService.selectTraineeProfile(1);

        assertTrue(foundTrainee.isPresent());
//        assertEquals("Juan", foundTrainee.get().getFirstName());
    }

}
