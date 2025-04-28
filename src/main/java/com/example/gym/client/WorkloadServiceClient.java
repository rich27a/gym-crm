package com.example.gym.client;

import com.example.gym.dtos.WorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "workload-service", url = "localhost:8090")
public interface WorkloadServiceClient {

    @PostMapping("/api/workload")
    ResponseEntity<?> processWorkload(@RequestBody WorkloadRequest request,
                                      @RequestHeader("Authorization") String authHeader,
                                      @RequestHeader("X-Transaction-ID") String transactionId);
}
