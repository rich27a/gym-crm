package com.example.gym;

import com.example.gym.models.Trainer;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import static org.junit.Assert.*;


public class TrainerServiceTest {
    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    private Trainer trainer;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setActive(true);
    }

    @Test
    public void testCreateTrainer(){
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        Trainer savedTrainer = trainerService.createTrainerProfile(trainer);
        assertNotNull(savedTrainer);
        assertEquals(savedTrainer.getUsername(), "John.Doe");
        assertNotEquals(savedTrainer.getPassword(), "1234");
    }

    @Test
    public void testFindTrainerById() {

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.selectTrainerProfile(1L);

        assertTrue(foundTrainer.isPresent());
//        assertEquals("Juan", foundTrainer.get().getFirstName());
    }
}
