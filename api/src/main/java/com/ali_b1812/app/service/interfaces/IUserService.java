package com.ali_b1812.app.service.interfaces;

import com.ali_b1812.app.dto.request.CreateUserRequest;
import com.ali_b1812.app.dto.request.UpdateUserRequest;
import com.ali_b1812.app.dto.response.UserResponse;
import com.ali_b1812.app.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface IUserService {
    
    // User registration
    UserResponse createUser(CreateUserRequest request);
    
    UserResponse createUserWithRole(CreateUserRequest request, UserRole role);
    
    // User retrieval
    UserResponse getUserById(Long id);
    
    UserResponse getUserByUsername(String username);
    
    UserResponse getUserByEmail(String email);
    
    Page<UserResponse> getAllUsers(Pageable pageable, String search, UserRole role, Boolean isActive);
    
    // User updates
    UserResponse updateUser(Long id, UpdateUserRequest request);
    
    UserResponse updateUserRole(Long id, UserRole role);
    
    UserResponse updateUserStatus(Long id, Boolean isActive);
    
    // User deletion
    void deleteUser(Long id);
    
    void deleteUserPermanently(Long id);
    
    // Profile picture
    String uploadProfilePicture(Long userId, MultipartFile file);
    
    // Statistics
    Map<String, Object> getUserStatistics();
    
    byte[] exportUsersToCsv();
    
    // Followers/Following
    void followUser(Long followerId, Long followingId);
    
    void unfollowUser(Long followerId, Long followingId);
    
    Page<UserResponse> getFollowers(Long userId, Pageable pageable);
    
    Page<UserResponse> getFollowing(Long userId, Pageable pageable);
}
