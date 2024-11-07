package com.example.gym.services;

import com.example.gym.models.CustomUserDetails;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.repositories.TrainerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    public CustomUserDetailsService(TrainerRepository trainerRepository, TraineeRepository traineeRepository) {
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return traineeRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .or(() -> trainerRepository.findByUsername(username).map(CustomUserDetails::new))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
