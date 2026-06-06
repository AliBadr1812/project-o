package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.mapper.UserMapper;
import com.ali_b1812.app.dto.request.CreateUserRequest;
import com.ali_b1812.app.dto.request.UpdateUserRequest;
import com.ali_b1812.app.dto.response.UserResponse;
import com.ali_b1812.app.model.entity.User;
import com.ali_b1812.app.model.enums.UserRole;
import com.ali_b1812.app.repository.UserRepository;
import com.ali_b1812.app.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.Predicate;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements IUserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditLoggerService auditLogger;
    
    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        
        // Check if user exists
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create user entity
        User user = userMapper.toEntity(request);
        // Set fields that mapper intentionally defers to service
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        
        // Save user
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());

        auditLogger.logUserActivity(
            "CREATE_USER",
            savedUser.getId(),
            savedUser.getUserName(),
            "User account created",
            null,
            savedUser
        );
        
        return userMapper.toResponse(savedUser);
    }
    
    @Override
    public UserResponse createUserWithRole(CreateUserRequest request, UserRole role) {
        UserResponse user = createUser(request);
        
        // Update role
        User userEntity = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setRole(role);
        
        User updatedUser = userRepository.save(userEntity);
        return userMapper.toResponse(updatedUser);
    }
    
    @Override
    public UserResponse getUserById(Long id) {
        log.debug("Getting user by ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        return userMapper.toResponse(user);
    }
    
    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        return userMapper.toResponse(user);
    }
    
    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        return userMapper.toResponse(user);
    }
    
    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable, String search, UserRole role, Boolean isActive) {
        log.debug("Getting all users with filters - search: {}, role: {}, isActive: {}", search, role, isActive);
        
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Search filter
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate usernamePredicate = cb.like(cb.lower(root.get("userName")), searchPattern);
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), searchPattern);
                Predicate firstNamePredicate = cb.like(cb.lower(root.get("firstName")), searchPattern);
                Predicate lastNamePredicate = cb.like(cb.lower(root.get("lastName")), searchPattern);
                predicates.add(cb.or(usernamePredicate, emailPredicate, firstNamePredicate, lastNamePredicate));
            }
            
            // Role filter
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            
            // Active filter
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }
            
            // Exclude deleted users (soft delete)
            predicates.add(cb.isNull(root.get("deletedAt")));
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toResponse);
    }
    
    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        // Check if new username/email already exists (if being changed)
        if (request.getUsername() != null &&
            !request.getUsername().equals(user.getUserName()) &&
            userRepository.existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (request.getEmail() != null && 
            !request.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Update user
        userMapper.updateEntityFromRequest(request, user);
        User updatedUser = userRepository.save(user);

        auditLogger.logUserActivity(
            "UPDATE_USER",
            updatedUser.getId(),
            updatedUser.getUserName(),
            "User profile updated",
            user,
            updatedUser
        );
        
        return userMapper.toResponse(updatedUser);
    }
    
    @Override
    public UserResponse updateUserRole(Long id, UserRole role) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        
        return userMapper.toResponse(updatedUser);
    }
    
    @Override
    public UserResponse updateUserStatus(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setIsActive(isActive);
        User updatedUser = userRepository.save(user);
        
        return userMapper.toResponse(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        log.info("Soft deleting user ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setDeletedAt(LocalDateTime.now());
        user.setIsActive(false);
        userRepository.save(user);

        auditLogger.logUserActivity(
            "DELETE_USER",
            user.getId(),
            user.getUserName(),
            "User soft deleted",
            user,
            null
        );
    }
    
    @Override
    public void deleteUserPermanently(Long id) {
        log.warn("Permanently deleting user ID: {}", id);
        userRepository.deleteById(id);
    }
    
    @Override
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        log.info("Uploading profile picture for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // In a real app, you would upload to S3/Cloudinary/local storage
        // This is a simplified example
        String fileName = "profile-" + userId + "-" + System.currentTimeMillis() + 
                         getFileExtension(file.getOriginalFilename());
        
        // Store file logic here...
        String imageUrl = "/uploads/profiles/" + fileName;
        
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
        
        return imageUrl;
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return ".jpg";
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? ".jpg" : filename.substring(lastDot);
    }
    
    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total users
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);
        
        // Active users
        long activeUsers = userRepository.countByIsActiveTrue();
        stats.put("activeUsers", activeUsers);
        
        // Users by role
        Map<UserRole, Long> usersByRole = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            long count = userRepository.countByRole(role);
            usersByRole.put(role, count);
        }
        stats.put("usersByRole", usersByRole);
        
        // New users today
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long newUsersToday = userRepository.countByCreatedAtAfter(startOfDay);
        stats.put("newUsersToday", newUsersToday);
        
        return stats;
    }
    
    @Override
    public byte[] exportUsersToCsv() {
        List<User> users = userRepository.findAll();
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(outputStream)) {
            
            // Write CSV header
            writer.println("ID,Username,Email,Role,First Name,Last Name,Is Active,Created At");
            
            // Write data
            for (User user : users) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                    user.getId(),
                    escapeCsv(user.getUserName()),
                    escapeCsv(user.getEmail()),
                    user.getRole(),
                    escapeCsv(user.getFirstName()),
                    escapeCsv(user.getLastName()),
                    user.getIsActive(),
                    user.getCreatedAt()
                );
            }
            
            writer.flush();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error exporting users to CSV", e);
            throw new RuntimeException("Failed to export users to CSV", e);
        }
    }
    
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    @Override
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new RuntimeException("Follower not found"));
        
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new RuntimeException("User to follow not found"));
        
        follower.getFollowing().add(following);
        following.getFollowers().add(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
        
        log.info("User {} followed user {}", followerId, followingId);
    }
    
    @Override
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new RuntimeException("Follower not found"));
        
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new RuntimeException("User to unfollow not found"));
        
        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
        
        log.info("User {} unfollowed user {}", followerId, followingId);
    }
    
    @Override
    public Page<UserResponse> getFollowers(Long userId, Pageable pageable) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return userRepository.findByFollowingId(userId, pageable)
            .map(userMapper::toResponse);
    }
     
    @Override
    public Page<UserResponse> getFollowing(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return userRepository.findUsersFollowedBy(userId, pageable)
            .map(userMapper::toResponse);
    }
}