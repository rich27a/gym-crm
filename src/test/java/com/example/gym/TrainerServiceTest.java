package com.example.gym;

import com.example.gym.dao.TrainerDAO;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.services.TrainerService;
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

public class TrainerServiceTest {
    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDAO trainerDAO;

    @Test
    public void testCreateTrainer(){
        Mockito.doNothing().when(trainerDAO).save(Mockito.any(Trainer.class));

        Trainer result = trainerService.createTrainerProfile("Juan", "Perez", Specialization.CARDIO);

        Mockito.verify(trainerDAO).save(Mockito.any(Trainer.class));

//        assertEquals("Juan", result.getFirstName());
//        assertEquals("Perez", result.getLastName());
    }

    @Test
    public void testFindTrainerById() {
        Trainer trainer = new Trainer();
//        trainer.setId(1);
//        trainer.setFirstName("Juan");
//        trainer.setLastName("Perez");

        Mockito.when(trainerDAO.findById(1)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.selectTrainerProfile(1);

        assertTrue(foundTrainer.isPresent());
//        assertEquals("Juan", foundTrainer.get().getFirstName());
    }
}
