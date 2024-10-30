package com.example.gym;

import com.example.gym.dao.TraineeDAO;
import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    @Test
    public void testCreateTrainee(){
        Mockito.doNothing().when(traineeDAO).save(Mockito.any(Trainee.class));

        Trainee result = traineeService.createTraineeProfile("Juan", "Perez", "25-02-1995", "Calle paz #256");

        Mockito.verify(traineeDAO).save(Mockito.any(Trainee.class));

//        assertEquals("Juan", result.getFirstName());
//        assertEquals("Perez", result.getLastName());
    }

    @Test
    public void testFindTraineeById() {
        Trainee trainee = new Trainee();
//        trainee.setId(1);
//        trainee.setFirstName("Juan");
//        trainee.setLastName("Perez");

        Mockito.when(traineeDAO.findById(1)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.selectTraineeProfile(1);

        assertTrue(foundTrainee.isPresent());
//        assertEquals("Juan", foundTrainee.get().getFirstName());
    }

}
