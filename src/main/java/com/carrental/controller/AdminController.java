package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')") // Strict Security: Only Admins can access this!
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", bookingRepository.calculateTotalRevenue());
        stats.put("activeBookings", bookingRepository.countActiveBookings());
        stats.put("totalVehicles", carRepository.count());
        stats.put("totalUsers", userRepository.count());

        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .data(stats)
                .timestamp(LocalDateTime.now())
                .build());
    }
}