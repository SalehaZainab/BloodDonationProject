package com.example.BloodDonationProject.controller;

import com.example.BloodDonationProject.dto.UserResponse;
import com.example.BloodDonationProject.dto.UserUpdateRequest;
import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.security.RequireAuth;
import com.example.BloodDonationProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequireAuth // Apply authentication to all endpoints in this controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<UserResponse>> getUsersByCity(@PathVariable String city) {
        List<UserResponse> users = userService.getUsersByCity(city);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by blood group
     */
    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<UserResponse>> getUsersByBloodGroup(@PathVariable BloodGroup bloodGroup) {
        List<UserResponse> users = userService.getUsersByBloodGroup(bloodGroup);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by city and blood group
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam String city,
            @RequestParam BloodGroup bloodGroup) {
        List<UserResponse> users = userService.getUsersByCityAndBloodGroup(city, bloodGroup);
        return ResponseEntity.ok(users);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
