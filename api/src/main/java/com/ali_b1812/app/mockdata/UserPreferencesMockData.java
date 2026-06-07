package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.UserPreferencesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory per-user preferences store.
 * Uses "default" as the userId for a single-admin-user setup.
 */
@Component
public class UserPreferencesMockData {

    private final Map<String, UserPreferencesResponse> store = new ConcurrentHashMap<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Optional<UserPreferencesResponse> get(String userId) {
        return Optional.ofNullable(store.get(userId));
    }

    public UserPreferencesResponse upsert(String userId, String themeId, Boolean darkMode) {
        UserPreferencesResponse prefs = UserPreferencesResponse.builder()
                .userId(userId)
                .themeId(themeId)
                .darkMode(darkMode)
                .updatedAt(LocalDateTime.now().format(FMT))
                .build();
        store.put(userId, prefs);
        return prefs;
    }
}
