package com.example.gym.repositories;

import com.example.gym.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    public Optional<Trainer> findByUsername(String username);
@Query("SELECT t FROM Trainer t WHERE t NOT IN (SELECT trainers FROM Trainee tr JOIN tr.trainers trainers WHERE tr.username = :username)")
List<Trainer> findTrainersNotAssignedToTrainee(@Param("username") String username);
}
