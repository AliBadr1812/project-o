package com.ali_b1812.app.dto.mapper;

import com.ali_b1812.app.dto.request.CreateUserRequest;
import com.ali_b1812.app.dto.request.UpdateUserRequest;
import com.ali_b1812.app.dto.response.UserResponse;
import com.ali_b1812.app.model.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    // Entity → Response
    UserResponse toResponse(User user);
    
    // CreateRequest → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "accountLockedUntil", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "emailVerifiedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(CreateUserRequest request);
    
    // Update Entity from Request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateUserRequest request, @MappingTarget User entity);
    
    // Calculate followers/following counts
    @AfterMapping
    default void calculateCounts(@MappingTarget UserResponse response, User user) {
        if (user.getFollowers() != null) {
            response.setFollowersCount(user.getFollowers().size());
        }
        if (user.getFollowing() != null) {
            response.setFollowingCount(user.getFollowing().size());
        }
    }
}