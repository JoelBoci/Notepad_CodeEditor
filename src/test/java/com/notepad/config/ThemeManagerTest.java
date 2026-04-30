package com.notepad.config;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.UIManager;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ThemeManagerTest {

    @Test
    @DisplayName("Test 1: Should Apply Light Theme")
    void test1_shouldApplyLightTheme() {
        ThemeManager.apply("light");
        assertInstanceOf(FlatLightLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 2: Should Apply Dark Theme")
    void test2_shouldApplyDarkTheme() {
        ThemeManager.apply("dark");
        assertInstanceOf(FlatDarkLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 3: Should Apply Darcula Theme")
    void test3_shouldApplyDarculaTheme() {
        ThemeManager.apply("darcula");
        assertInstanceOf(FlatDarculaLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 4: Should Apply Mac Light Theme")
    void test4_shouldApplyMacLightTheme() {
        ThemeManager.apply("macLight");
        assertInstanceOf(FlatMacLightLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 5: Should Apply Mac Dark Theme")
    void test5_shouldApplyMacDarkTheme() {
        ThemeManager.apply("macDark");
        assertInstanceOf(FlatMacDarkLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 6: Unknown Theme Should Default To Light")
    void test6_unknownThemeShouldDefaultToLight() {
        ThemeManager.apply("unknown");
        assertInstanceOf(FlatLightLaf.class, UIManager.getLookAndFeel());
    }

    @Test
    @DisplayName("Test 7: Null Theme Should Default To Light")
    void test7_nullThemeShouldDefaultToLight() {
        ThemeManager.apply(null);
        assertInstanceOf(FlatLightLaf.class, UIManager.getLookAndFeel());
    }
}
