package com.ali_b1812.app.repository;

import com.ali_b1812.app.model.entity.User;
import com.ali_b1812.app.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // Basic finders
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    // Count methods
    long countByIsActiveTrue();
    
    long countByRole(UserRole role);
    
    long countByCreatedAtAfter(LocalDateTime date);
    
    // Followers/Following
    @Query("SELECT u FROM User u JOIN u.followers f WHERE f.id = :followerId")
    Page<User> findUsersFollowedBy(@Param("followerId") Long followerId, Pageable pageable);
    
    @Query("SELECT u FROM User u JOIN u.following f WHERE f.id = :followingId")
    Page<User> findByFollowingId(@Param("followingId") Long followingId, Pageable pageable);
    
    // Search
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);
}