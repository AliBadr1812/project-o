package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.response.UserPreferencesResponse;
import com.ali_b1812.app.mockdata.UserPreferencesMockData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPreferencesService {

    private final UserPreferencesMockData mockData;

    public Optional<UserPreferencesResponse> getPreferences(String userId) {
        return mockData.get(userId);
    }

    public UserPreferencesResponse savePreferences(String userId, UserPreferencesResponse patch) {
        // If partial patch, merge with existing
        Optional<UserPreferencesResponse> existing = mockData.get(userId);
        String themeId  = patch.getThemeId()  != null ? patch.getThemeId()
                        : existing.map(UserPreferencesResponse::getThemeId).orElse("purple");
        Boolean darkMode = patch.getDarkMode() != null ? patch.getDarkMode()
                         : existing.map(UserPreferencesResponse::getDarkMode).orElse(false);
        return mockData.upsert(userId, themeId, darkMode);
    }
}
