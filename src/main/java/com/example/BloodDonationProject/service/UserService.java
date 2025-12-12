package com.example.BloodDonationProject.service;

import com.example.BloodDonationProject.dto.UserRegistrationRequest;
import com.example.BloodDonationProject.dto.UserResponse;
import com.example.BloodDonationProject.dto.UserUpdateRequest;
import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Hash password in auth module
        user.setPhone(request.getPhone());
        user.setCity(request.getCity());
        user.setBloodGroup(request.getBloodGroup());

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllByDeletedAtIsNull().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByCity(String city) {
        return userRepository.findByCityAndDeletedAtIsNull(city).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByBloodGroup(BloodGroup bloodGroup) {
        return userRepository.findByBloodGroupAndDeletedAtIsNull(bloodGroup).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByCityAndBloodGroup(String city, BloodGroup bloodGroup) {
        return userRepository.findByCityAndBloodGroupAndDeletedAtIsNull(city, bloodGroup).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCity(request.getCity());
        user.setBloodGroup(request.getBloodGroup());

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void deactivateUser(String id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(String id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(true);
        userRepository.save(user);
    }

    // Helper method to convert Entity to DTO
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCity(user.getCity());
        response.setBloodGroup(user.getBloodGroup());
        response.setRole(user.getRole());
        response.setIsActive(user.IsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
