package com.example.gym.repositories;

import com.example.gym.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    public Optional<Trainer> findByUsername(String username);
}
