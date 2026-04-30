package com.notepad.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppConfigTest {

    private static final Preferences PREFS = Preferences.userRoot().node("com/notepad");

    @AfterEach
    void tearDown() throws BackingStoreException {
        PREFS.remove("theme");
        PREFS.flush();
    }

    @Test
    @DisplayName("Test 1: Should Return Light Theme By Default")
    void test1_shouldReturnLightThemeByDefault() throws BackingStoreException {
        PREFS.remove("theme");
        PREFS.flush();

        assertEquals("light", AppConfig.getTheme());
    }

    @Test
    @DisplayName("Test 2: Should Save And Return Selected Theme")
    void test2_shouldSaveAndReturnSelectedTheme() {
        AppConfig.setTheme("dark");
        assertEquals("dark", AppConfig.getTheme());
    }

    @Test
    @DisplayName("Test 3: Should Overwrite Existing Theme")
    void test3_shouldOverwriteExistingTheme() {
        AppConfig.setTheme("dark");
        AppConfig.setTheme("darcula");
        assertEquals("darcula", AppConfig.getTheme());
    }
}
