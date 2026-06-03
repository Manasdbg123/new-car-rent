package com.carrental.scheduler;

import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryCleanupScheduler {

    private final CarRepository carRepository;

    @Scheduled(fixedRate = 300000) // Runs every 5 minutes
    @Transactional
    public void releaseExpiredInventoryLocks() {
        log.info("Running background job: Cleaning up expired inventory locks...");

        LocalDateTime now = LocalDateTime.now();
        var expiredCars = carRepository.findAll().stream()
                .filter(car -> car.getLockedUntil() != null && car.getLockedUntil().isBefore(now))
                .toList();

        if (!expiredCars.isEmpty()) {
            expiredCars.forEach(car -> {
                car.setLockedUntil(null);
                car.setLockedBy(null);
            });
            carRepository.saveAll(expiredCars);
            log.info("Successfully released {} abandoned vehicles back to the public pool.", expiredCars.size());
        } else {
            log.info("No expired locks found. Inventory is clean.");
        }
    }
}